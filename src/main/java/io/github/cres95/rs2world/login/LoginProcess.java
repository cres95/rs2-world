package io.github.cres95.rs2world.login;

import io.github.cres95.rs2world.net.ClientConnectedEvent;
import io.github.cres95.rs2world.util.Process;
import io.github.cres95.rs2world.net.Client;
import io.github.cres95.rs2world.net.packets.PacketContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class LoginProcess implements Process {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginProcess.class);

    private final PacketContext packetContext;
    private final BlockingQueue<Client> clients;

    public LoginProcess(PacketContext packetContext) {
        this.packetContext = packetContext;
        this.clients = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        List<Client> currentClients = new ArrayList<>(clients.size());
        clients.drainTo(currentClients);
        for (Client c : currentClients) {
            try {
                c.processQueuedPackets(packetContext);
            } catch (LoginException le) {
                c.send(b -> b.put((byte) le.getResponseCode()));
                c.disconnect();
            }
            if (!c.isDisconnected()) {
                clients.offer(c);
            }
        }
        currentClients.clear();
    }

    @EventListener
    public void handle(ClientConnectedEvent event) {
        clients.offer(event.getClient());
    }

    @Override
    public long cycleRate() {
        return 1000L;
    }
}
