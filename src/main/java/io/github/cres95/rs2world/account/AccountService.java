package io.github.cres95.rs2world.account;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class AccountService {

    private final AccountJpaRepository repository;

    public AccountService(AccountJpaRepository repository) {
        this.repository = repository;
    }

    public Account getByLoginName(String loginName) {
        return repository.findByLoginName(loginName).orElse(null);
    }

    @Transactional
    public Account create(String username, String password) {
        return repository.save(new Account(username, password));
    }
}
