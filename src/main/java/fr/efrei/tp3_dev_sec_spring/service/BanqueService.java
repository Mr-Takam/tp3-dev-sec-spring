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

    @PostAuthorize("hasRole('ADMIN') or returnObject.proprietaireLogin == authentication.name")
    public CompteBancaire consulterCompte(String numero) {
        return compteRepository.findById(numero)
                .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
    }

    @PreAuthorize("(hasRole('CLIENT') and T(java.lang.Math).abs(#montant) <= 1000 and @banqueService.isOwner(#numeroCompte)) " +
                  "or (hasRole('ADMIN') and T(java.lang.Math).abs(#montant) > 1000)")
    public void faireOperation(String numeroCompte, double montant) {
        CompteBancaire cb = consulterCompte(numeroCompte);
        cb.setSolde(cb.getSolde() + montant);
        compteRepository.save(cb);
    }

    public boolean isOwner(String numeroCompte) {
        CompteBancaire cb = compteRepository.findById(numeroCompte).orElse(null);
        return cb != null && cb.getProprietaireLogin().equals(
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }
}