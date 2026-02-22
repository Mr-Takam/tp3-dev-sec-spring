package fr.efrei.tp3_dev_sec_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"fr.efrei.tp3_dev_sec_spring", "fr.efrei.tp3_dev_sec_spring.api.rest"})
public class Tp3DevSecSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tp3DevSecSpringApplication.class, args);
	}

}