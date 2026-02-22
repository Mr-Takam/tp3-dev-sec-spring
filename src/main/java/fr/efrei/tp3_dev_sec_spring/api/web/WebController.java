package fr.efrei.tp3_dev_sec_spring.api.web;

import fr.efrei.tp3_dev_sec_spring.banque.entities.CompteBancaire;
import fr.efrei.tp3_dev_sec_spring.banque.repository.ICompteBancaireRepository;
import fr.efrei.tp3_dev_sec_spring.service.BanqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private BanqueService banqueService;

    @Autowired
    private ICompteBancaireRepository compteRepository;

    @GetMapping("/banque/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Ajout du nom d'utilisateur pour l'affichage
        model.addAttribute("username", authentication.getName());
        
        // Récupération des comptes selon le rôle
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Un ADMIN voit tous les comptes du système
            model.addAttribute("comptes", compteRepository.findAll());
            model.addAttribute("isAdmin", true);
        } else {
            // Un CLIENT voit uniquement ses comptes
            List<CompteBancaire> mesComptes = compteRepository.findByProprietaireLogin(authentication.getName());
            model.addAttribute("comptes", mesComptes);
            model.addAttribute("isAdmin", false);
        }
        
        return "dashboard"; // Cherchera src/main/resources/templates/dashboard.html
    }
}