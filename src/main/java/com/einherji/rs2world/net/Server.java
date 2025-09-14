package com.einherji.rs2world.net;

import com.einherji.rs2world.net.clients.Client;
import com.einherji.rs2world.net.clients.ClientService;
import com.einherji.rs2world.net.login.LoginException;
import com.einherji.rs2world.net.login.LoginResponseCodes;
import com.einherji.rs2world.net.login.LoginService;
import com.einherji.rs2world.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public final class Server implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private static final int PORT = 43594;
    private static final int WORKERS = 3;
    private static final int BUFFER_SIZE = 2048;
    private static final int ACCEPT_ATTEMPTS = 5;
    private static final long ACCEPT_DELAY = 10000;

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final ExecutorService workers;
    private final ThreadLocal<ByteBuffer> workerBuffers;
    private final ClientService clientService;
    private final LoginService loginService;
    private final Timer acceptTimer = new Timer();

    public Server(ClientService clientService, LoginService loginService) {
        this.clientService = clientService;
        this.loginService = loginService;
        workers = Executors.newFixedThreadPool(WORKERS);
        workerBuffers = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(BUFFER_SIZE));
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            selector.selectNow();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                if (key.isAcceptable() && acceptTimer.elapsed(ACCEPT_DELAY)) {
                    acceptTimer.reset();
                    workers.submit(this::acceptConnections);
                }
                if (key.isReadable()) {
                    //workers.submit(() -> read(key));
                }
                keys.remove();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptConnections() {
        ByteBuffer threadBuffer = workerBuffers.get();
        boolean worldFull = false;
        for (int i = 0; i < ACCEPT_ATTEMPTS; i++) {
            SocketChannel channel = null;
            try {
                channel = serverSocketChannel.accept();
                if (channel == null) return;

                if (worldFull) {
                    handleException(channel, threadBuffer, LoginResponseCodes.WORLD_FULL);
                    continue;
                }

                channel.configureBlocking(false);
                UUID uuid = UUID.randomUUID();
                SelectionKey key = channel.register(selector, SelectionKey.OP_READ, uuid);

                Client client = clientService.create(key, channel, uuid);
                loginService.add(client);
            } catch (IOException ioe) {
                LOGGER.error("Encountered error during accept cycle: ", ioe);
            } catch(LoginException le) {
                handleException(channel, threadBuffer, le.getResponseCode());
                if (le.getResponseCode() == LoginResponseCodes.WORLD_FULL)
                    worldFull = true;
            }
        }
    }

    private void handleException(SocketChannel channel, ByteBuffer buffer, int responseCode) {
        if (channel == null) return;
        try {
            buffer.clear();
            buffer.put((byte) responseCode);
            buffer.flip();
            channel.write(buffer);
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
