package io.github.cres95.rs2world.net.packets.outbound.impl;

import io.github.cres95.rs2world.net.Client;
import io.github.cres95.rs2world.net.packets.outbound.AbstractOutboundPacketChannel;
import io.github.cres95.rs2world.net.util.ISAACCipher;
import io.github.cres95.rs2world.net.util.BufferOps;

public class Rev317OutboundPacketChannel extends AbstractOutboundPacketChannel {

    public Rev317OutboundPacketChannel(Client client, ISAACCipher encryptor) {
        super(client, encryptor);
    }

    @Override
    public void sendMessage(String message) {
        send(message.length() + 3, b -> {
            int mark = BufferOps.writeVariableHeader(b, encrypt(253));
            BufferOps.writeString(b, message);
            BufferOps.finishVariableHeader(b, mark);
        });
    }
}
