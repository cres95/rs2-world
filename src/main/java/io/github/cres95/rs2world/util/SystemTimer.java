package io.github.cres95.rs2world.util;

import java.util.concurrent.TimeUnit;

public class SystemTimer {

    private long lastMeasure;
    private final TimeUnit timeUnit;

    public SystemTimer() {
        reset();
        this.timeUnit = TimeUnit.MILLISECONDS;
    }

    public SystemTimer(TimeUnit timeUnit) {
        reset();
        this.timeUnit = timeUnit;
    }

    public long elapsed() {
        return System.nanoTime() - lastMeasure;
    }

    public boolean elapsed(long time) {
        return elapsed() >= toNanos(time);
    }

    public void reset() {
        this.lastMeasure = System.nanoTime();
    }

    public boolean resetOnElapsed(long time) {
        boolean elapsed = elapsed(time);
        if (elapsed) reset();
        return elapsed;
    }

    private long toNanos(long time) {
        return TimeUnit.NANOSECONDS.convert(time, timeUnit);
    }
}
