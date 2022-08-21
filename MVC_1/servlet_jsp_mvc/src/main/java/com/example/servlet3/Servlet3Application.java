package com.example.servlet3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ServletComponentScan
@SpringBootApplication
public class Servlet3Application {

	public static void main(String[] args) {
		SpringApplication.run(Servlet3Application.class, args);
	}

	@Bean
	ViewResolver internalResourceViewResolver() {
		return new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
	}

}
