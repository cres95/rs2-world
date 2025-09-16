package io.github.cres95.rs2world.net;

import org.springframework.context.ApplicationEvent;

public class ClientConnectedEvent extends ApplicationEvent {

    private final Client client;

    public ClientConnectedEvent(Object source, Client client) {
        super(source);
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
