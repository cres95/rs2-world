package io.github.cres95.rs2world;

public interface Rs2WorldProcess extends Runnable {

    long cycleRate();

    default void onShutdown() {

    }

}
