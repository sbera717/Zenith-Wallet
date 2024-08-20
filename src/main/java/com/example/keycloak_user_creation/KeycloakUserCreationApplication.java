package com.example.keycloak_user_creation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class KeycloakUserCreationApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakUserCreationApplication.class, args);
	}

}
