package hr.java.banking.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hr.java.banking.RacunService;
import hr.java.banking.entities.Racun;
import hr.java.banking.exceptions.BankingStatusException;

@RestController
@RequestMapping("api/racun")
public class RacunController {
	
	@Autowired
	private RacunService racunService;
	
	@GetMapping
	public Set<Racun> getAll() {
		return new HashSet<Racun>((Collection) racunService.getAll());
	}
	
	@GetMapping("ingoing")
	public Set<Racun> getIngoing() {
		return new HashSet<Racun>((Collection) racunService.findIngoingRacun(SecurityContextHolder.getContext().getAuthentication().getName()));
	}
	
	@GetMapping("outgoing")
	public Set<Racun> getOutgoing() {
		return new HashSet<Racun>((Collection) racunService.findOutgoingRacun(SecurityContextHolder.getContext().getAuthentication().getName()));
	}
	
	@PostMapping("ingoing")
	public Racun saveIngoing(@RequestBody Racun racun) {
		Racun rac = null;
		try {
			rac = this.racunService.createRacunIngoing(racun, SecurityContextHolder.getContext().getAuthentication().getName());
		} catch (BankingStatusException e) {
			throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
		}
		
		return rac;
		
	}
	
	@PostMapping("outgoing")
	public Racun saveOutgoing(@RequestBody Racun racun) {
		Racun rac = null;
		try {
			rac = this.racunService.createRacunOutgoing(racun, SecurityContextHolder.getContext().getAuthentication().getName());
		} catch (BankingStatusException e) {
			throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
		}
		
		return rac;
		
	}
	
	@DeleteMapping
	public void delete(@RequestParam UUID id) {
		this.racunService.delete(id);
	}
	
	@RequestMapping(value = "pdf", method = RequestMethod.GET)
	public InputStreamResource createQrCode(@RequestParam("id") String id) {
		InputStreamResource res = null;
		try {
			res = racunService.createQrCode(UUID.fromString(id));
		} catch (BankingStatusException e) {
			throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
		}
		return res;
	}
	
	@RequestMapping(value = "xml", method = RequestMethod.POST)
	public InputStreamResource createXml(@RequestBody List<UUID> ids) {
		
		InputStreamResource res = null;
		try {
			res = racunService.getXml(ids);
		} catch (BankingStatusException e) {
			throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
		}
		return res;

	}
	

}
