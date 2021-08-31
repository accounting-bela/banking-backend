package hr.java.banking;

import com.fasterxml.jackson.core.JsonProcessingException;
import hr.java.banking.entities.Certifikat;
import hr.java.banking.exceptions.BankingStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Optional;

public interface CertifikatService  extends BaseService<Certifikat>{

    Optional<Certifikat> findByKorisnikId(String korisnikId);

    Certifikat getDataFromCertificate(MultipartFile multipartFile, String password, String korisnikId) throws Exception;

    void initPayApi(Certifikat certifikat) throws Exception;
}
