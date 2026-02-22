package fr.efrei.tp3_dev_sec_spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecuConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // La ressource /public est accessible à tous (étape 21)
                .requestMatchers("/public").permitAll()
                // La ressource /protege/** nécessite une authentification (étape 21)
                .requestMatchers("/protege/**").authenticated()
                // Par défaut, on demande l'authentification pour le reste (sécurité max)
                .anyRequest().authenticated()
            )
            // Activation du formulaire de login (étape 13) et Basic Auth (étape 14)
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}