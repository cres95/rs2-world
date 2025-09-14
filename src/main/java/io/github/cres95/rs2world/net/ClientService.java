package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.core.WorldCycleAware;
import io.github.cres95.rs2world.core.WorldCycleContext;
import io.github.cres95.rs2world.net.login.LoginException;
import io.github.cres95.rs2world.net.login.LoginResponseCode;
import io.github.cres95.rs2world.net.packets.PacketDecoder;
import io.github.cres95.rs2world.net.util.BufferLease;
import io.github.cres95.rs2world.net.util.BufferPool;
import io.github.cres95.rs2world.util.ArrayWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@Component
public class ClientService implements WorldCycleAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    private final ArrayWrapper<Client> clients;
    private final BufferPool bufferPool;

    public ClientService(ServerProperties properties, BufferPool bufferPool) {
        this.bufferPool = bufferPool;
        this.clients = ArrayWrapper.wrapConcurrent(new Client[properties.getCapacity()]);
    }

    public Client register(SocketChannel channel, SelectionKey key) {
        int emptySlot = clients.findEmpty();
        if (emptySlot == -1) {
            throw LoginException.forResponse(LoginResponseCode.WORLD_FULL);
        }
        BufferLease bufferLease = bufferPool.lease();
        if (bufferLease == null) {
            throw LoginException.forResponse(LoginResponseCode.WORLD_FULL);
        }
        Client client = new Client(
                emptySlot,
                bufferLease,
                key,
                channel,
                PacketDecoder.STAGE_1_LOGIN_PACKET_DECODER);
        clients.set(emptySlot, client);
        return client;
    }

    @Override
    public void cycle(WorldCycleContext ctx) {
        clients.forEach(c -> c.cycle(ctx));
    }
}
