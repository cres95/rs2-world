package io.github.cres95.rs2world.net.packets.inbound;

import io.netty.buffer.ByteBuf;

public interface InboundPacketBuilder {

    int VARIABLE_LENGTH = -1;

    InboundPacket build(ByteBuf buffer);

    int opcode();

    int length();

}
