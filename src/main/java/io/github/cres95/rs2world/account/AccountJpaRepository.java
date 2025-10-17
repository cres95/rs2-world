package io.github.cres95.rs2world.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT p FROM Account p WHERE p.loginName = :loginName")
    Optional<Account> findByLoginName(String loginName);

}
