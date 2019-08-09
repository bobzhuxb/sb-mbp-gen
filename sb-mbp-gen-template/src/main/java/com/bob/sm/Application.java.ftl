package ${packageName};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TorontoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TorontoApplication.class, args);
	}

}

