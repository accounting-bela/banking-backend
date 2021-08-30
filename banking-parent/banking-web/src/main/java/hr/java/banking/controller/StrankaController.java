package hr.java.banking.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import hr.java.banking.StrankaService;
import hr.java.banking.entities.Stranka;
import hr.java.banking.exceptions.BankingStatusException;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("api/stranka")
public class StrankaController {
	
	@Autowired
	private StrankaService strankaService;
	
	@GetMapping
	public Set<Stranka> getAll() {
		return new HashSet<Stranka>((Collection) strankaService.getAll());
	}
	
	@GetMapping("korisnik")
	public Set<Stranka> getForKorisnik() {
		String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
		return strankaService.findForKorisnik(keycloakId);
	}
	
	@GetMapping("other")
	public Set<Stranka> getOther() {
		String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
		return new HashSet<Stranka>((Collection) strankaService.findOther(keycloakId));
	}

	@GetMapping("iban")
	public Set<Stranka> getByIban(@RequestParam String iban) {
		return new HashSet<Stranka>((Collection) strankaService.findByIban(iban));
	}
	
	@PostMapping
	public Stranka save(@RequestBody Stranka stranka) {
		Stranka str = null;
		try {
			str = strankaService.save(stranka);
		} catch (BankingStatusException e) {
			throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
		}
		
		return str;
	}
	
	@PostMapping("my-stranka")
	public Stranka saveMyStranka(@RequestBody Stranka stranka) {
		String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
		Stranka str = null;
		try {
			str = strankaService.saveMyStranka(stranka, keycloakId);
		} catch (BankingStatusException e) {
			throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
		}
		
		return str;
	}

}
