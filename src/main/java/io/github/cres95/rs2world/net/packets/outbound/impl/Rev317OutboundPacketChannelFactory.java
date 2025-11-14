package io.github.cres95.rs2world.net.packets.outbound.impl;

import io.github.cres95.rs2world.SupportedRevisions;
import io.github.cres95.rs2world.net.Client;
import io.github.cres95.rs2world.net.packets.outbound.OutboundPacketChannel;
import io.github.cres95.rs2world.net.packets.outbound.OutboundPacketChannelFactory;
import io.github.cres95.rs2world.net.util.ISAACCipher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(SupportedRevisions.R317)
public class Rev317OutboundPacketChannelFactory implements OutboundPacketChannelFactory {

    @Override
    public OutboundPacketChannel create(Client client, ISAACCipher encryptor) {
        return new Rev317OutboundPacketChannel(client, encryptor);
    }
}
