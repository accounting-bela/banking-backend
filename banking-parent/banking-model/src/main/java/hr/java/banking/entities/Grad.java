package hr.java.banking.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Table(name="grad")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Grad extends BaseModel {
	
	@Column(name="naziv")
	private String naziv;
	
	@Column(name="post")
	private String post;
}
