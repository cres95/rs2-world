package io.github.cres95.rs2world.net.login.host;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Host {

    private final AtomicInteger currentConnections;
    private final Map<HostEvent, List<Long>> eventHistory;
    private final String address;

    public Host(String address) {
        this.currentConnections = new AtomicInteger(0);
        this.eventHistory = new HashMap<>();
        this.address = address;
    }

    public void event(HostEvent event) {
        getEventHistoryFor(event).add(System.currentTimeMillis());
    }

    public void onConnect() {
        currentConnections.incrementAndGet();
    }

    public void onDisconnect() {
        currentConnections.decrementAndGet();
    }

    public String getAddress() {
        return address;
    }

    int getCurrentConnections() {
        return currentConnections.get();
    }

    List<Long> getEventHistoryFor(HostEvent event) {
        return eventHistory.computeIfAbsent(event, k -> new LinkedList<>());
    }
}
