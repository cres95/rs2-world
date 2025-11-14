package io.github.cres95.rs2world.net.packets.inbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InboundPacketBuilderDemux {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboundPacketBuilderDemux.class);

    private final Map<Integer, InboundPacketBuilder> packetBuilders = new HashMap<>();

    public InboundPacketBuilderDemux(List<InboundPacketBuilder> packetBuilders) {
        packetBuilders.forEach(pb -> this.packetBuilders.put(pb.opcode(), pb));
    }

    public InboundPacketBuilder getPacketBuilder(int opcode) {
        InboundPacketBuilder inboundPacketBuilder = packetBuilders.get(opcode);
        if (inboundPacketBuilder == null) {
            LOGGER.info("Could not find packet builder for opcode {}", opcode);
        }
        return inboundPacketBuilder;
    }

}
