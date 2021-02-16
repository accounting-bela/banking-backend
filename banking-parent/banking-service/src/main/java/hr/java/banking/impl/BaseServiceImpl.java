package hr.java.banking.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.banking.BaseService;
import hr.java.banking.entities.BaseModel;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.repository.BaseRepository;

@Service
public abstract class BaseServiceImpl<T extends BaseModel, R extends BaseRepository<T>> implements BaseService<T> {
	
	
	R repository;
	
	
	public BaseServiceImpl(R repository) {
		this.repository = repository;
	}

	@Override
	public Iterable<T> getAll() {
		return repository.findAll();
	}

	@Override
	public Optional<T> findOne(UUID id) {
		return repository.findById(id);
	}

	@Override
	public T save(T object) throws BankingStatusException {
		return repository.save(object);
	}

	@Override
	public void delete(UUID id) {
		repository.deleteById(id);
		
	}
	
	

}
