package io.github.cres95.rs2world.core;

import io.github.cres95.rs2world.net.ClientService;
import io.github.cres95.rs2world.Rs2WorldProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorldProcess implements Rs2WorldProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorldProcess.class);

    private final WorldCycleContext worldCycleContext;
    private final ClientService clientService;

    public WorldProcess(WorldCycleContext worldCycleContext, ClientService clientService) {
        this.worldCycleContext = worldCycleContext;
        this.clientService = clientService;
    }

    @Override
    public void run() {
        clientService.cycle(worldCycleContext);
    }

    @Override
    public long cycleRate() {
        return 600L;
    }
}