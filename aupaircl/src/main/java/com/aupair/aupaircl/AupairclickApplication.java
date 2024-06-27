package com.aupair.aupaircl;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.net.URI;
@Slf4j
@SpringBootApplication
public class AupairclickApplication {

	public static void main(String[] args) {
		SpringApplication.run(AupairclickApplication.class, args);
		openSwagger();
	}
	@Bean
	public OpenAPI customApi(){

		return new OpenAPI().info(new Info().title("Au Pair Click Apis").version("0.11").
				description("Apis de desarrollo au pair click").termsOfService("http://swagger.io/terms/").
				license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
	private static void openSwagger() {
		try {
			URI swaggerUri = new URI("http://localhost:8080/doc/swagger-ui/index.html");

			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(swaggerUri);
				log.info(""+swaggerUri);
			} else {
				log.info(""+swaggerUri);

			}
		} catch (Exception e) {
			log.error("Error "+e.getMessage());
		}
	}
}
