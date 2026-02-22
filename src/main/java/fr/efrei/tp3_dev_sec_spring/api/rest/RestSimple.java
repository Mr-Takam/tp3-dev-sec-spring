package fr.efrei.tp3_dev_sec_spring.api.rest;

import fr.efrei.tp3_dev_sec_spring.banque.entities.CompteBancaire; // Import crucial
import fr.efrei.tp3_dev_sec_spring.service.BanqueService;
import fr.efrei.tp3_dev_sec_spring.service.CalculSalaires;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestSimple {

    @Autowired
    private CalculSalaires calculSalaires;

    @Autowired
    private BanqueService banqueService;

    @GetMapping("/public")
    public String ressourcePublique() {
        return "ressources publique accessible à tous";
    }

    @GetMapping("/protege/user")
    public String ressourceUtilisateurs() {
        return "page privée réservée aux utilisateurs";
    }

    @GetMapping("/protege/admin")
    public String ressourceAdministrateurs() {
        return "page privée réservée aux administrateurs";
    }

    @GetMapping("/protege/salaire/{nom}")
    public double ressourceSalaire(@PathVariable String nom) {
        return calculSalaires.getSalaire(nom);
    }

    // --- Endpoints pour la Banque Simplifiée ---

    @GetMapping("/banque/consulter/{id}")
    public CompteBancaire consulter(@PathVariable String id) {
        return banqueService.consulterCompte(id);
    }

    @PostMapping("/banque/operation")
    public String faireOperation(@RequestParam String id, @RequestParam double montant) {
        banqueService.faireOperation(id, montant);
        return "Opération effectuée. Nouveau solde : " + banqueService.consulterCompte(id).getSolde();
    }
}