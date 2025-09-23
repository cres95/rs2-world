package io.github.cres95.rs2world.net;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Host {

    private static final long ACCOUNT_CREATION_INTERVAL = 3600000L; // 1 Hour
    private static final int ACCOUNT_CREATION_LIMIT = 2;
    private static final long LOGIN_ATTEMPT_INTERVAL = 20000L;
    private static final long LOGIN_ATTEMPT_LIMIT = 5;
    private static final long CONNECTION_THROTTLE_INTERVAL = 5000L;
    private static final int CONNECTION_THROTTLE_LIMIT = 2;

    private final AtomicInteger currentConnections = new AtomicInteger(0);
    private final List<Long> connectionAttempts = new LinkedList<>();
    private final List<Long> loginAttempts = new LinkedList<>();
    private final List<Long> accountCreations = new LinkedList<>();
    private final String address;

    public Host(String address) {
        this.address = address;
    }

    public void onConnectionAttempt() {
        connectionAttempts.add(System.currentTimeMillis());
    }

    public void onConnect() {
        currentConnections.incrementAndGet();
    }

    public void onDisconnect() {
        currentConnections.decrementAndGet();
    }

    public void onFailedLogin() {
        loginAttempts.add(System.currentTimeMillis());
    }

    public void onAccountCreation() {
        accountCreations.add(System.currentTimeMillis());
    }

    public boolean reachedThrottleLimit() {
        return updateAndCountConnectionAttempts() >= CONNECTION_THROTTLE_LIMIT;
    }

    public boolean reachedLoginAttemptLimit() {
        return updateAndCountLoginAttempts() >= LOGIN_ATTEMPT_LIMIT;
    }

    public boolean reachedAccountCreationLimit() {
        return updateAndCountAccountCreations() >= ACCOUNT_CREATION_LIMIT;
    }

    public String getAddress() {
        return address;
    }

    int updateAndCountLoginAttempts() {
        long cutoff = System.currentTimeMillis() - LOGIN_ATTEMPT_INTERVAL;
        loginAttempts.removeIf(attempt -> attempt < cutoff);
        return loginAttempts.size();
    }

    int updateAndCountConnectionAttempts() {
        long cutoff = System.currentTimeMillis() - CONNECTION_THROTTLE_INTERVAL;
        connectionAttempts.removeIf(attempt -> attempt < cutoff);
        return connectionAttempts.size();
    }

    int updateAndCountAccountCreations() {
        long cutoff = System.currentTimeMillis() - ACCOUNT_CREATION_INTERVAL;
        accountCreations.removeIf(attempt -> attempt < cutoff);
        return accountCreations.size();
    }

    boolean isInactive() {
        return currentConnections.get() == 0
                && updateAndCountConnectionAttempts() == 0
                && updateAndCountLoginAttempts() == 0
                && updateAndCountAccountCreations() == 0;
    }
}
