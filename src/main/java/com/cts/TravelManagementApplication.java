package com.cts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication

@OpenAPIDefinition(
	    info = @Info(
	        title = "Travel-Management Application",
	        version = "1.0",
	        description = "API documentation for my secured Spring Boot application"
	    ),
	    security = @SecurityRequirement(name = "BearerAuth") // Apply BearerAuth globally
	)
	@SecurityScheme(
	    name = "BearerAuth", // Name of the security scheme
	    type = SecuritySchemeType.HTTP,
	    scheme = "bearer",
	    bearerFormat = "JWT", // Optional, for documentation purposes
	    description = "JWT authentication using a Bearer token"
	)
public class TravelManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelManagementApplication.class, args);
	}

}
