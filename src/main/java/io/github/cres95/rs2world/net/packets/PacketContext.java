package io.github.cres95.rs2world.net.packets;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PacketContext {

    private final SecureRandom secureRandom;

    public PacketContext() {
        this.secureRandom = new SecureRandom();
    }

    public long generateServerSessionKey() {
        return secureRandom.nextLong();
    }
}
