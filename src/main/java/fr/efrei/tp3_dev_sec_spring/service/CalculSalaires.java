package fr.efrei.tp3_dev_sec_spring.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalculSalaires {

    private final Map<String, Double> salaires = new HashMap<>();

    public CalculSalaires() {
        salaires.put("toto", 2500.0);
        salaires.put("tintin", 4500.0);
        salaires.put("pdg", 15000.0); 
    }

    // On utilise #nom car c'est le nom du paramètre ci-dessous
    @PreAuthorize("hasRole('ADMIN') or #nom == authentication.name")
    @PostAuthorize("hasRole('USER') or (hasRole('ADMIN') and returnObject <= 10000)")
    public double getSalaire(String nom) {
        if (!salaires.containsKey(nom)) {
            throw new IllegalArgumentException("Employé inconnu : " + nom);
        }
        return salaires.get(nom);
    }
}