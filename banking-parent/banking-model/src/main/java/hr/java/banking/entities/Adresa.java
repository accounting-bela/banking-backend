package hr.java.banking.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Table(name="adresa")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Adresa extends BaseModel {
	
	@Column(name="adresa")
	private String adresa;
	
	@ManyToOne
	@Cascade(CascadeType.PERSIST)
	@JoinColumn(name = "grad_id", referencedColumnName = "id")
	private Grad grad;

}
