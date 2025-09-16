package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.net.packets.PacketContext;
import io.github.cres95.rs2world.net.packets.Packet;
import io.github.cres95.rs2world.net.packets.PacketDecoder;
import io.github.cres95.rs2world.net.util.BufferLease;
import io.github.cres95.rs2world.util.SystemTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private static final long TIMEOUT_THRESHOLD = 10000L;

    private final BufferLease outBufferLease;
    private final Queue<Packet> packetQueue;
    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;
    private final SystemTimer timeoutTimer;
    private PacketDecoder packetDecoder;
    private boolean disconnected;

    public Client(BufferLease bufferLease,
                   SelectionKey key,
                   SocketChannel channel,
                   PacketDecoder packetDecoder) {
        this.outBufferLease = bufferLease;
        this.packetQueue = new LinkedList<>();
        this.selectionKey = key;
        this.socketChannel = channel;
        this.timeoutTimer = new SystemTimer();
        this.packetDecoder = packetDecoder;
        this.disconnected = false;
    }

    public void send(Consumer<ByteBuffer> instructions) {
        Assert.notNull(instructions, "'instructions' must not be null");
        ByteBuffer buffer = outBufferLease.buffer();
        buffer.clear();
        instructions.accept(buffer);
        flushOutBuffer();
    }

    public void decode(ByteBuffer in) {
        try {
            int readBytes = socketChannel.read(in);
            if (readBytes == -1) {
                disconnect();
                return;
            }
        } catch(IOException ioe) {
            disconnect();
            return;
        }
        in.flip();
        Packet packet = null;
        while ((packet = packetDecoder.safeDecode(in, null)) != null) {
            synchronized (packetQueue) {
                packetQueue.add(packet);
            }
        }
        timeoutTimer.reset();
    }

    public void setPacketDecoder(PacketDecoder packetDecoder) {
        this.packetDecoder = packetDecoder;
    }

    public void disconnect() {
        if (disconnected) return;
        this.disconnected = true;
        this.outBufferLease.close();
        this.selectionKey.cancel();
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void processQueuedPackets(PacketContext ctx) {
        if (timeoutTimer.elapsed(TIMEOUT_THRESHOLD) && !isDisconnected()) {
            disconnect();
            return;
        }
        synchronized (packetQueue) {
            Packet packet = null;
            while ((packet = packetQueue.poll()) != null) {
                packet.execute(this, ctx);
            }
        }
    }

    private void flushOutBuffer() {
        try {
            ByteBuffer buffer = outBufferLease.buffer();
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            buffer.clear();
        } catch(IOException e) {
            disconnect();
        }
    }
}
