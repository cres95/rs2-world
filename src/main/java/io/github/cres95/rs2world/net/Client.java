package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.core.WorldCycleAware;
import io.github.cres95.rs2world.core.WorldCycleContext;
import io.github.cres95.rs2world.net.packets.Packet;
import io.github.cres95.rs2world.net.packets.PacketDecoder;
import io.github.cres95.rs2world.net.util.BufferLease;
import io.github.cres95.rs2world.util.SystemTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

public class Client implements WorldCycleAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private static final long TIMEOUT_THRESHOLD = 10000L;

    private final int id;
    private final BufferLease outBufferLease;
    private final Queue<Packet> packetQueue;
    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;
    private final SystemTimer timeoutTimer;
    private PacketDecoder packetDecoder;
    private boolean disconnected;

    public Client(int id,
                   BufferLease bufferLease,
                   SelectionKey key,
                   SocketChannel channel,
                   PacketDecoder packetDecoder) {
        this.id = id;
        this.outBufferLease = bufferLease;
        this.packetQueue = new LinkedList<>();
        this.selectionKey = key;
        this.socketChannel = channel;
        this.timeoutTimer = new SystemTimer();
        this.packetDecoder = packetDecoder;
        this.disconnected = false;
    }

    public int getId() {
        return id;
    }

    public void flushOutBuffer() {
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

    public void readAndDecode(ByteBuffer in) {
        try {
            int readBytes = socketChannel.read(in);
            if (readBytes == -1) {
                //error
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

    public ByteBuffer getOutBuffer() {
        return outBufferLease.buffer();
    }

    void disconnect() {
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

    @Override
    public void cycle(WorldCycleContext ctx) {
        if (timeoutTimer.elapsed(TIMEOUT_THRESHOLD) && !isDisconnected()) {
            disconnect();
        }
        synchronized (packetQueue) {
            Packet packet = null;
            while ((packet = packetQueue.poll()) != null) {
                packet.execute(this, ctx);
            }
        }
    }
}
