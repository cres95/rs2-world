package io.github.cres95.rs2world.net.packets;

import io.github.cres95.rs2world.login.LoginRequestCode;
import io.github.cres95.rs2world.login.LoginResponseCode;
import io.github.cres95.rs2world.net.Client;
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
        public void execute(Client client, PacketContext ctx) {
            int response = requestCode == LoginRequestCode.CONNECTION_REQUEST
                    ? LoginResponseCode.PROCEED_TO_LOGIN
                    : LoginResponseCode.BAD_SESSION_ID;
            client.send(b -> {
                b.putLong(0L);
                b.put((byte) response);
                b.putLong(ctx.generateServerSessionKey());
            });
            if (response == LoginResponseCode.PROCEED_TO_LOGIN) {
                client.setPacketDecoder(STAGE_2_LOGIN_PACKET_DECODER);
            } else {
                client.disconnect();
            }
        }
    }

}