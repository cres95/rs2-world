package io.github.cres95.rs2world.net;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("rs2.net")
public class ServerProperties {

    private int port = 43594;
    private String host = "127.0.0.1";
    private int corePoolSize = 2;
    private int maxPoolSize = 10;
    private int keepAliveDuration = 60;
    private int bufferSize = 2048;
    private long batchAcceptDelay = 2000L;
    private int batchAcceptAttempts = 5;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveDuration() {
        return keepAliveDuration;
    }

    public void setKeepAliveDuration(int keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public long getBatchAcceptDelay() {
        return batchAcceptDelay;
    }

    public void setBatchAcceptDelay(long batchAcceptDelay) {
        this.batchAcceptDelay = batchAcceptDelay;
    }

    public int getBatchAcceptAttempts() {
        return batchAcceptAttempts;
    }

    public void setBatchAcceptAttempts(int batchAcceptAttempts) {
        this.batchAcceptAttempts = batchAcceptAttempts;
    }
}
