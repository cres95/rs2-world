package io.github.cres95.rs2world.net.packets;

import io.github.cres95.rs2world.core.WorldCycleContext;
import io.github.cres95.rs2world.net.Client;

public interface Packet {

    void execute(Client client, WorldCycleContext ctx);

}
