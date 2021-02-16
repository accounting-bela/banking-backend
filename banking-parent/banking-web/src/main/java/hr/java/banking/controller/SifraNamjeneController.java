package hr.java.banking.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.java.banking.SifraNamjeneService;
import hr.java.banking.entities.SifraNamjene;

@RestController
@RequestMapping("api/sifra")
public class SifraNamjeneController {
	
	@Autowired
	private SifraNamjeneService sifraService;
	
	@GetMapping
	public Set<SifraNamjene> getAll() {
		return new HashSet<SifraNamjene>((Collection) sifraService.getAll());
	}

}
