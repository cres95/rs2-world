package io.github.cres95.rs2world.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
public class Account {

    @Id
    private UUID uuid;
    private String loginName;
    private String displayName;
    private String password;
    private int rank;
    private Instant bannedUntil;
    private boolean banned;

    public Account() {
    }

    public Account(String loginName, String password) {
        this.uuid = UUID.randomUUID();
        this.loginName = loginName;
        this.displayName = loginName;
        this.password = password;
        this.rank = 0;
        this.bannedUntil = null;
        this.banned = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Instant getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(Instant bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
