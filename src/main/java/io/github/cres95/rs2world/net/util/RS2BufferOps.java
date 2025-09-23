package io.github.cres95.rs2world.net.util;

import io.netty.buffer.ByteBuf;

public class RS2BufferOps {

    public static String readString(ByteBuf buffer) {
        StringBuilder b = new StringBuilder();
        byte temp;
        while ((temp = buffer.readByte()) != 10) {
            b.append((char) temp);
        }
        return b.toString();
    }

}
