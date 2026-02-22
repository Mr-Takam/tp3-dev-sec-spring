package fr.efrei.tp3_dev_sec_spring.api.rest;

import fr.efrei.tp3_dev_sec_spring.service.CalculSalaires; // N'oublie pas l'import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestSimple {

    @Autowired
    private CalculSalaires calculSalaires; // Étape 47 : Injection du service

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

    // Étape 47 : Nouvelle ressource pour le salaire
    @GetMapping("/protege/salaire/{nom}")
    public double ressourceSalaire(@PathVariable String nom) {
        return calculSalaires.getSalaire(nom);
    }
}