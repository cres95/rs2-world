package com.einherji.rs2world.net.packets;

import java.nio.ByteBuffer;

public interface PacketBuilder {

    Packet build(ByteBuffer buffer);

}
