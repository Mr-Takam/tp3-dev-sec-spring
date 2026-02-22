package fr.efrei.tp3_dev_sec_spring.api.rest;

import fr.efrei.tp3_dev_sec_spring.banque.entities.CompteBancaire;
import fr.efrei.tp3_dev_sec_spring.service.BanqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestSimple {

    @Autowired
    private BanqueService banqueService;

    @GetMapping("/public")
    public String ressourcePublique() {
        return "ressources publique accessible à tous";
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

    // AJOUT ÉTAPE SUIVANTE : Endpoint de création
    @PostMapping("/banque/creer")
    public CompteBancaire creerCompte(@RequestParam String id, @RequestParam String login) {
        return banqueService.creerCompte(id, login);
    }
}