package io.github.cres95.rs2world.net.login.host;

import io.github.cres95.rs2world.util.Frequency;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("rs2.login.hosts")
public class HostProperties {

    private Map<HostEvent, Frequency> eventFrequencies = new HashMap<>();

    public Map<HostEvent, Frequency> getEventFrequencies() {
        return eventFrequencies;
    }

    public void setEventFrequencies(Map<HostEvent, Frequency> eventFrequencies) {
        this.eventFrequencies = eventFrequencies;
    }
}
