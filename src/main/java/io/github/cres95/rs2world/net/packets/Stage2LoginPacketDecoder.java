package io.github.cres95.rs2world.net.packets;

import io.github.cres95.rs2world.core.WorldCycleContext;
import io.github.cres95.rs2world.net.Client;
import io.github.cres95.rs2world.net.util.ISAACCipher;
import io.github.cres95.rs2world.net.util.RS2BufferOps;

import java.nio.ByteBuffer;

public class Stage2LoginPacketDecoder implements PacketDecoder {

    @Override
    public Packet decode(ByteBuffer buffer, ISAACCipher decryptor) {
        if (buffer.remaining() < 2) return null;
        int requestCode = buffer.get();
        int length = buffer.get() & 0xff;
        if (buffer.remaining() < length) return null;
        buffer.get(); //255
        int clientVersion = buffer.getShort();
        buffer.get(); //memory
        for (int i = 0; i < 9; i++) {
            buffer.getInt();
        }
        buffer.get();
        int rsaOpcode = buffer.get();
        long clientSessionKey = buffer.getLong();
        long serverSessionKey = buffer.getLong();
        int sessionId = buffer.getInt();
        String username = RS2BufferOps.readString(buffer);
        String password = RS2BufferOps.readString(buffer);
        return new Stage2LoginPacket(requestCode, sessionId, username, password, clientSessionKey, serverSessionKey);
    }

    private record Stage2LoginPacket(int loginRequestCode,
                                     int sessionId,
                                     String username,
                                     String password,
                                     long clientSessionKey,
                                     long serverSessionKey) implements Packet {

        @Override
        public void execute(Client client, WorldCycleContext ctx) {
            System.out.printf("Login request with username: %s and password: %s%n", username, password);
        }
    }
}
