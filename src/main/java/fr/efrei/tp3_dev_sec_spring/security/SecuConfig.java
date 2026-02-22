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
@EntityScan(basePackages = {
    "fr.efrei.tp3_dev_sec_spring.security.entities",
    "fr.efrei.tp3_dev_sec_spring.banque.entities"
})
@EnableJpaRepositories(basePackages = {
    "fr.efrei.tp3_dev_sec_spring.security.repository",
    "fr.efrei.tp3_dev_sec_spring.banque.repository"
})
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
            // Désactive la protection CSRF pour permettre les requêtes POST via curl/Postman
            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/public").permitAll()
                .requestMatchers("/error").permitAll()
                
                // Accès autorisé à tout utilisateur authentifié
                // Les règles fines (1000€, propriété) sont dans BanqueService
                .requestMatchers("/banque/**").authenticated() 

                .requestMatchers("/protege/user").hasAnyRole("USER", "ADMIN", "CLIENT")
                .requestMatchers("/protege/admin").hasRole("ADMIN")
                
                .anyRequest().denyAll() 
            )
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}