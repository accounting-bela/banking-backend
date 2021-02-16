package hr.java.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hr.java.banking.KorisnikService;
import hr.java.banking.entities.Korisnik;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.request.KorisnikRequest;

@RestController
@RequestMapping("api/korisnik")
public class KorisnikController {
	
	@Autowired
	private KorisnikService korisnikService;
	
	@PostMapping("registriraj")
	public Korisnik createKorisnik(@RequestBody KorisnikRequest korisnikRequest) {
		
		try {
			return korisnikService.createUser(korisnikRequest);
		} catch (BankingStatusException e) {
			throw new ResponseStatusException(HttpStatus.valueOf(e.getCode()), e.getMessage());
		}
		
	}

}
