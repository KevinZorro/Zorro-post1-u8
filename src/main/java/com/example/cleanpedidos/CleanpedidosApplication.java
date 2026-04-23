package com.example.cleanpedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.cleanpedidos")
public class CleanpedidosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanpedidosApplication.class, args);
	}

}
