package io.github.cres95.rs2world.player;

import io.github.cres95.rs2world.net.packets.inbound.InboundPacketQueue;
import io.github.cres95.rs2world.net.packets.outbound.OutboundPacketChannel;
import io.github.cres95.rs2world.region.Position;

public final class Player {

    private final int id;
    private final int rank;
    private final String username;
    private final Position position;
    private final InboundPacketQueue inboundPacketQueue;
    private final OutboundPacketChannel outboundPacketChannel;

    public Player(
            int id,
            int rank,
            String username,
            Position position,
            OutboundPacketChannel outboundPacketChannel) {
        this.id = id;
        this.rank = rank;
        this.username = username;
        this.position = position;
        this.outboundPacketChannel = outboundPacketChannel;
        this.inboundPacketQueue = new InboundPacketQueue();
    }

    public int id() {
        return id;
    }

    public int rank() {
        return rank;
    }

    public String username() {
        return username;
    }

    public Position position() {
        return position;
    }

    public InboundPacketQueue inboundPacketQueue() {
        return inboundPacketQueue;
    }

    public OutboundPacketChannel outboundPacketChannel() {
        return outboundPacketChannel;
    }

}
