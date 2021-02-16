package hr.java.banking;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import hr.java.banking.entities.BaseModel;
import hr.java.banking.exceptions.BankingStatusException;

@Service
@Transactional
public interface BaseService<T extends BaseModel> {

	Iterable<T> getAll();
	
	Optional<T> findOne(UUID id);
	
	T save(T object) throws BankingStatusException;
	
	T update(T object) throws BankingStatusException;
	
	void delete(UUID id);
}
