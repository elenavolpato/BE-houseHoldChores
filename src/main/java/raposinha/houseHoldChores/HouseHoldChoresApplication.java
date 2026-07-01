package raposinha.houseHoldChores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HouseHoldChoresApplication {
	public static void main(String[] args) {
		SpringApplication.run(HouseHoldChoresApplication.class, args);

	}
}
