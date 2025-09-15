package io.github.cres95.rs2world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class RS2WorldProcesses implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RS2WorldProcesses.class);

    private final ScheduledExecutorService scheduledExecutorService;
    private final List<Rs2WorldProcess> processes;
    private final List<ScheduledFuture<?>> futures;

    public RS2WorldProcesses(List<Rs2WorldProcess> processes) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(processes.size());
        this.processes = processes;
        this.futures = new ArrayList<>(processes.size());
    }

    @Override
    public void run(String... args) {
        processes.forEach(p -> futures.add(scheduledExecutorService.scheduleAtFixedRate(p, 0, p.cycleRate(), TimeUnit.MILLISECONDS)));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> processes.forEach(Rs2WorldProcess::onShutdown)));
    }

    public void shutdown() {
        //do not interrupt these threads because one of them is running this section
        futures.forEach(f -> f.cancel(false));
        scheduledExecutorService.shutdown();
    }
}
