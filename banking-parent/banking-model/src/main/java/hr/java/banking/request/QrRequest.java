package hr.java.banking.request;

import java.math.BigDecimal;

import hr.java.banking.entities.Racun;
import lombok.Data;

@Data
public class QrRequest {
	
	private String renderer;
	private QrData data;
	private QrOptions options;
	
	
	public QrRequest(Racun racun) {
		this.renderer = "image";
		this.data = new QrData(racun);
		QrOptions options = new QrOptions();
		options.setColor("#000000");
		options.setFormat("jpg");
		this.options = options;
	}
	
	
	
	

}

@Data
class QrData {
	private BigDecimal amount;
	private String purpose;
	private String description;
	private Sender sender;
	private Receiver receiver;
	
	public QrData(Racun racun) {
		this.amount = BigDecimal.valueOf(((double)racun.getIznos())/100);
		this.purpose = racun.getSifraNamjene().getSifra();
		this.description = racun.getOpis();
		Sender sender = new Sender();
		sender.setName(racun.getUplatitelj().getNaziv());
		sender.setStreet(racun.getUplatitelj().getAdresa().getAdresa());
		sender.setPlace(racun.getUplatitelj().getAdresa().getGrad().getPost() + " " + racun.getUplatitelj().getAdresa().getGrad().getNaziv());
		this.sender = sender;
		Receiver reciever = new Receiver();
		reciever.setName(racun.getPrimatelj().getNaziv());
		reciever.setStreet(racun.getPrimatelj().getAdresa().getAdresa());
		reciever.setPlace(racun.getPrimatelj().getAdresa().getGrad().getPost() + " " + racun.getPrimatelj().getAdresa().getGrad().getNaziv());
		reciever.setIban(racun.getPrimatelj().getIban());
		reciever.setModel(racun.getModel().name().substring(2, 4));
		reciever.setReference(racun.getPozivNaBroj());
		this.receiver = reciever;
		
	}
	
}

@Data
class QrOptions {
	private String format;
	private String color;
}

@Data
class Sender {
	private String name;
	private String street;
	private String place;
}

@Data
class Receiver {
	private String name;
	private String street;
	private String place;
	private String iban;
	private String model;
	private String reference;
}
