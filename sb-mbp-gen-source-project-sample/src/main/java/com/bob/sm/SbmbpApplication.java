package com.bob.sm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SbmbpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbmbpApplication.class, args);
	}

}

