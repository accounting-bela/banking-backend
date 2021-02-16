package hr.java.banking.repository;

import java.util.UUID;

import hr.java.banking.entities.Racun;

public interface RacunRepository extends BaseRepository<Racun>  {
	
	Iterable<Racun> findAllByPrimatelj_Korisnik_Id(UUID id);
	
	Iterable<Racun> findAllByUplatitelj_Korisnik_Id(UUID id);

}
