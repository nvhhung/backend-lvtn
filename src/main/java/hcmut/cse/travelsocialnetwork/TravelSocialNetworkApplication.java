package hcmut.cse.travelsocialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"hcmut.cse.travelsocialnetwork.*"})
public class TravelSocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelSocialNetworkApplication.class, args);
	}

}
