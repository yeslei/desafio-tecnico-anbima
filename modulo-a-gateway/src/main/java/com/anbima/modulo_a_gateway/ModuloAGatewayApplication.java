package com.anbima.modulo_a_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ModuloAGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModuloAGatewayApplication.class, args);
	}

}
