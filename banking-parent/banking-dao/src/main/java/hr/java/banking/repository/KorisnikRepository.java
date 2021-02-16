package hr.java.banking.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hr.java.banking.entities.Korisnik;

@Repository
public interface KorisnikRepository extends BaseRepository<Korisnik> {
	
	Optional<Korisnik> findAllByKeycloakId(String id);
	
	Optional<Korisnik> findByUsername(String username);

}
