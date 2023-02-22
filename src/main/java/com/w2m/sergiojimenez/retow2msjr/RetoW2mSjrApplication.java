package com.w2m.sergiojimenez.retow2msjr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RetoW2mSjrApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(RetoW2mSjrApplication.class, args);
	}

}
