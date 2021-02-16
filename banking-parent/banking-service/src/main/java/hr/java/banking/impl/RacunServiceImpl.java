package hr.java.banking.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import hr.java.banking.BillTemplate;
import hr.java.banking.KorisnikService;
import hr.java.banking.RacunService;
import hr.java.banking.RequestService;
import hr.java.banking.SifraNamjeneService;
import hr.java.banking.StrankaService;
import hr.java.banking.entities.Korisnik;
import hr.java.banking.entities.Racun;
import hr.java.banking.entities.SifraNamjene;
import hr.java.banking.entities.Stranka;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.repository.RacunRepository;

@Service
public class RacunServiceImpl extends BaseServiceImpl<Racun, RacunRepository> implements RacunService {
	
	private StrankaService strankaService;
	
	private KorisnikService korisnikService;
	
	private SifraNamjeneService sifraService;
	
	private RequestService requestService;
	
	private SpringTemplateEngine engine;


	@Autowired
	public RacunServiceImpl(RacunRepository repository, StrankaService strankaService, KorisnikService korisnikService,
			SifraNamjeneService sifraService, RequestService requestService, SpringTemplateEngine engine) {
		super(repository);
		this.strankaService = strankaService;
		this.korisnikService = korisnikService;
		this.sifraService = sifraService;
		this.requestService = requestService;
		this.engine = engine;
	}

	@Override
	public Racun update(Racun object) throws BankingStatusException {
		Racun racun = new Racun();
		Optional<Stranka> optionalStranka = strankaService.findOne(object.getPrimatelj().getId());
		Stranka primatelj = null;
		if(optionalStranka.isPresent())
		{
			primatelj = optionalStranka.get();
		}
		else
		{
			optionalStranka = strankaService.findByIbanAndNaziv(object.getPrimatelj().getIban(), object.getPrimatelj().getNaziv());
			if(optionalStranka.isPresent())
			{
				primatelj = optionalStranka.get();
				racun.setPrimatelj(primatelj);
			}
			else
			{
				racun.setPrimatelj(strankaService.save(object.getPrimatelj()));
			}
		}
		optionalStranka = strankaService.findOne(object.getPrimatelj().getId());
		Stranka uplatitelj = null;
		if(optionalStranka.isPresent())
		{
			uplatitelj = optionalStranka.get();
		}
		else
		{
			optionalStranka = strankaService.findByIbanAndNaziv(object.getUplatitelj().getIban(), object.getPrimatelj().getNaziv());
			if(optionalStranka.isPresent())
			{
				uplatitelj = optionalStranka.get();
				racun.setPrimatelj(uplatitelj);
			}
			else
			{
				racun.setUplatitelj(strankaService.save(object.getPrimatelj()));
			}
		}
		racun.setIban(object.getPrimatelj().getIban());
		racun.setIznos(object.getIznos());
		racun.setModel(object.getModel());
		racun.setPozivNaBroj(object.getPozivNaBroj());
		racun.setUplatnica(object.getUplatnica());
		racun.setOpis(object.getOpis());
		racun.setValuta(object.getValuta());
		
		return super.save(racun);
		
		
		
	}
	
	@Override
	public Racun createRacunOutgoing(Racun object, String keycloakId) throws BankingStatusException {
		Korisnik korisnik = korisnikService.findByKeycloakId(keycloakId).get();
		Racun racun = new Racun();
		Optional<Stranka> optionalStranka = object.getPrimatelj().getId() != null ? strankaService.findOne(object.getPrimatelj().getId()) : Optional.empty();
		Stranka primatelj = null;
		if(optionalStranka.isPresent())
		{
			primatelj = optionalStranka.get();
		}
		else
		{
			optionalStranka = strankaService.findByIbanAndNaziv(object.getPrimatelj().getIban(), object.getPrimatelj().getNaziv());
			if(optionalStranka.isPresent())
			{
				primatelj = optionalStranka.get();
			}
			else
			{
				primatelj = object.getPrimatelj();
				primatelj.setKorisnik(korisnik);
				primatelj = strankaService.save(primatelj);
			}
		}
		racun.setPrimatelj(primatelj);;
		optionalStranka = object.getUplatitelj().getId() != null ? strankaService.findOne(object.getUplatitelj().getId()) : Optional.empty();
		Stranka uplatitelj = null;
		if(optionalStranka.isPresent())
		{
			uplatitelj = optionalStranka.get();
		}
		else
		{
			optionalStranka = strankaService.findByIbanAndNaziv(object.getUplatitelj().getIban(), object.getUplatitelj().getNaziv());
			if(optionalStranka.isPresent())
			{
				uplatitelj = optionalStranka.get();
			}
			else
			{
				uplatitelj = strankaService.save(object.getUplatitelj());
			}
		}
		racun.setUplatitelj(uplatitelj);
		Optional<SifraNamjene> optionalSifra = Optional.empty(); 
		if(object.getSifraNamjene().getRbr() != null)
		{
			optionalSifra = sifraService.findOne(object.getSifraNamjene().getRbr());
		}
		else if(object.getSifraNamjene().getSifra() != null)
		{
			optionalSifra = sifraService.findBySifra(object.getSifraNamjene().getSifra());
		}
		racun.setSifraNamjene(optionalSifra.get());
		racun.setIban(object.getPrimatelj().getIban());
		racun.setIznos(object.getIznos());
		racun.setModel(object.getModel());
		racun.setPozivNaBroj(object.getPozivNaBroj());
		racun.setUplatnica(object.getUplatnica());
		racun.setOpis(object.getOpis());
		racun.setValuta(object.getValuta());
		
		return super.save(racun);
	}







