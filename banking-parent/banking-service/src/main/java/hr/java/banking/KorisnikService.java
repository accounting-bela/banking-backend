package hr.java.banking;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hr.java.banking.entities.Korisnik;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.request.KorisnikRequest;

@Service
public interface KorisnikService extends BaseService<Korisnik> {
	
	Optional<Korisnik> findByKeycloakId(String id);
	
	Optional<Korisnik> findByUsername(String name);
	
	Korisnik createUser(KorisnikRequest korisnikRequest) throws BankingStatusException;

}
