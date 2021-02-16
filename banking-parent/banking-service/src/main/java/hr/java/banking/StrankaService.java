package hr.java.banking;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import hr.java.banking.entities.Stranka;
import hr.java.banking.exceptions.BankingStatusException;

public interface StrankaService extends BaseService<Stranka> {
	
	Optional<Stranka> findByNaziv(String naziv);
	
	Optional<Stranka> findByIbanAndNaziv(String iban, String naziv);
	
	Set<Stranka> findForKorisnik(String keycloakId);
	
	Iterable<Stranka> findOther(String keycloakId);
	
	Stranka saveMyStranka(Stranka stranka, String keycloakId) throws BankingStatusException;

}
