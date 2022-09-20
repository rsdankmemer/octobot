package org.octobot.bot.event;

import org.octobot.script.event.GameEvent;
import org.octobot.script.event.listeners.GameEventListener;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * EventQueue
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class EventQueue implements Runnable {
    private static final Object LOCK = new Object();

    /**
     * StopEvent
     *
     * @author Pat-ji
     */
    private static class StopEvent extends GameEvent {
        @Override
        public void dispatch(final GameEventListener listener) {
        }
    }

    private final EventHandler handler;
    private final Map<Integer, EventObject> queue;

    private Thread thread;

    public EventQueue() {
        handler = new EventHandler();
        queue = new HashMap<Integer, EventObject>();
    }

    public void clear() {
        handler.clear();
        queue.clear();
    }

    public void dispatchEvent(final EventObject event) {
        synchronized (queue) {
            boolean added = false;
            for (int i = 0; i < queue.size(); ++i)
                if (!queue.containsKey(i)) {
                    queue.put(i, event);
                    added = true;
                    break;
                }

            if (!added)
                queue.put(queue.size(), event);

            queue.notifyAll();
        }
    }

    private void processEvent(final EventObject event) {
        handler.dispatch(event);
    }

    public void addListener(final GameEventListener listener) {
        handler.addListener(listener);
    }

    public void removeListener(final GameEventListener listener) {
        handler.removeListener(listener);
    }

    public void stop() {
        synchronized (LOCK) {
            dispatchEvent(new StopEvent());
        }
    }

    @Override
    public void run() {
        while (thread != null && thread.isAlive()) {
            try {
                EventObject event = null;
                synchronized (queue) {
                    while (queue.size() <= 0) {
                        try {
                            queue.wait(20);
                        } catch (final Exception ignored) { }
                    }

                    int empty = 0;
                    for (int i = 0; i < queue.size() + empty; ++i) {
                        if (!queue.containsKey(i)) {
                            empty++;
                            continue;
                        }

                        event = queue.get(i);
                        queue.remove(i);
                        break;
                    }
                }

                if (event != null) {
                    if (event instanceof StopEvent) {
                        thread = null;
                        synchronized (event) {
                            event.notifyAll();
                        }

                        return;
                    }

                    try {
                        processEvent(event);
                    } catch (final ThreadDeath death) {
                        thread = null;
                        event.notifyAll();
                        return;
                    } catch (final Throwable e) {
                        System.out.println("[EventQueue] - Throwable caught in event processing: " + e.getMessage());
                    }

                    synchronized (event) {
                        event.notifyAll();
                    }
                }
            } catch (final Exception e) {
                System.out.println("[EventQueue] - Exception caught in the queue processing: " + e.getMessage());
            }
        }
    }

    public void start() {
        synchronized (LOCK) {
            if (thread != null)
                throw new IllegalThreadStateException("[EventQueue] - Event thread is already initialized.");

            thread = new Thread(this, "EventQueue");
            thread.setDaemon(true);
            thread.start();
        }
    }

}
