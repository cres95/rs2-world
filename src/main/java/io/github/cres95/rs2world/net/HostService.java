package io.github.cres95.rs2world.net;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HostService {

    private final Map<String, Host> hosts = new HashMap<>();

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

    public void prune() {
        synchronized (hosts) {
            hosts.values().removeIf(Host::isInactive);
        }
    }

}
