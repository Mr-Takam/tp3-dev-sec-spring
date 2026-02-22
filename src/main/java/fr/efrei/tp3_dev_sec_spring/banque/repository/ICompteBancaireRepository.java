package fr.efrei.tp3_dev_sec_spring.banque.repository;

import fr.efrei.tp3_dev_sec_spring.banque.entities.CompteBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompteBancaireRepository extends JpaRepository<CompteBancaire, String> {
    List<CompteBancaire> findByProprietaireLogin(String login);
}