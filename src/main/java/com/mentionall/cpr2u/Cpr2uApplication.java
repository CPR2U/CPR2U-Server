package com.mentionall.cpr2u;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Cpr2uApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cpr2uApplication.class, args);
	}

}
