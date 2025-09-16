package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.net.packets.PacketDecoder;
import io.github.cres95.rs2world.net.util.BufferLease;
import io.github.cres95.rs2world.util.Process;
import io.github.cres95.rs2world.util.SystemTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Component
public class ServerProcess implements Process {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerProcess.class);

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final ExecutorService workers;
    private final Supplier<BufferLease> workerBufferLeaseSupplier;
    private final BiFunction<SelectionKey, SocketChannel, Client> clientGenerator;
    private final long batchAcceptDelay;
    private final int batchAcceptAttempts;
    private final ApplicationEventPublisher eventPublisher;
    private final SystemTimer batchAcceptTimer = new SystemTimer();

    public ServerProcess(Selector selector,
                         ServerSocketChannel serverSocketChannel,
                         ExecutorService workers,
                         Supplier<BufferLease> workerBufferLeaseSupplier,
                         BiFunction<SelectionKey, SocketChannel, Client> clientGenerator,
                         ServerProperties properties,
                         ApplicationEventPublisher eventPublisher) {
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
        this.workers = workers;
        this.workerBufferLeaseSupplier = workerBufferLeaseSupplier;
        this.clientGenerator = clientGenerator;
        this.batchAcceptDelay = properties.getBatchAcceptDelay();
        this.batchAcceptAttempts = properties.getBatchAcceptAttempts();
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void run() {
        try {
            selector.selectNow();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable() && batchAcceptTimer.resetOnElapsed(batchAcceptDelay)) {
                    workers.submit(this::batchAccept);
                }
                if (key.isReadable()) {
                    workers.submit(() -> read(key));
                }
                iterator.remove();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long cycleRate() {
        return 100L;
    }

    private void batchAccept() {
        for (int i = 0; i < batchAcceptAttempts; i++) {
            SelectionKey key = null;
            SocketChannel channel = null;
            try {
                channel = serverSocketChannel.accept();
                if (channel == null) return;

                //TODO: host validations

                channel.configureBlocking(false);
                key = channel.register(selector, SelectionKey.OP_READ);
                Client client = clientGenerator.apply(key, channel);
                key.attach(client);
                eventPublisher.publishEvent(new ClientConnectedEvent(this, client));
            } catch (IOException ioe) {
                LOGGER.error("Critical error during accept cycle; ", ioe);
                closeConnection(key, channel);
            }
        }
    }

    private void closeConnection(SelectionKey key, SocketChannel channel) {
        try {
            if (key != null) key.cancel();
            if (channel != null) channel.close();
        } catch(IOException ioe) {
            LOGGER.error("Error during connection closure; ", ioe);
        }
    }

    private void read(SelectionKey key) {
        Client client = (Client) key.attachment();
        ByteBuffer buffer = workerBufferLeaseSupplier.get().buffer();
        client.decode(buffer);
    }

}
