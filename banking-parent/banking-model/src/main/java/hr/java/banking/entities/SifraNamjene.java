package hr.java.banking.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper=false)
@Table(name="sifra_namjene")
@Entity
public class SifraNamjene implements Serializable {
	
	@Id
	@Column(name="rbr")
	private Long rbr;
	
	@Column(name="klasifikacija")
	private String klasifikacija;
	
	@Column(name="sifra")
	private String sifra;
	
	@Column(name="naziv")
	private String naziv;
	
	@Column(name="definicija")
	private String definicija;
	
	

}
