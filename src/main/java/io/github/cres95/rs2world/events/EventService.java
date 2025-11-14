package io.github.cres95.rs2world.events;

import io.github.cres95.rs2world.Engine;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

@Component
public class EventService {

    private final Map<Long, List<EventFuture>> events = new HashMap<>();

    public EventFuture schedule(int delay, Runnable evt) {
        return schedule(delay, () -> {
            evt.run();
            return 0;
        });
    }

    public EventFuture scheduleRepeating(int delay, Runnable evt) {
        return schedule(delay, () -> {
            evt.run();
            return delay;
        });
    }

    public EventFuture schedule(int delay, Event evt) {
        Assert.isTrue(delay > 0, "delay must be positive");
        EventFuture future = new EventFuture(evt);
        getEventListForTickWithDelay(delay).add(future);
        return future;
    }

    private List<EventFuture> getEventListForTickWithDelay(long delay) {
        long targetTick = Engine.getCurrentTick() + delay;
        return events.computeIfAbsent(targetTick, k -> new LinkedList<>());
    }

    public void triggerScheduledEvents() {
        List<EventFuture> eventFutures = events.get(Engine.getCurrentTick());
        if (eventFutures == null) return;
        for (EventFuture future : eventFutures) {
            if (!future.isCancelled()) {
                int delay = future.getEvent().execute();
                if (delay > 0) {
                    getEventListForTickWithDelay(delay).add(future);
                }
            }
        }
        eventFutures.clear();
        events.remove(Engine.getCurrentTick());
    }



}
