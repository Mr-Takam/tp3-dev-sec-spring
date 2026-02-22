package fr.efrei.tp3_dev_sec_spring.security.repository;

import fr.efrei.tp3_dev_sec_spring.security.entities.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserAccountsRepository extends JpaRepository<UserAccounts, String> {
}