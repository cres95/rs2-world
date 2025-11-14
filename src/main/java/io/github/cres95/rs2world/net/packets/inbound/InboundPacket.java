package io.github.cres95.rs2world.net.packets.inbound;

import io.github.cres95.rs2world.player.Player;

public interface InboundPacket {

    void execute(Player player);

}