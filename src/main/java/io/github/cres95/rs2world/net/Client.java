package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.net.login.host.Host;
import io.github.cres95.rs2world.net.packets.inbound.InboundPacketDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;

import java.util.function.Consumer;

public record Client(SocketChannel channel, Host host) {

    public static final AttributeKey<Client> ATTR_KEY = AttributeKey.valueOf("Client");

    public void send(Consumer<ByteBuf> operations) {
        send(256, operations);
    }

    public void send(int capacity, Consumer<ByteBuf> operations) {
        ByteBuf buffer = channel.alloc().buffer(capacity);
        operations.accept(buffer);
        channel.writeAndFlush(buffer);
    }

    public void disconnect() {
        channel.disconnect();
        host.onDisconnect();
    }
}
