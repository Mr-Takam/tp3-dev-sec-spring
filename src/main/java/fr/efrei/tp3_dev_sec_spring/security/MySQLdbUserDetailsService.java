package fr.efrei.tp3_dev_sec_spring.security;

import fr.efrei.tp3_dev_sec_spring.security.entities.UserAccounts;
import fr.efrei.tp3_dev_sec_spring.security.repository.IUserAccountsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MySQLdbUserDetailsService implements UserDetailsService {
    private final IUserAccountsRepository repository;

    public MySQLdbUserDetailsService(IUserAccountsRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccounts account = repository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(account.getLogin())
            .password(account.getPassword())
            .roles(account.getRoles().split(","))
            .build();
    }
}