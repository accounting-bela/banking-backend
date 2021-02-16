package hr.java.banking.repository;

import java.util.Optional;
import java.util.UUID;

import hr.java.banking.entities.Stranka;

public interface StrankaRepository extends BaseRepository<Stranka> {
	
	Optional<Stranka> findByNaziv(String naziv);
	
	Optional<Stranka> findByIbanAndNaziv(String iban, String naziv);
	
	Iterable<Stranka> findByKorisnik_IdNotOrKorisnikIsNull(UUID id);

}
