package fr.efrei.tp3_dev_sec_spring.init;

import fr.efrei.tp3_dev_sec_spring.security.entities.UserAccounts;
import fr.efrei.tp3_dev_sec_spring.security.repository.IUserAccountsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component // Étape 42
public class InitAccounts implements CommandLineRunner {

    private final IUserAccountsRepository repository;
    private final PasswordEncoder passwordEncoder;

    // Injection par constructeur
    public InitAccounts(IUserAccountsRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // On nettoie la base au démarrage (optionnel pour les tests)
        repository.deleteAll();

        // Création du compte USER
        UserAccounts user = new UserAccounts();
        user.setLogin("toto");
        user.setPassword(passwordEncoder.encode("12345")); // Hachage dynamique !
        user.setRoles("USER");
        repository.save(user);

        // Création du compte ADMIN
        UserAccounts admin = new UserAccounts();
        admin.setLogin("tintin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles("ADMIN");
        repository.save(admin);

        System.out.println(">>> Base de données initialisée avec toto (USER) et tintin (ADMIN)");
    }
}