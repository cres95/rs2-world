package io.github.cres95.rs2world.net.login.host;

import io.github.cres95.rs2world.net.login.LoginException;
import io.github.cres95.rs2world.util.Frequency;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HostService {

    private final Map<HostEvent, Frequency> eventFrequencies;
    private final Map<String, Host> hosts = new HashMap<>();

    public HostService(HostProperties properties) {
        this.eventFrequencies = properties.getEventFrequencies();
    }

    public Host touch(String address) {
        Host host = hosts.get(address);
        if (host == null) {
            host = new Host(address);
            synchronized (hosts) {
                hosts.put(address, host);
            }
        }
        return host;
    }

    public void validateFor(Host host, HostEvent event) {
        Frequency frequency = eventFrequencies.get(event);
        if (frequency == null) return;
        if (updateAndCountEvents(host, event, frequency.getDuration().toMillis()) >= frequency.getOccurrences()) {
            throw LoginException.forResponse(event.getErrorCode());
        }
    }

    private int updateAndCountEvents(Host host, HostEvent event, long duration) {
        long cutoff = System.currentTimeMillis() - duration;
        List<Long> eventHistory = host.getEventHistoryFor(event);
        eventHistory.removeIf(t -> t < cutoff);
        return eventHistory.size();
    }

    public void prune() {
        synchronized (hosts) {
            hosts.values().removeIf(this::isInactive);
        }
    }

    private boolean isInactive(Host host) {
        for (HostEvent event : HostEvent.values()) {
            if (updateAndCountEvents(host, event, eventFrequencies.get(event).getDuration().toMillis()) > 0) {
                return false;
            }
        }
        return host.getCurrentConnections() == 0;
    }

}
