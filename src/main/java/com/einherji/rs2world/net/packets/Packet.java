package com.einherji.rs2world.net.packets;

import com.einherji.rs2world.net.clients.Client;

public interface Packet {

    void execute(Client client, PacketContext ctx);

}
