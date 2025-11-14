package io.github.cres95.rs2world.net.packets.inbound;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InboundPacketQueue {

    private final List<InboundPacket> packets = new LinkedList<>();

    void add(InboundPacket packet) {
        synchronized (packets) {
            packets.add(packet);
        }
    }

    public List<InboundPacket> poll() {
        synchronized (packets) {
            List<InboundPacket> polledPackets = new ArrayList<>(packets);
            packets.clear();
            return polledPackets;
        }
    }



}
