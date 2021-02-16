package hr.java.banking;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hr.java.banking.entities.SifraNamjene;

@Service
@Transactional
public interface SifraNamjeneService {
	
	Iterable<SifraNamjene> getAll();
	
	Optional<SifraNamjene> findOne(Long id);
	
	Optional<SifraNamjene> findBySifra(String sifra);

}
