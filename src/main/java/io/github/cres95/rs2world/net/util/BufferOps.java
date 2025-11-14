package io.github.cres95.rs2world.net.util;

import io.netty.buffer.ByteBuf;

public class BufferOps {

    public static int writeVariableHeader(ByteBuf buf, int header) {
        buf.writeByte(header);
        int currentPos = buf.writerIndex();
        buf.writeByte(0);
        return currentPos;
    }

    public static void finishVariableHeader(ByteBuf b, int markedPos) {
        b.setByte(markedPos, (byte) (b.writerIndex() - markedPos - 1));
    }

    public static void writeString(ByteBuf buf, String str) {
        for (byte b : str.getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(10);
    }

    public static String readString(ByteBuf buffer) {
        StringBuilder b = new StringBuilder();
        byte temp;
        while ((temp = buffer.readByte()) != 10) {
            b.append((char) temp);
        }
        return b.toString();
    }

}
