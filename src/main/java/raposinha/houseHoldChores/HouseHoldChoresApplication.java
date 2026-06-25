package raposinha.houseHoldChores;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HouseHoldChoresApplication {
	@Value("${app.version}")
	static String appVersion;

	public static void main(String[] args) {
		SpringApplication.run(HouseHoldChoresApplication.class, args);
		System.out.println(appVersion);

	}
}
