package fr.efrei.tp3_dev_sec_spring.security;

// Importation du repository et des annotations Spring Data
import fr.efrei.tp3_dev_sec_spring.security.repository.IUserAccountsRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Importation des composants de sécurité
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EntityScan("fr.efrei.tp3_dev_sec_spring.security.entities") // Étape 37
@EnableJpaRepositories("fr.efrei.tp3_dev_sec_spring.security.repository") // Étape 39
public class SecuConfig {

    @Bean // Étape 34
    public UserDetailsService userDetailsService(IUserAccountsRepository repository) {
        return new MySQLdbUserDetailsService(repository);
    }

    @Bean // Étape 33
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean // Étape 32
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/public").permitAll()
                
                // On autorise l'accès HTTP aux salaires pour tout utilisateur authentifié
                // La sécurité fine (qui voit quoi) sera gérée par @PreAuthorize dans le service
                .requestMatchers("/protege/salaire/**").authenticated() 

                .requestMatchers("/protege/user").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/protege/admin").hasRole("ADMIN")
                
                // Optionnel mais recommandé : autoriser la page d'erreur pour voir les messages
                .requestMatchers("/error").permitAll() 

                .anyRequest().denyAll() 
            )
            // Étape 23 : Ajout des filtres d'authentification
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}