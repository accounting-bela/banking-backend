package hr.java.banking.keycloak;

import java.net.URI;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import hr.java.banking.entities.Korisnik;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.request.KorisnikRequest;

@Service
public class KeycloakService {
	
	@Value("${keycloak.auth-server-url}")
    private String SERVER_URL;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${myKeycloak.username}")
    private String USERNAME;

    @Value("${myKeycloak.password}")
    private String PASSWORD;

    @Value("${keycloak.resource}")
    private String CLIENT_ID;
    
    @Value("${keycloak.credentials.secret}") 
    private String secret;

    private Keycloak getInstance() {
        return KeycloakBuilder
                .builder()
                .serverUrl(SERVER_URL)
                .realm(REALM)
                .username(USERNAME)
                .password(PASSWORD)
                .clientId(CLIENT_ID)
                .clientSecret(secret)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }
    
    public Korisnik createAccount(KorisnikRequest korisnikRequest) throws BankingStatusException {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(korisnikRequest.getLozinka());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(korisnikRequest.getKorisnicko());
        user.setFirstName(korisnikRequest.getIme());
        user.setLastName(korisnikRequest.getPrezime());
        user.setEmail(korisnikRequest.getEmail());
        user.setCredentials(Arrays.asList(credential));
        user.setEnabled(true);
        javax.ws.rs.core.Response response = getInstance().realm(REALM).users().create(user);
        final int status = response.getStatus();
        if (status != HttpStatus.CREATED.value()) {
            throw new BankingStatusException(status, "Keycloak greška");
        }
        final String createdId = getCreatedId(response);
        // Reset password
        CredentialRepresentation newCredential = new CredentialRepresentation();
        UserResource userResource = getInstance().realm(REALM).users().get(createdId);
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(korisnikRequest.getLozinka());
        newCredential.setTemporary(false);
        userResource.resetPassword(newCredential);
        Korisnik korisnik = new Korisnik();
        korisnik.setUsername(korisnikRequest.getKorisnicko());
        korisnik.setKeycloakId(createdId);
        return korisnik;
        
    }
    
    private String getCreatedId(Response response) {
        URI location = response.getLocation();
        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            Response.StatusType statusInfo = response.getStatusInfo();
            throw new RuntimeException("Create method returned status " +
                    statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode() + "); expected status: Created (201)");
        }
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

}
