package io.github.cres95.rs2world.util;

public class SystemTimer {

    private long lastMeasure;

    public SystemTimer() {
        this.lastMeasure = System.nanoTime();
    }

    public long elapsed() {
        return System.nanoTime() - lastMeasure;
    }

    public boolean elapsed(long time) {
        return elapsed() >= time;
    }

    public void reset() {
        this.lastMeasure = System.nanoTime();
    }

    public boolean resetOnElapsed(long time) {
        boolean elapsed = elapsed(time);
        if (elapsed) reset();
        return elapsed;
    }
}
