package hr.java.banking.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import hr.java.banking.entities.Stranka;

public interface StrankaRepository extends BaseRepository<Stranka> {
	
	Optional<Stranka> findByNaziv(String naziv);
	
	Optional<Stranka> findByIbanAndNaziv(String iban, String naziv);

	Set<Stranka> findByKorisnikId(String korisnikId);

	Iterable<Stranka> findByIban(String iban);
	
	Iterable<Stranka> findByKorisnikIdNotOrKorisnikIdIsNull(String id);

}
