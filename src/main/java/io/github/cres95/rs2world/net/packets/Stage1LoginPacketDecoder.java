package io.github.cres95.rs2world.net.packets;

import io.github.cres95.rs2world.core.WorldCycleContext;
import io.github.cres95.rs2world.net.Client;
import io.github.cres95.rs2world.net.login.LoginRequestCode;
import io.github.cres95.rs2world.net.login.LoginResponseCode;
import io.github.cres95.rs2world.net.util.ISAACCipher;

import java.nio.ByteBuffer;

public class Stage1LoginPacketDecoder implements PacketDecoder {

    @Override
    public Packet decode(ByteBuffer buffer, ISAACCipher decryptor) {
        if (buffer.remaining() < 2) return null;
        int requestCode = buffer.get() & 0xff;
        buffer.get();
        return new Stage1LoginPacket(requestCode);
    }

    private record Stage1LoginPacket(int requestCode) implements Packet {
        @Override
        public void execute(Client client, WorldCycleContext ctx) {
            int response = requestCode == LoginRequestCode.CONNECTION_REQUEST
                    ? LoginResponseCode.PROCEED_TO_LOGIN
                    : LoginResponseCode.BAD_SESSION_ID;
            client.getOutBuffer().clear();
            client.getOutBuffer().putLong(0L);
            client.getOutBuffer().put((byte) response);
            client.getOutBuffer().putLong(ctx.generateServerSessionKey());
            client.flushOutBuffer();
            client.setPacketDecoder(STAGE_2_LOGIN_PACKET_DECODER);
        }
    }

}