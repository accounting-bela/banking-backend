package hr.java.banking.request;


import lombok.Data;

@Data
public class KorisnikRequest {
	
	private String korisnicko;
	
	private String lozinka;
	
	private String email;
	
	private String ime;
	
	private String prezime;

}
