package io.github.cres95.rs2world.net.login.playerdetails;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class PlayerDetailsService {

    private final PlayerDetailsJpaRepository repository;

    public PlayerDetailsService(PlayerDetailsJpaRepository repository) {
        this.repository = repository;
    }

    public Optional<PlayerDetails> findByLoginName(String loginName) {
        return repository.findByLoginName(loginName);
    }
}
