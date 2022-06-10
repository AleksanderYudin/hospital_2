package com.example.hospital_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class Hospital2Application implements WebMvcConfigurer {

	public static ConcurrentHashMap<String, CompletableFuture<String>> futureMap = new ConcurrentHashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(Hospital2Application.class, args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder(8);
	}
}
