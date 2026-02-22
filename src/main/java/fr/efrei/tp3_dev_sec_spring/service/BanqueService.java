package fr.efrei.tp3_dev_sec_spring.service;

import fr.efrei.tp3_dev_sec_spring.banque.entities.CompteBancaire;
import fr.efrei.tp3_dev_sec_spring.banque.repository.ICompteBancaireRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class BanqueService {

    private final ICompteBancaireRepository compteRepository;

    public BanqueService(ICompteBancaireRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    /**
     * Un ADMIN peut consulter n'importe quel compte.
     * Un CLIENT ne peut consulter que son propre compte.
     */
    @PostAuthorize("hasRole('ADMIN') or returnObject.proprietaireLogin == authentication.name")
    public CompteBancaire consulterCompte(String numero) {
        return compteRepository.findById(numero)
                .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
    }

    /**
     * Règle des opérations (Débit/Crédit) :
     * - CLIENT : Doit être propriétaire ET montant valeur absolue <= 1000€
     * - ADMIN  : Montant valeur absolue doit être > 1000€
     */
    @PreAuthorize("(hasRole('CLIENT') and T(java.lang.Math).abs(#montant) <= 1000 and @banqueService.isOwner(#numeroCompte)) " +
                  "or (hasRole('ADMIN') and T(java.lang.Math).abs(#montant) > 1000)")
    public void faireOperation(String numeroCompte, double montant) {
        CompteBancaire cb = consulterCompte(numeroCompte);
        cb.setSolde(cb.getSolde() + montant);
        compteRepository.save(cb);
    }

    /**
     * Création de compte :
     * Seul un CONSEILLER BANCAIRE (ADMIN) peut créer des comptes pour les clients.
     */
    @PreAuthorize("hasRole('ADMIN')")
    public CompteBancaire creerCompte(String numero, String proprietaireLogin) {
        if (compteRepository.existsById(numero)) {
            throw new IllegalArgumentException("Le compte " + numero + " existe déjà.");
        }
        
        CompteBancaire nouveauCompte = new CompteBancaire();
        nouveauCompte.setNumeroCompte(numero);
        nouveauCompte.setProprietaireLogin(proprietaireLogin);
        nouveauCompte.setSolde(0.0);
        
        return compteRepository.save(nouveauCompte);
    }

    /**
     * Méthode utilitaire pour vérifier si l'utilisateur connecté est le propriétaire du compte.
     */
    public boolean isOwner(String numeroCompte) {
        CompteBancaire cb = compteRepository.findById(numeroCompte).orElse(null);
        return cb != null && cb.getProprietaireLogin().equals(
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }
}