package io.github.cres95.rs2world.net.packets.inbound;

import io.github.cres95.rs2world.net.util.ISAACCipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboundPacketDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboundPacketDecoder.class);

    private final InboundPacketBuilderDemux demux;
    private final InboundPacketQueue queue;
    private final ISAACCipher decryptor;

    private ByteBuf buffer;
    private Integer opcode;
    private Integer length;

    public InboundPacketDecoder(InboundPacketBuilderDemux demux,
                                InboundPacketQueue queue,
                                ISAACCipher decryptor) {
        this.demux = demux;
        this.queue = queue;
        this.decryptor = decryptor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf incoming = (ByteBuf) msg;
        buffer.writeBytes(incoming);
        incoming.release();
        while (buffer.readableBytes() > 0) {
            if (opcode == null) {
                opcode = (buffer.readByte() & 0xff) - (decryptor.getNextValue() & 0xff);
            }
            InboundPacketBuilder builder = demux.getPacketBuilder(opcode);
            if (builder == null) {
                LOGGER.info("No packet builder found for opcode: {}, discarding buffer", opcode);
                buffer.clear();
                opcode = null;
                length = null;
                return;
            }
            if (length == null) {
                length = builder.length();
            }
            if (length == InboundPacketBuilder.VARIABLE_LENGTH) {
                if (buffer.readableBytes() < 1) {
                    compact();
                    return;
                }
                length = buffer.readByte() & 0xff;
            }
            if (length < 0) {
                LOGGER.info("Packet length decoded to be negative, opcode: {}", opcode);
                buffer.clear();
                opcode = null;
                length = null;
                return;
            }
            if (buffer.readableBytes() < length) {
                compact();
                return;
            }
            InboundPacket packet = builder.build(buffer);
            queue.add(packet);
            opcode = null;
            length = null;
        }
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

    private void compact() {
        if (buffer.writableBytes() < 256) {
            buffer.discardReadBytes();
        }
    }

}
