package hr.java.banking.repository;

import hr.java.banking.entities.Certifikat;

import java.util.Optional;

public interface CertifikatRepository extends BaseRepository<Certifikat>{

    Optional<Certifikat> findByKorisnikId(String korisnikId);
}
