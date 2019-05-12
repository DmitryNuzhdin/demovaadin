package com.example.demovaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemovaadinApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx =
		SpringApplication.run(DemovaadinApplication.class, args);
	}

}
