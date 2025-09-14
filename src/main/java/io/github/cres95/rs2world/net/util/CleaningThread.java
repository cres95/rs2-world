package io.github.cres95.rs2world.net.util;

public class CleaningThread extends Thread {

    private final Runnable cleanup;

    public CleaningThread(Runnable task, Runnable cleanup) {
        super(task);
        this.cleanup = cleanup;
    }

    @Override
    public void run() {
        try {
            super.run();
        } finally {
            if (cleanup != null) cleanup.run();
        }
    }
}
