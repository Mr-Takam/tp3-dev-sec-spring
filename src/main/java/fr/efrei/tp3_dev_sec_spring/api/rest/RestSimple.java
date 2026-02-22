package fr.efrei.tp3_dev_sec_spring.api.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestSimple {

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
}