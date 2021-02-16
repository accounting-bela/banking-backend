package hr.java.banking.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import hr.java.banking.entities.SifraNamjene;

public interface SifraNamjeneRepository extends CrudRepository<SifraNamjene, Long> {
	
	Optional<SifraNamjene> findBySifra(String sifra);

}
