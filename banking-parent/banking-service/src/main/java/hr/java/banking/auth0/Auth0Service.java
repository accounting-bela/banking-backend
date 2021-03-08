package hr.java.banking.auth0;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Auth0Service {
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;
	
	@Value("${auth0.managment.token}")
	private String token;
	
	private HttpHeaders headers;
	
	private RestTemplate restTemplate;

	@Autowired
	public Auth0Service(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
		this.headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		this.headers.set("Authorization", "Bearer " + token);
	}
	
	
	
	

}
