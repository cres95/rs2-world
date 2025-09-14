package io.github.cres95.rs2world.core;

import io.github.cres95.rs2world.net.ClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class World {

    private final WorldCycleContext worldCycleContext;
    private final ClientService clientService;

    public World(WorldCycleContext worldCycleContext, ClientService clientService) {
        this.worldCycleContext = worldCycleContext;
        this.clientService = clientService;
    }

    @Scheduled(fixedRate = 600L)
    private void cycle() {
        clientService.cycle(worldCycleContext);
    }

}