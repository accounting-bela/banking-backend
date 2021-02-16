package hr.java.banking;

import java.util.Optional;

import hr.java.banking.entities.Grad;

public interface GradService extends BaseService<Grad> {
	
	Optional<Grad> findByPost(String post);

}
