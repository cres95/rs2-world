package io.github.cres95.rs2world.net.login.playerdetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PlayerDetailsJpaRepository extends JpaRepository<PlayerDetails, UUID> {

    @Query("SELECT p FROM PlayerDetails p WHERE p.loginName = :loginName")
    Optional<PlayerDetails> findByLoginName(String loginName);

}
