package io.github.cres95.rs2world.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.AttributeKey;

import java.util.function.Consumer;

public class Client {

    public static final AttributeKey<Client> ATTR_KEY = AttributeKey.valueOf("Client");

    private final SocketChannel channel;
    private final Host host;

    public Client(SocketChannel channel, Host host) {
        this.channel = channel;
        this.host = host;
    }

    public void sendRaw(Consumer<ByteBuf> operations) {
        ByteBuf buffer = channel.alloc().buffer();
        operations.accept(buffer);
        channel.writeAndFlush(buffer);
    }

    public void disconnect() {
        channel.disconnect();
        host.onDisconnect();
    }

    public Host host() {
        return host;
    }


}
