package io.github.cres95.rs2world.net.util;

import io.github.cres95.rs2world.net.ServerProperties;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BufferPool {

    private final Queue<ByteBuffer> pool;
    private final int bufferSize;
    private final AtomicInteger size;

    public BufferPool(ServerProperties serverProperties) {
        this.bufferSize = serverProperties.getBufferSize();
        this.pool = new LinkedList<>();
        this.size = new AtomicInteger(0);
    }

    public BufferLease lease() {
        synchronized (pool) {
            ByteBuffer polled = pool.poll();
            if (polled == null) {
                polled = ByteBuffer.allocateDirect(bufferSize);
                size.incrementAndGet();
            }
            return new BufferLease(polled, this);
        }
    }

    public void recycle(ByteBuffer buffer) {
        synchronized (pool) {
            buffer.clear();
            pool.add(buffer);
        }
    }

    public int getEstimatedTotalSize() {
        return size.get() * bufferSize;
    }
}
