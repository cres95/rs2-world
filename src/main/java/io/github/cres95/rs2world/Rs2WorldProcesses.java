package io.github.cres95.rs2world;

import io.github.cres95.rs2world.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Rs2WorldProcesses implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Rs2WorldProcesses.class);

    private final Server server;
    private final Engine engine;

    public Rs2WorldProcesses(Server server, Engine engine) {
        this.server = server;
        this.engine = engine;
    }

    @Override
    public void run(String... args) {
        server.start();
        engine.start();
    }

}
