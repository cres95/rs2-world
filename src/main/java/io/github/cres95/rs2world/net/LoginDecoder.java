package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.net.login.LoginRequest;
import io.github.cres95.rs2world.net.login.LoginRequestCode;
import io.github.cres95.rs2world.net.login.LoginRequestEvent;
import io.github.cres95.rs2world.net.util.BufferOps;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.ApplicationEventPublisher;

public class LoginDecoder extends ChannelInboundHandlerAdapter {

    private final ApplicationEventPublisher applicationEventPublisher;
    private ByteBuf buffer;
    private Integer requestCode;
    private Integer length;

    public LoginDecoder(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf incoming = (ByteBuf) msg;
        buffer.writeBytes(incoming);
        incoming.release();

        if (requestCode == null && length == null) {
            if (buffer.readableBytes() < 2) return;
            requestCode = buffer.readByte() & 0xff;
            length = buffer.readByte() & 0xff;
        }

        if (buffer.readableBytes() < length) return;

        buffer.readByte(); //255
        int clientVersion = buffer.readShort();
        int memory = buffer.readByte();
        for (int i = 0; i < 9; i++) buffer.readInt();
        buffer.readByte();
        int rsaOpcode = buffer.readByte();
        long clientSessionKey = buffer.readLong();
        long serverSessionKey = buffer.readLong();
        int sessionId = buffer.readInt();
        String username = BufferOps.readString(buffer);
        String password = BufferOps.readString(buffer);
        Client client = ctx.channel().attr(Client.ATTR_KEY).get();
        boolean reconnecting = requestCode == LoginRequestCode.RECONNECT_REQUEST;
        LoginRequest loginRequest = new LoginRequest(client, sessionId, serverSessionKey, clientSessionKey, username, password, reconnecting);
        applicationEventPublisher.publishEvent(new LoginRequestEvent(this, loginRequest));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buffer = ctx.alloc().buffer(512);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buffer.release();
        buffer = null;
    }
}
