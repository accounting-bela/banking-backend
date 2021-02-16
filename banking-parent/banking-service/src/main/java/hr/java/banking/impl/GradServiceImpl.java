package hr.java.banking.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.banking.GradService;
import hr.java.banking.entities.Grad;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.repository.GradRepository;

@Service
public class GradServiceImpl extends BaseServiceImpl<Grad, GradRepository> implements GradService {

	@Autowired
	public GradServiceImpl(GradRepository repository) {
		super(repository);
	}

	@Override
	public Grad update(Grad object) throws BankingStatusException {
		Grad grad = super.findOne(object.getId()).get();
		
		grad.setNaziv(object.getNaziv());
		grad.setPost(object.getPost());
		
		return super.save(grad);
	}

	@Override
	public Optional<Grad> findByPost(String post) {
		return repository.findByPost(post);
	}
	

	


	

	


}
