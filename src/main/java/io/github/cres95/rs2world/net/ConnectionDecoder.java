package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.net.login.LoginRequestCode;
import io.github.cres95.rs2world.net.login.LoginResponseCode;
import io.github.cres95.rs2world.net.login.LoginService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

@Component @ChannelHandler.Sharable
public class ConnectionDecoder extends ChannelInboundHandlerAdapter {

    private final LoginService loginService;

    public ConnectionDecoder(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Client client = ctx.channel().attr(Client.ATTR_KEY).get();
        ByteBuf buffer = (ByteBuf) msg;
        if (buffer.readableBytes() < 2) {
            return;
        }
        int requestCode = buffer.readByte() & 0xff;
        buffer.readByte();
        if (requestCode == LoginRequestCode.CONNECTION_REQUEST) {
            client.sendRaw(b -> {
                b.writeLong(0L);
                b.writeByte(LoginResponseCode.PROCEED_TO_LOGIN);
                b.writeLong(loginService.generateServerSessionKey());
            });
            ctx.pipeline().remove("decoder");
            ctx.pipeline().addLast("decoder", loginService.newLoginDecoder());
        } else {
            client.disconnect();
        }
        buffer.release();
    }
}
