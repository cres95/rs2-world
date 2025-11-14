package io.github.cres95.rs2world;

import io.github.cres95.rs2world.events.EventService;
import io.github.cres95.rs2world.player.PlayerService;
import io.github.cres95.rs2world.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class Engine {

    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);

    private static long currentTick = 0;

    private final Timer timer = Timer.forNanos();
    private final ScheduledExecutorService scheduledExecutorService;
    private final PlayerService playerService;
    private final EventService eventService;

    private ScheduledFuture future;

    public Engine(PlayerService playerService,
                  EventService eventService) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.playerService = playerService;
        this.eventService = eventService;
    }

    public void run() {
        timer.reset();
        currentTick++;
        playerService.cycle();
        eventService.triggerScheduledEvents();
    }

    public static long getCurrentTick() {
        return currentTick;
    }

    public void start() {
        future = scheduledExecutorService.scheduleAtFixedRate(this::run, 0, 600, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        future.cancel(false);
    }
}
