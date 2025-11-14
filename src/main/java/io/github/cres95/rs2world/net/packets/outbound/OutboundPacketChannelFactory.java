package io.github.cres95.rs2world.net.packets.outbound;

import io.github.cres95.rs2world.net.Client;
import io.github.cres95.rs2world.net.util.ISAACCipher;

public interface OutboundPacketChannelFactory {

    OutboundPacketChannel create(Client client, ISAACCipher encryptor);

}
