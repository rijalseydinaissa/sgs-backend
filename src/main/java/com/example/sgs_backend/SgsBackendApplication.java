package com.example.sgs_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SgsBackendApplication {

	public static void main(String[] args) {
		var encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
		System.out.println(encoder.encode("Admin@1234"));
		SpringApplication.run(SgsBackendApplication.class, args);
	}

}
