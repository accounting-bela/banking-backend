package hr.java.banking.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.java.banking.CertifikatService;
import hr.java.banking.GradService;
import hr.java.banking.entities.Certifikat;
import hr.java.banking.entities.Grad;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.repository.CertifikatRepository;
import hr.java.banking.repository.GradRepository;
import hr.java.banking.util.CertificateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Service
public class CertifikatServiceImpl extends BaseServiceImpl<Certifikat, CertifikatRepository> implements CertifikatService {

    @Autowired
    private RestTemplate restTemplate;

    public CertifikatServiceImpl(CertifikatRepository repository) {
        super(repository);
    }

    @Override
    public Certifikat update(Certifikat object) throws BankingStatusException {
        Certifikat certifikat = super.findOne(object.getId()).get();

        certifikat.setCertifikat(object.getCertifikat());
        certifikat.setPrivatniKljuc(object.getPrivatniKljuc());
        certifikat.setSerijskiBroj(object.getSerijskiBroj());
        certifikat.setKorisnikId(object.getKorisnikId());

        return super.save(certifikat);
    }

    @Override
    public Optional<Certifikat> findByKorisnikId(String korisnikId) {
        return repository.findByKorisnikId(korisnikId);
    }

    @Override
    public Certifikat getDataFromCertificate(MultipartFile multipartFile, String password, String korisnikId) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        InputStream input = new BufferedInputStream(multipartFile.getInputStream());
        keyStore.load(input, password.toCharArray());
        List<String> e = Collections.list(keyStore.aliases());
        X509Certificate certificate = null;
        PrivateKey key = null;
        for(String alias : e) {
            certificate = (X509Certificate) keyStore
                    .getCertificate(alias);
            key = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        }
        Optional<Certifikat> certifikatOptional = findByKorisnikId(korisnikId);
        Certifikat certifikat = new Certifikat();
        if(certifikatOptional.isPresent()) {
            certifikat = certifikatOptional.get();
        } else {
            certifikat.setKorisnikId(korisnikId);
        }
        certifikat.setCertifikat(Base64.getEncoder().encodeToString(certificate.getEncoded()));
        certifikat.setPrivatniKljuc(Base64.getEncoder().encodeToString(key.getEncoded()));
        certifikat.setSerijskiBroj(certificate.getSerialNumber().toString());

        certifikat = super.save(certifikat);
        initPayApi(certifikat);
        return certifikat;
    }

    @Override
    public void initPayApi(Certifikat certifikat) throws Exception {

        HttpHeaders headers = new HttpHeaders();

        Map<String, String> body = new HashMap<>();
        body.put("userEmail", "lahur.123@gmail.com");

        ObjectMapper objectMapper = new ObjectMapper();

        String digest = "sha-512=" + Base64.getEncoder().encodeToString(CertificateUtils.encryptThisString(objectMapper
                .writeValueAsString(body)).getBytes(StandardCharsets.UTF_8));

        headers.add("digest", digest);
        headers.add("x-request-id", UUID.randomUUID().toString());

        String singing_string = "";

        for(String key : headers.toSingleValueMap().keySet()) {
            singing_string = singing_string + key + ": " + headers.toSingleValueMap().get(key) + "\n";
        }
        singing_string = singing_string.substring(0, singing_string.length() - 2);

        String signedString = Base64.getEncoder().encodeToString(CertificateUtils.signSHA256RSA(singing_string, certifikat.getPrivatniKljuc()).getBytes(StandardCharsets.UTF_8));

        Map<String, String> signatureHeaderMap = new HashMap<>();

        signatureHeaderMap.put("keyId", certifikat.getSerijskiBroj());
        signatureHeaderMap.put("algorithm", "rsa-sha256");
        signatureHeaderMap.put("headers", String.join(" ", headers.toSingleValueMap().keySet()));
        signatureHeaderMap.put("signature", signedString);

        String signatureHeader = "";

        for(String key : signatureHeaderMap.keySet()) {
            signatureHeader = signatureHeader + key + "=\"" + signatureHeaderMap.get(key) + "\",";
        }
        signatureHeader = signatureHeader.substring(0, singing_string.length() - 1);

        headers.add("signature", signatureHeader);

        headers.add("TPP-Signature-Certificate", certifikat.getCertifikat());

        HttpEntity<Map<String, String>> entity = new HttpEntity(body, headers);

        System.out.println(headers.toSingleValueMap());

        System.out.println(restTemplate.exchange("https://api-sandbox.zaba.hr/tpp/v1/authentications", HttpMethod.POST, entity, Map.class));





    }
}
