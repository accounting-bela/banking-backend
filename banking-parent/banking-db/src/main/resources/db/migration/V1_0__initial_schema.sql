CREATE TABLE "sifra_namjene"(
   rbr INTEGER  NOT NULL PRIMARY KEY ,
   klasifikacija VARCHAR(40) NOT NULL,
   sifra VARCHAR(5) NOT NULL,
   naziv VARCHAR(54) NOT NULL,
   definicija VARCHAR(252) NOT NULL
);

CREATE TABLE "grad"(
	id VARCHAR(36) NOT NULL PRIMARY KEY,
	naziv VARCHAR(100) NOT NULL,
	post VARCHAR(5) NOT NULL
);

CREATE TABLE "adresa"(
	id VARCHAR(36) NOT NULL PRIMARY KEY,
	adresa VARCHAR(100) NOT NULL,
	grad_id VARCHAR(36) NOT NULL,
	FOREIGN KEY(grad_id) REFERENCES grad(id)
);

CREATE TABLE "stranka"(
	id VARCHAR(36) NOT NULL PRIMARY KEY,
	naziv VARCHAR(100) NOT NULL,
	iban VARCHAR(30) NOT NULL,
	adresa_id VARCHAR(36) NOT NULL,
	korisnik_id VARCHAR(36),
	FOREIGN KEY(adresa_id) REFERENCES adresa(id)
);



CREATE TABLE "racun"(
	id VARCHAR(36) NOT NULL PRIMARY KEY,
	uplatnica VARCHAR(30) NOT NULL,
	valuta VARCHAR(3) NOT NULL,
	iznos BIGINT NOT NULL,
	model VARCHAR(4) NOT NULL,
	poziv_na_broj VARCHAR(100) NOT NULL,
	opis VARCHAR(100) NOT NULL,
	iban VARCHAR(30) NOT NULL,
	sifra_namjene_id INTEGER NOT NULL,
	uplatitelj_id VARCHAR(36) NOT NULL,
	primatelj_id VARCHAR(36) NOT NULL,
    FOREIGN KEY(uplatitelj_id) REFERENCES stranka(id),
    FOREIGN KEY(primatelj_id) REFERENCES stranka(id),
    FOREIGN KEY(sifra_namjene_id) REFERENCES sifra_namjene(rbr)
);
