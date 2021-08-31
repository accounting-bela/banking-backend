package hr.java.banking.controller;

import hr.java.banking.CertifikatService;
import hr.java.banking.entities.Certifikat;
import hr.java.banking.exceptions.BankingStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping("api/certifikat")
public class CertifikatController {

    @Autowired
    private CertifikatService certifikatService;

    @PostMapping("/upload")
    public Certifikat uploadCertificate(@RequestParam("file") MultipartFile file, @RequestParam("password") String password) throws Exception {
        String keycloakId = SecurityContextHolder.getContext().getAuthentication().getName();
        return certifikatService.getDataFromCertificate(file, password, keycloakId);
    }
}
