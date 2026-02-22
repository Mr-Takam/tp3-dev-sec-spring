package fr.efrei.tp3_dev_sec_spring.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
                // Étape 21 : Public reste public
                .requestMatchers("/public").permitAll()
                
                // Étape 22 : Restriction par rôles
                // /protege/user accessible par USER ou ADMIN
                .requestMatchers("/protege/user").hasAnyRole("USER", "ADMIN")
                
                // /protege/admin accessible uniquement par ADMIN
                .requestMatchers("/protege/admin").hasRole("ADMIN")
                
                // Toute autre ressource est inaccessible (étape 22)
                .anyRequest().denyAll() 
            )
            // Étape 23 : Ajout des filtres d'authentification
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        // Création de l'utilisateur Toto avec le rôle USER
        UserDetails user1 = User.withUsername("toto")
                .password("{noop}12345") // {noop} car pas encore d'encodeur (étape 27)
                .roles("USER")
                .build();

        // Création de l'utilisateur Tintin avec le rôle ADMIN
        UserDetails user2 = User.withUsername("tintin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}