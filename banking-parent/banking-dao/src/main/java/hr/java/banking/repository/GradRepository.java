package hr.java.banking.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import hr.java.banking.entities.Grad;

@Repository
public interface GradRepository extends BaseRepository<Grad> {
	
	Optional<Grad> findByPost(String post);

}
