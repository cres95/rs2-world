package io.github.cres95.rs2world.events;

public class EventFuture {

    private final Event event;
    private boolean cancelled;

    EventFuture(Event event) {
        this.event = event;
        this.cancelled = false;
    }

    Event getEvent() {
        return event;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

}
