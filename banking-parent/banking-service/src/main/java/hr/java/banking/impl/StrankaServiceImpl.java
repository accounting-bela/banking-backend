package hr.java.banking.impl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.banking.AdresaService;
import hr.java.banking.KorisnikService;
import hr.java.banking.StrankaService;
import hr.java.banking.entities.Adresa;
import hr.java.banking.entities.Korisnik;
import hr.java.banking.entities.Stranka;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.repository.StrankaRepository;


@Service
public class StrankaServiceImpl extends BaseServiceImpl<Stranka, StrankaRepository> implements StrankaService {
	
	private AdresaService adresaService;
	
	private KorisnikService korisnikService;

	@Autowired
	public StrankaServiceImpl(StrankaRepository repository, AdresaService adresaService,
			KorisnikService korisnikService) {
		super(repository);
		this.adresaService = adresaService;
		this.korisnikService = korisnikService;
	}





	@Override
	public Stranka save(Stranka object) throws BankingStatusException {
		Optional<Adresa> adresaOptional = adresaService.findByAdresaAndGrad(object.getAdresa().getAdresa(), object.getAdresa().getGrad().getPost());
		if(!adresaOptional.isPresent())
		{
			object.setAdresa(adresaService.save(object.getAdresa()));
		}
		else
		{
			object.setAdresa(adresaOptional.get());
		}
		return super.save(object);
	}





	@Override
	public Stranka update(Stranka object) throws BankingStatusException {
		Stranka stranka = null;
		Optional<Stranka> optionalStranka = repository.findById(object.getId());
		
		if(optionalStranka.isPresent())
		{
			stranka = optionalStranka.get();
		}
		else
		{
			throw new BankingStatusException(404, "Stranka s određenim identifikacijskim brojem ne postoji");
		}
		stranka.setNaziv(stranka.getNaziv());
		if(object.getAdresa().getId() == null)
		{
			stranka.setAdresa(object.getAdresa());
		}
		else
		{
			Optional<Adresa> optionalAdresa = adresaService.findOne(object.getId());
			if(optionalAdresa.isPresent())
			{
				stranka.setAdresa(optionalAdresa.get());
			}
			else
			{
				throw new BankingStatusException(404, "Adresa s određenim identifikacijskim brojem ne postoji");
			}
		}
		return stranka;
	}



	@Override
	public Optional<Stranka> findByIbanAndNaziv(String iban, String naziv) {
		return repository.findByIbanAndNaziv(iban, naziv);
	}





	@Override
	public Set<Stranka> findForKorisnik(String keycloakId) {
		Korisnik korisnik = korisnikService.findByKeycloakId(keycloakId).get();
		return korisnik.getStranke();
	}





	@Override
	public Iterable<Stranka> findOther(String keycloakId) {
		Korisnik korisnik = korisnikService.findByKeycloakId(keycloakId).get();
		return repository.findByKorisnik_IdNotOrKorisnikIsNull(korisnik.getId());
	}





	@Override
	public Stranka saveMyStranka(Stranka stranka, String keycloakId) throws BankingStatusException {
		Korisnik korisnik = korisnikService.findByKeycloakId(keycloakId).get();
		stranka.setKorisnik(korisnik);
		return save(stranka);
	}





	@Override
	public Optional<Stranka> findByNaziv(String naziv) {
		return repository.findByNaziv(naziv);
	}







}
