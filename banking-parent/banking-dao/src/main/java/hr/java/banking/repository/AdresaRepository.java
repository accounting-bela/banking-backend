package hr.java.banking.repository;

import java.util.Optional;

import hr.java.banking.entities.Adresa;

public interface AdresaRepository extends BaseRepository<Adresa> {
	
	Optional<Adresa> findByAdresaAndGrad_Post(String adresa, String post);
	
}
