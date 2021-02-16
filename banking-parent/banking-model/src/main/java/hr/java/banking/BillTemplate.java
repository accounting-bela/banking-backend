package hr.java.banking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import hr.java.banking.entities.Racun;
import hr.java.banking.entities.Stranka;

public class BillTemplate {
	
	public Header header;
	public List<Group> groups;
	
	public BillTemplate(List<Racun> racuni) {
		Set<Stranka> uplatitelji = racuni.stream().map(m -> m.getUplatitelj()).collect(Collectors.toSet());
		List<Group> groups = new ArrayList<Group>();
		this.header = new Header(racuni);
		for(Stranka u: uplatitelji)
		{
			List<Racun> r = racuni.stream().filter(f -> f.getUplatitelj().getId() == u.getId()).collect(Collectors.toList());
			Group group = new Group(r);
			groups.add(group);
			
		}
		this.groups = groups;
		
		
		
	}
	

}

class Header {
	public String msgId;
	public String creDtTm;
	public String nbOfTxs;
	public String ctrlSum;
	public String payerName;
	
	public Header(List<Racun> racuni) {
		DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
		this.msgId = "UN" + LocalDate.now().format(format1) + "0001" + "701";
		this.creDtTm = LocalDateTime.now().format(format2);
		this.nbOfTxs = String.valueOf(racuni.size());
		this.ctrlSum = String.valueOf(racuni.stream().mapToDouble(m -> (m.getIznos().doubleValue()/100)).sum());
		this.payerName = racuni.get(0).getUplatitelj().getNaziv();
		
	}

}

class Group {
	public String pmtInfId;
	public String nbOfTxs;
	public String ctrlSum;
	public String reqdExctnDt;
	public String dbtrNm;
	public String dbtrIban;
	public String bic;
	public List<Transaction> transactions;
	
	public Group(List<Racun> racuni)
	{
		this.pmtInfId = UUID.randomUUID().toString();
		this.nbOfTxs = String.valueOf(racuni.size());
		this.ctrlSum = String.valueOf(racuni.stream().mapToDouble(m -> (m.getIznos().doubleValue()/100)).sum());
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.reqdExctnDt = LocalDate.now().format(format);
		this.dbtrNm = racuni.get(0).getUplatitelj().getNaziv();
		this.dbtrIban = racuni.get(0).getUplatitelj().getIban();
		this.bic = null;
		List<Transaction> transactions = new ArrayList<Transaction>();
		for(Racun r: racuni)
		{
			Transaction transaction = new Transaction(r);
			transactions.add(transaction);
		}
		this.transactions = transactions;
		
	}
}

class Transaction {
	public String endToEndId;
	public String amt;
	public String cdtrNm;
	public String cdtrIban;
	public String ref;
	public String addtlRmtInf;
	
	public Transaction(Racun racun) {
		this.endToEndId = racun.getModel().name() + racun.getPozivNaBroj();
		this.amt = String.valueOf((racun.getIznos().doubleValue() / 100));
		this.cdtrNm = racun.getPrimatelj().getNaziv();
		this.cdtrIban = racun.getPrimatelj().getIban();
		this.ref = racun.getModel().name() + racun.getPozivNaBroj();
		this.addtlRmtInf = racun.getOpis();
	}
}