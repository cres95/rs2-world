package com.einherji.rs2world.net.clients;

import com.einherji.rs2world.util.Timer;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class Client {

    private final UUID uuid;
    private final SocketChannel channel;
    private final SelectionKey selectionKey;
    private final ByteBuffer outBuffer;
    private final Timer timeoutTimer = new Timer();

    private ClientState state;

    public Client(UUID uuid,
                  SocketChannel channel,
                  SelectionKey selectionKey,
                  ByteBuffer outBuffer) {
        this.uuid = uuid;
        this.channel = channel;
        this.selectionKey = selectionKey;
        this.outBuffer = outBuffer;
        this.state = ClientState.CONNECTING;
    }

    public UUID getUuid() {
        return uuid;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public ByteBuffer getOutBuffer() {
        return outBuffer;
    }

    public ClientState getState() {
        return state;
    }

    public Timer getTimeoutTimer() {
        return timeoutTimer;
    }
}
