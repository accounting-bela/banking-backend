package hr.java.banking.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import hr.java.banking.enums.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Table(name="racun")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Racun extends BaseModel {
	
	@Column(name="uplatnica")
	private String uplatnica;
	
	@Column(name="valuta")
	private String valuta;
	
	@Column(name="iznos")
	private Long iznos;
	
	@Column(name="iban")
	private String iban;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "model", length = 4)
	private Model model;
	
	@Column(name="poziv_na_broj")
	private String pozivNaBroj;
	
	@Column(name="opis")
	private String opis;
	
	@ManyToOne
	@JoinColumn(name = "sifra_namjene_id", referencedColumnName = "rbr")
	private SifraNamjene sifraNamjene;
	
	@ManyToOne
	@JoinColumn(name = "uplatitelj_id", referencedColumnName = "id")
	private Stranka uplatitelj;
	
	@ManyToOne
	@JoinColumn(name = "primatelj_id", referencedColumnName = "id")
	private Stranka primatelj;
	
	

}
