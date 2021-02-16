package hr.java.banking.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.java.banking.AdresaService;
import hr.java.banking.GradService;
import hr.java.banking.entities.Adresa;
import hr.java.banking.entities.Grad;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.repository.AdresaRepository;

@Service
public class AdresaServiceImpl extends BaseServiceImpl<Adresa, AdresaRepository> implements AdresaService {
	
	private GradService gradService;

	
	@Autowired
	public AdresaServiceImpl(AdresaRepository repository, GradService gradService) {
		super(repository);
		this.gradService = gradService;
	}



	@Override
	public Adresa update(Adresa object) {
		Adresa adresa = super.findOne(object.getId()).get();
		adresa.setAdresa(object.getAdresa());
		adresa.setGrad(object.getGrad());
		
		return adresa;
		
	}
	
	

	@Override
	public Adresa save(Adresa object) throws BankingStatusException {
		Optional<Grad> gradOptional = gradService.findByPost(object.getGrad().getPost());
		if(gradOptional.isPresent())
		{
			object.setGrad(gradOptional.get());
		}
		else
		{
			object.setGrad(gradService.save(object.getGrad()));
		}
		return super.save(object);
	}

	@Override
	public Optional<Adresa> findByAdresaAndGrad(String adresa, String post) {
		return repository.findByAdresaAndGrad_Post(adresa, post);
	}

	

}
