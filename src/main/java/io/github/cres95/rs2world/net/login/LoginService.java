package io.github.cres95.rs2world.net.login;

import io.github.cres95.rs2world.net.login.host.HostEvent;
import io.github.cres95.rs2world.net.login.host.HostService;
import io.github.cres95.rs2world.net.login.playerdetails.PlayerDetails;
import io.github.cres95.rs2world.net.login.playerdetails.PlayerDetailsService;
import io.github.cres95.rs2world.net.LoginDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private final SecureRandom sessionGen = new SecureRandom();
    private final ScheduledExecutorService executorService;
    private final PlayerDetailsService playerDetailsService;
    private final HostService hostService;

    public LoginService(HostService hostService, PlayerDetailsService playerDetailsService) {
        this.playerDetailsService = playerDetailsService;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.hostService = hostService;
        this.executorService.scheduleWithFixedDelay(hostService::prune, 5, 5, TimeUnit.MINUTES);
    }

    public long generateServerSessionKey() {
        return sessionGen.nextLong();
    }

    public LoginDecoder newLoginDecoder() {
        return new LoginDecoder(this);
    }

    public void registerLoginRequest(LoginRequest request) {
        executorService.submit(() -> processLoginRequest(request));
    }

    private void processLoginRequest(LoginRequest request) {
        try {
            LOGGER.info("Login request with username: {} and password: {}", request.username(), request.password());
            if (request.reconnecting()) {
                handleReconnection(request);
            } else {
                Optional<PlayerDetails> details = playerDetailsService.findByLoginName(request.username());
                details.ifPresentOrElse(pd -> handleExistingPlayer(pd, request), () -> handleNewPlayer(request));
            }
        } catch(LoginException e) {
            request.client().host().event(HostEvent.LOGIN_ATTEMPT);
            request.client().sendRaw(b -> b.writeByte(e.getResponseCode()));
            request.client().disconnect();
        }
    }

    private void handleReconnection(LoginRequest request) {

    }

    private void handleExistingPlayer(PlayerDetails details, LoginRequest request) {
        if (hostService.checkFor(request.client().host(), HostEvent.LOGIN_ATTEMPT)) {
            throw LoginException.forResponse(LoginResponseCode.LOGIN_ATTEMPTS_EXCEEDED);
        }
        if (!details.getPassword().equals(request.password())) {
            throw LoginException.forResponse(LoginResponseCode.INVALID_CREDENTIALS);
        }
        if (details.isBanned() || details.getBannedUntil() != null && details.getBannedUntil().isAfter(Instant.now())) {
            throw LoginException.forResponse(LoginResponseCode.ACCOUNT_DISABLED);
        }
    }

    private void handleNewPlayer(LoginRequest request) {
        if (hostService.checkFor(request.client().host(), HostEvent.ACCOUNT_CREATION)) {
            throw LoginException.forResponse(LoginResponseCode.LOGIN_ATTEMPTS_EXCEEDED);
        }
        request.client().host().event(HostEvent.ACCOUNT_CREATION);
    }
}
