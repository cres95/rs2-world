package io.github.cres95.rs2world.util;

import io.github.cres95.rs2world.net.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProcessRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessRunner.class);

    private final Server server;

    public ProcessRunner(Server server) {
        this.server = server;
    }

    @Override
    public void run(String... args) {
        server.start();
    }

}
