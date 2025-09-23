package io.github.cres95.rs2world.net.login;

import io.github.cres95.rs2world.net.Client;

public record LoginRequest(Client client,
                           int sessionId,
                           long serverSessionKey,
                           long clientSessionKey,
                           String username,
                           String password,
                           boolean reconnecting) {
}
