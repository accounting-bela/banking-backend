package hr.java.banking.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Table(name="certifikat")
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Certifikat extends BaseModel {

    @Column(name="privatni_kljuc")
    private String privatniKljuc;

    @Column(name="certifikat")
    private String certifikat;

    @Column(name="serijski_broj")
    private String serijskiBroj;

    @Column(name="korisnik_id")
    private String korisnikId;
}
