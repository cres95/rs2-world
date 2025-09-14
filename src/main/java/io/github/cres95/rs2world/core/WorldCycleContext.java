package io.github.cres95.rs2world.core;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class WorldCycleContext {

    private final SecureRandom secureRandom = new SecureRandom();

    public long generateServerSessionKey() {
        return secureRandom.nextLong();
    }

}
