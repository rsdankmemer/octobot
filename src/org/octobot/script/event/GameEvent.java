package org.octobot.script.event;

import org.octobot.script.event.listeners.GameEventListener;

import java.util.EventObject;

/**
 * GameEvent
 *
 * @author Pat-ji
 */
public abstract class GameEvent extends EventObject {
    private static final Object SOURCE = new Object();

    public GameEvent() {
        super(SOURCE);
    }

    /**
     * This method is called to dispatch a {@link GameEvent}
     *
     * @param listener the {@link GameEventListener} to parse the {@link GameEvent} with
     */
    public abstract void dispatch(final GameEventListener listener);

}
