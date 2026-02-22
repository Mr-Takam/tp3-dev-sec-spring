package fr.efrei.tp3_dev_sec_spring.banque.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CompteBancaire {
    @Id
    private String numeroCompte;
    private double solde;
    private String proprietaireLogin; // Lien avec le login de l'UserAccounts

    // Getters et Setters indispensables pour JPA
    public String getNumeroCompte() { return numeroCompte; }
    public void setNumeroCompte(String numeroCompte) { this.numeroCompte = numeroCompte; }

    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }

    public String getProprietaireLogin() { return proprietaireLogin; }
    public void setProprietaireLogin(String proprietaireLogin) { this.proprietaireLogin = proprietaireLogin; }
}