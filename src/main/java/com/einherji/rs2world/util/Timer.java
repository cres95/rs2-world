package com.einherji.rs2world.util;

public class Timer {

    private long lastMeasure;

    public Timer() {
        reset();
    }

    public void reset() {
        lastMeasure = System.currentTimeMillis();
    }

    public long elapsed() {
        return System.currentTimeMillis() - lastMeasure;
    }

    public boolean elapsed(long t) {
        return elapsed() >= t;
    }

}
