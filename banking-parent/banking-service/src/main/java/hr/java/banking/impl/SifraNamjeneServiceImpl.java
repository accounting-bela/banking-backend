package hr.java.banking.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.banking.SifraNamjeneService;
import hr.java.banking.entities.SifraNamjene;
import hr.java.banking.repository.SifraNamjeneRepository;

@Service
public class SifraNamjeneServiceImpl implements SifraNamjeneService {
	
	private SifraNamjeneRepository repository;

	@Autowired
	public SifraNamjeneServiceImpl(SifraNamjeneRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<SifraNamjene> findOne(Long id) {
		return repository.findById(id);
		
	}

	@Override
	public Optional<SifraNamjene> findBySifra(String sifra) {
		return repository.findBySifra(sifra);
	}

	@Override
	public Iterable<SifraNamjene> getAll() {
		return repository.findAll();
	}

}
