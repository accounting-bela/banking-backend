package hr.java.banking;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.core.io.InputStreamResource;
import org.xml.sax.SAXException;

import hr.java.banking.entities.Racun;
import hr.java.banking.exceptions.BankingStatusException;

public interface RacunService extends BaseService<Racun> {
	
	Racun createRacunIngoing(Racun racun, String keycloakId) throws BankingStatusException;
	
	Racun createRacunOutgoing(Racun racun, String keycloakId) throws BankingStatusException;
	
	InputStreamResource createQrCode(UUID id) throws BankingStatusException;
	
	Iterable<Racun> findIngoingRacun(String id);
	
	Iterable<Racun> findOutgoingRacun(String id);
	
	InputStreamResource getXml(List<UUID> ids) throws BankingStatusException;

}
