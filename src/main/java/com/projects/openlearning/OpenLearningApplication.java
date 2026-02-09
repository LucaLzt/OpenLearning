package com.projects.openlearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OpenLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenLearningApplication.class, args);
	}

}
