package io.github.cres95.rs2world.net.packets;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import io.github.cres95.rs2world.net.util.ISAACCipher;

public interface PacketDecoder {

    PacketDecoder STAGE_1_LOGIN_PACKET_DECODER = new Stage1LoginPacketDecoder();
    PacketDecoder STAGE_2_LOGIN_PACKET_DECODER = new Stage2LoginPacketDecoder();

    Packet decode(ByteBuffer buffer, ISAACCipher decryptor);

    default Packet safeDecode(ByteBuffer buffer, ISAACCipher decryptor) {
        try {
            return decode(buffer, decryptor);
        } catch (BufferUnderflowException e) {
            return null;
        }
    }

}
