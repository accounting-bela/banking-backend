package hr.java.banking.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Table(name="korisnik")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Korisnik extends BaseModel {
	
	@Column(name="username")
	private String username;
	
	@Column(name="keycloak_id")
	private String keycloakId;
	
	@OneToMany
	@Cascade(CascadeType.ALL)
	@JoinColumn(name="korisnik_id", referencedColumnName="id")
	private Set<Stranka> stranke;

}
