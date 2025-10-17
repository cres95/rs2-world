package io.github.cres95.rs2world.account;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class AccountService {

    private final AccountJpaRepository repository;

    public AccountService(AccountJpaRepository repository) {
        this.repository = repository;
    }

    public Optional<Account> findByLoginName(String loginName) {
        return repository.findByLoginName(loginName);
    }
}
