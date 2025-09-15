package io.github.cres95.rs2world.core;

/**
 * Whilst there is no real gain from implementing this interface on anything that requires a game tick, it is a handy
 * tool to be able to track those that do and don't already.
 * */
public interface WorldCycleAware {

    void cycle(WorldCycleContext ctx);

}
