package hr.java.banking;

import java.util.Optional;

import hr.java.banking.entities.Adresa;

public interface AdresaService extends BaseService<Adresa> {
	
	Optional<Adresa> findByAdresaAndGrad(String adresa, String post);

}
