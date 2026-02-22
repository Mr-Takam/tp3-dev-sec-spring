package fr.efrei.tp3_dev_sec_spring.init;

import fr.efrei.tp3_dev_sec_spring.banque.entities.CompteBancaire;
import fr.efrei.tp3_dev_sec_spring.banque.repository.ICompteBancaireRepository;
import fr.efrei.tp3_dev_sec_spring.security.entities.UserAccounts;
import fr.efrei.tp3_dev_sec_spring.security.repository.IUserAccountsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitAccounts implements CommandLineRunner {

    private final IUserAccountsRepository userRepository;
    private final ICompteBancaireRepository compteRepository;
    private final PasswordEncoder passwordEncoder;

    // L'injection se fait automatiquement par le constructeur
    public InitAccounts(IUserAccountsRepository userRepository, 
                        ICompteBancaireRepository compteRepository, 
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.compteRepository = compteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Nettoyage des tables (ordre important à cause des liens logiques)
        compteRepository.deleteAll();
        userRepository.deleteAll();

        // 2. Création des utilisateurs (Rôles adaptés au projet : CLIENT et ADMIN)
        UserAccounts client = new UserAccounts();
        client.setLogin("toto");
        client.setPassword(passwordEncoder.encode("12345"));
        client.setRoles("CLIENT"); // Changé de USER à CLIENT pour le projet
        userRepository.save(client);

        UserAccounts conseiller = new UserAccounts();
        conseiller.setLogin("tintin");
        conseiller.setPassword(passwordEncoder.encode("admin123"));
        conseiller.setRoles("ADMIN");
        userRepository.save(conseiller);

        // 3. Création de comptes bancaires de test
        CompteBancaire c1 = new CompteBancaire();
        c1.setNumeroCompte("CB-TOTO-01");
        c1.setSolde(500.0);
        c1.setProprietaireLogin("toto");
        compteRepository.save(c1);

        CompteBancaire c2 = new CompteBancaire();
        c2.setNumeroCompte("CB-TINTIN-01");
        c2.setSolde(10000.0);
        c2.setProprietaireLogin("tintin");
        compteRepository.save(c2);

        System.out.println(">>> Base de données initialisée :");
        System.out.println(">>> Utilisateurs : toto (CLIENT), tintin (ADMIN)");
        System.out.println(">>> Comptes créés : CB-TOTO-01 (500€), CB-TINTIN-01 (10000€)");
    }
}