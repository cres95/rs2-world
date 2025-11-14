package io.github.cres95.rs2world.net.login;

import io.github.cres95.rs2world.account.Account;
import io.github.cres95.rs2world.account.AccountService;
import io.github.cres95.rs2world.net.login.host.HostEvent;
import io.github.cres95.rs2world.net.login.host.HostService;
import io.github.cres95.rs2world.net.packets.inbound.InboundPacketBuilderDemux;
import io.github.cres95.rs2world.net.packets.inbound.InboundPacketDecoder;
import io.github.cres95.rs2world.net.packets.outbound.OutboundPacketChannel;
import io.github.cres95.rs2world.net.packets.outbound.OutboundPacketChannelFactory;
import io.github.cres95.rs2world.net.util.ISAACCipher;
import io.github.cres95.rs2world.player.Player;
import io.github.cres95.rs2world.player.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private final SecureRandom sessionGen = new SecureRandom();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ScheduledExecutorService executorService;
    private final AccountService accountService;
    private final HostService hostService;
    private final PlayerService playerService;
    private final InboundPacketBuilderDemux inboundPacketBuilderDemux;
    private final OutboundPacketChannelFactory outboundPacketChannelFactory;

    public LoginService(HostService hostService,
                        AccountService accountService,
                        PlayerService playerService,
                        InboundPacketBuilderDemux inboundPacketBuilderDemux,
                        OutboundPacketChannelFactory outboundPacketChannelFactory) {
        this.accountService = accountService;
        this.playerService = playerService;
        this.inboundPacketBuilderDemux = inboundPacketBuilderDemux;
        this.outboundPacketChannelFactory = outboundPacketChannelFactory;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.hostService = hostService;
        this.executorService.scheduleWithFixedDelay(hostService::prune, 5, 5, TimeUnit.MINUTES);
    }

    public long generateServerSessionKey() {
        return sessionGen.nextLong();
    }

    @EventListener
    public void handleLoginRequestEvent(LoginRequestEvent event) {
        executorService.submit(() -> handleLoginRequest(event.getRequest()));
    }

    private void handleLoginRequest(LoginRequest request) {
        try {
            Account account = authenticate(request);
            Player player = register(request, account);
        } catch(LoginException e) {
            request.client().host().event(HostEvent.LOGIN_ATTEMPT);
            request.client().send(b -> b.writeByte(e.getResponseCode()));
            request.client().disconnect();
        } catch(Exception e) {
            request.client().disconnect();
        }
    }

    private Account authenticate(LoginRequest request) {
        hostService.validateFor(request.client().host(), HostEvent.LOGIN_ATTEMPT);
        Account account = accountService.getByLoginName(request.username());
        if (account != null) {
            if (!passwordEncoder.matches(request.password(), account.getPassword())) {
                throw LoginException.forResponse(LoginResponseCode.INVALID_CREDENTIALS);
            }
            if (account.isBanned() || (account.getBannedUntil() != null && account.getBannedUntil().isAfter(Instant.now()))) {
                throw LoginException.forResponse(LoginResponseCode.ACCOUNT_DISABLED);
            }
        } else {
            hostService.validateFor(request.client().host(), HostEvent.ACCOUNT_CREATION);
            request.client().host().event(HostEvent.ACCOUNT_CREATION);
            String encryptedPassword = passwordEncoder.encode(request.password());
            account = accountService.create(request.username(), encryptedPassword);
        }
        return account;
    }

    private Player register(LoginRequest request, Account account) {
        int[] seed = {
                (int) (request.clientSessionKey() >> 32),
                (int) request.clientSessionKey(),
                (int) (request.serverSessionKey() >> 32),
                (int) request.serverSessionKey()};
        ISAACCipher decryptor = ISAACCipher.asDecryptor(seed);
        ISAACCipher encryptor = ISAACCipher.asEncryptor(seed);
        OutboundPacketChannel outboundPacketChannel = outboundPacketChannelFactory.create(request.client(), encryptor);
        Player player = playerService.register(
                init -> init.withUsername(account.getDisplayName())
                        .withRank(account.getRank())
                        .withOutboundPacketChannel(outboundPacketChannel));
        InboundPacketDecoder decoder = new InboundPacketDecoder(inboundPacketBuilderDemux, player.inboundPacketQueue(), decryptor);
        request.client().channel().pipeline().remove("decoder");
        request.client().channel().pipeline().addLast(decoder);
        return player;
    }
}