	@Override
	public Racun createRacunIngoing(Racun object, String keycloakId) throws BankingStatusException {
		Korisnik korisnik = korisnikService.findByKeycloakId(keycloakId).get();
		Racun racun = new Racun();
		Optional<Stranka> optionalStranka = object.getPrimatelj().getId() != null ? strankaService.findOne(object.getPrimatelj().getId()) : Optional.empty();
		Stranka primatelj = null;
		if(optionalStranka.isPresent())
		{
			primatelj = optionalStranka.get();
		}
		else
		{
			if(object.getPrimatelj().getIban() == null)
			{
				optionalStranka = strankaService.findByNaziv(object.getPrimatelj().getNaziv());
			}
			else
			{
				optionalStranka = strankaService.findByIbanAndNaziv(object.getPrimatelj().getIban(), object.getPrimatelj().getNaziv());
			}
			
			if(optionalStranka.isPresent())
			{
				primatelj = optionalStranka.get();
			}
			else
			{
				primatelj = strankaService.save(object.getPrimatelj());
			}
		}
		racun.setPrimatelj(primatelj);;
		optionalStranka = object.getUplatitelj().getId() != null ? strankaService.findOne(object.getUplatitelj().getId()) : Optional.empty();
		Stranka uplatitelj = null;
		if(optionalStranka.isPresent())
		{
			uplatitelj = optionalStranka.get();
		}
		else
		{
			if(object.getUplatitelj().getIban() == null)
			{
				optionalStranka = strankaService.findByNaziv(object.getUplatitelj().getNaziv());
			}
			else
			{
				optionalStranka = strankaService.findByIbanAndNaziv(object.getUplatitelj().getIban(), object.getUplatitelj().getNaziv());
			}
			if(optionalStranka.isPresent())
			{
				uplatitelj = optionalStranka.get();
			}
			else
			{
				uplatitelj = object.getUplatitelj();
				uplatitelj.setKorisnik(korisnik);
				strankaService.save(uplatitelj);
			}
		}
		racun.setUplatitelj(uplatitelj);
		Optional<SifraNamjene> optionalSifra = Optional.empty(); 
		if(object.getSifraNamjene().getRbr() != null)
		{
			optionalSifra = sifraService.findOne(object.getSifraNamjene().getRbr());
		}
		else if(object.getSifraNamjene().getSifra() != null)
		{
			optionalSifra = sifraService.findBySifra(object.getSifraNamjene().getSifra());
		}
		racun.setSifraNamjene(optionalSifra.get());
		racun.setIban(object.getPrimatelj().getIban());
		racun.setIznos(object.getIznos());
		racun.setModel(object.getModel());
		racun.setPozivNaBroj(object.getPozivNaBroj());
		racun.setUplatnica(object.getUplatnica());
		racun.setOpis(object.getOpis());
		racun.setValuta(object.getValuta());
		
		return super.save(racun);
	}










	@Override
	public InputStreamResource createQrCode(UUID id) throws BankingStatusException {
		Racun racun = repository.findById(id).get();
		return requestService.getQrCode(racun);
		
	}







	@Override
	public Iterable<Racun> findIngoingRacun(String id) {
		Korisnik korisnik = korisnikService.findByKeycloakId(id).get();
		return repository.findAllByUplatitelj_Korisnik_Id(korisnik.getId());
	}







	@Override
	public Iterable<Racun> findOutgoingRacun(String id) {
		Korisnik korisnik = korisnikService.findByKeycloakId(id).get();
		return repository.findAllByPrimatelj_Korisnik_Id(korisnik.getId());
	}







	@Override
	public InputStreamResource getXml(List<UUID> ids) throws BankingStatusException {
		List<Racun> racuni = new ArrayList((Collection)repository.findAllById(ids));
		BillTemplate bill = new BillTemplate(racuni);
		Context context = new Context();
		context.setVariable("bill", bill);
		
		String content = engine.process("racuni", context);
		ByteArrayInputStream byteStream = null;
		try {
			String out = this.stringToDom(content);
			File file = new File(out);
		    byteStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
		} catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
			throw new BankingStatusException(400, e.getLocalizedMessage());
		}
		
		return new InputStreamResource(byteStream);
		
		
		
	}
	
	public String stringToDom(String xmlSource) 
	        throws SAXException, ParserConfigurationException, IOException, TransformerException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);

	    String out = "racuni.xml";
	    StreamResult result =  new StreamResult(new File(out));
	    transformer.transform(source, result);
	    
	    return out;
	}








	
	

	

}
