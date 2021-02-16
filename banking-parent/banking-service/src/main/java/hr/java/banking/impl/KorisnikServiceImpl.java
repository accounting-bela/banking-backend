package hr.java.banking.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.banking.KorisnikService;
import hr.java.banking.entities.Korisnik;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.keycloak.KeycloakService;
import hr.java.banking.repository.KorisnikRepository;
import hr.java.banking.request.KorisnikRequest;

@Service
public class KorisnikServiceImpl extends BaseServiceImpl<Korisnik, KorisnikRepository> implements KorisnikService {
	
	private KeycloakService keycloakService;

	@Autowired
	public KorisnikServiceImpl(KorisnikRepository repository, KeycloakService keycloakService) {
		super(repository);
		this.keycloakService = keycloakService;
	}

	@Override
	public Korisnik update(Korisnik object) throws BankingStatusException {
		Optional<Korisnik> optionalKorisnik = super.findOne(object.getId());
		Korisnik korisnik = null;
		if(optionalKorisnik.isPresent())
		{
			korisnik = optionalKorisnik.get();
		}
		else
		{
			throw new BankingStatusException(404, "Korisnik s određenim identifikacijskim brojem ne postoji");
		}
		korisnik.setStranke(object.getStranke());
		
		return super.save(korisnik);
	}

	@Override
	public Optional<Korisnik> findByKeycloakId(String id) {
		return repository.findAllByKeycloakId(id);
	}

	@Override
	public Optional<Korisnik> findByUsername(String name) {
		return repository.findByUsername(name);
	}

	@Override
	public Korisnik createUser(KorisnikRequest korisnikRequest) throws BankingStatusException {
		Korisnik korisnik = keycloakService.createAccount(korisnikRequest);
		return super.save(korisnik);
	}


	

}
