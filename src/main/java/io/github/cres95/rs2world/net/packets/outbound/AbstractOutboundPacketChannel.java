package io.github.cres95.rs2world.net.packets.outbound;

import io.github.cres95.rs2world.net.Client;
import io.github.cres95.rs2world.net.util.ISAACCipher;
import io.netty.buffer.ByteBuf;

import java.util.function.Consumer;

public abstract class AbstractOutboundPacketChannel implements OutboundPacketChannel {

    private final Client client;
    private final ISAACCipher encryptor;

    public AbstractOutboundPacketChannel(final Client client, ISAACCipher encryptor) {
        this.client = client;
        this.encryptor = encryptor;
    }

    protected void send(int bytes, Consumer<ByteBuf> ops) {
        client.send(bytes, ops);
    }

    protected int encrypt(int value) {
        return value + encryptor.getNextValue();
    }


}
