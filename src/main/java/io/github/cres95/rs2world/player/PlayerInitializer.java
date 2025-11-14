package io.github.cres95.rs2world.player;

import io.github.cres95.rs2world.net.packets.outbound.OutboundPacketChannel;

public final class PlayerInitializer {

    private int id;
    private int rank;
    private String username;
    private OutboundPacketChannel outboundPacketChannel;

    private PlayerInitializer(int id) {
        this.id = id;
    }

    public static PlayerInitializer withId(int id) {
        return new PlayerInitializer(id);
    }

    public PlayerInitializer withUsername(String username) {
        this.username = username;
        return this;
    }

    public PlayerInitializer withRank(int rank) {
        this.rank = rank;
        return this;
    }

    public PlayerInitializer withOutboundPacketChannel(OutboundPacketChannel outboundPacketChannel) {
        this.outboundPacketChannel = outboundPacketChannel;
        return this;
    }

    public Player build() {
        Player player = new Player(id, rank, username, null, outboundPacketChannel);
        return player;
    }
}
