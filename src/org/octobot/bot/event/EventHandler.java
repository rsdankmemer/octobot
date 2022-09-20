package org.octobot.bot.event;

import org.octobot.script.event.GameEvent;
import org.octobot.script.event.listeners.GameEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

/**
 * EventHandler
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class EventHandler implements EventListener {
    private static final Object LOCK = new Object();

    private final List<GameEventListener> listeners;

    public EventHandler() {
        listeners = new ArrayList<GameEventListener>();
    }

    public void clear() {
        listeners.clear();
    }

    public void addListener(final GameEventListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(final GameEventListener listener) {
        listeners.remove(listener);
    }

    public void dispatch(final EventObject event) {
        synchronized (EventHandler.LOCK) {
            for (int i = 0; i < listeners.size(); i++) {
                if (!(event instanceof GameEvent)) continue;

                final GameEvent gameEvent = (GameEvent) event;
                gameEvent.dispatch(listeners.get(i));
            }
        }
    }

}
