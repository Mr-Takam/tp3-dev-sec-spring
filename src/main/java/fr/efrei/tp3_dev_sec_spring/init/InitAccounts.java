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

    public InitAccounts(IUserAccountsRepository userRepository, 
                        ICompteBancaireRepository compteRepository, 
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.compteRepository = compteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Nettoyage des tables
        compteRepository.deleteAll();
        userRepository.deleteAll();

        // 1. Création des personnels de la banque (ADMIN)
        UserAccounts conseiller = new UserAccounts();
        conseiller.setLogin("m.leclerc");
        conseiller.setPassword(passwordEncoder.encode("Conseiller2024!"));
        conseiller.setRoles("ADMIN");
        userRepository.save(conseiller);

        // 2. Création des clients (CLIENT)
        UserAccounts client1 = new UserAccounts();
        client1.setLogin("j.dupont");
        client1.setPassword(passwordEncoder.encode("ClientSecure123*"));
        client1.setRoles("CLIENT");
        userRepository.save(client1);

        UserAccounts client2 = new UserAccounts();
        client2.setLogin("a.martin");
        client2.setPassword(passwordEncoder.encode("MartinPass789#"));
        client2.setRoles("CLIENT");
        userRepository.save(client2);

        // 3. Création des comptes bancaires associés
        // Compte de Jean Dupont (1500€ initial)
        CompteBancaire cpteDupont = new CompteBancaire();
        cpteDupont.setNumeroCompte("FR76-DUPONT-2024-001");
        cpteDupont.setSolde(1500.0);
        cpteDupont.setProprietaireLogin("j.dupont");
        compteRepository.save(cpteDupont);

        // Compte d'Alice Martin (450€ initial)
        CompteBancaire cpteMartin = new CompteBancaire();
        cpteMartin.setNumeroCompte("FR76-MARTIN-2024-002");
        cpteMartin.setSolde(450.0);
        cpteMartin.setProprietaireLogin("a.martin");
        compteRepository.save(cpteMartin);

        System.out.println(">>> Base de données initialisée avec des données professionnelles.");
        System.out.println(">>> Conseiller : m.leclerc | Clients : j.dupont, a.martin");
    }
}