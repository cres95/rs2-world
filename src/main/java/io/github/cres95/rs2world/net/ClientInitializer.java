package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.net.login.host.Host;
import io.github.cres95.rs2world.net.login.host.HostEvent;
import io.github.cres95.rs2world.net.login.host.HostService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInitializer.class);

    private final ConnectionDecoder connectionDecoder;
    private final HostService hostService;

    public ClientInitializer(ConnectionDecoder connectionDecoder,
                             HostService hostService) {
        this.connectionDecoder = connectionDecoder;
        this.hostService = hostService;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        Host host = hostService.touch(ch.remoteAddress().getHostString());
        host.event(HostEvent.CONNECTION_ATTEMPT);
        hostService.validateFor(host, HostEvent.CONNECTION_ATTEMPT);
        host.onConnect();
        ch.attr(Client.ATTR_KEY).set(new Client(ch, host));
        ch.pipeline().addLast("decoder", connectionDecoder);
    }
}
