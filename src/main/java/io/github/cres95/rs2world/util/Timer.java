package io.github.cres95.rs2world.util;

import io.github.cres95.rs2world.Engine;

import java.util.function.Supplier;

public class Timer {

    private static final Supplier<Long> TICK_SUPPLIER = Engine::getCurrentTick;
    private static final Supplier<Long> NANO_SUPPLIER = System::nanoTime;

    private long lastMeasure;
    private final Supplier<Long> timeSupplier;

    private Timer(Supplier<Long> timeSupplier) {
        this.timeSupplier = timeSupplier;
        reset();
    }

    public static Timer forTicks() {
        return new Timer(TICK_SUPPLIER);
    }

    public static Timer forNanos() {
        return new Timer(NANO_SUPPLIER);
    }

    public long elapsed() {
        return timeSupplier.get() - lastMeasure;
    }

    public boolean elapsed(long time) {
        return elapsed() >= time;
    }

    public void reset() {
        this.lastMeasure = timeSupplier.get();
    }

    public boolean resetOnElapsed(long time) {
        boolean elapsed = elapsed(time);
        if (elapsed) reset();
        return elapsed;
    }

}
