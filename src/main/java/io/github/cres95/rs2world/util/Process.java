package io.github.cres95.rs2world.util;

public interface Process extends Runnable {

    long cycleRate();

    default void onShutdown() {

    }

}
