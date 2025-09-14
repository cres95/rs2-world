package io.github.cres95.rs2world.net.util;

import java.nio.ByteBuffer;

public class RS2BufferOps {

    public static String readString(ByteBuffer buffer) {
        StringBuilder b = new StringBuilder();
        byte temp;
        while ((temp = buffer.get()) != 10) {
            b.append((char) temp);
        }
        return b.toString();
    }

}
