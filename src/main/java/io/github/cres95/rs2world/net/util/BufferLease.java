package io.github.cres95.rs2world.net.util;

import java.nio.ByteBuffer;

public class BufferLease implements AutoCloseable {

    private ByteBuffer buffer;
    private final BufferPool pool;

    BufferLease(ByteBuffer buffer, BufferPool pool) {
        this.buffer = buffer;
        this.pool = pool;
    }

    public ByteBuffer buffer() {
        return buffer;
    }

    @Override
    public void close() {
        this.pool.recycle(buffer);
        this.buffer = null;
    }
}
