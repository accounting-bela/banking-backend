package hr.java.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "hr.java.banking")
@EntityScan("hr.java.banking")
public class BankingWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingWebApplication.class, args);
	}

}
