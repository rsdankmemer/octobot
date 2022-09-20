package org.octobot.script.event;

import org.octobot.script.event.listeners.GameEventListener;
import org.octobot.script.event.listeners.RandomListener;

/**
 * RandomEvent
 *
 * @author Pat-ji
 */
public class RandomEvent extends GameEvent {
    private final int type;
    private final Event event;

    public RandomEvent(final int type, final Event event) {
        this.type = type;
        this.event = event;
    }

    /**
     * This method is used to get the {@link RandomEvent}s {@link Event}
     *
     * @return the {@link RandomEvent}s {@link Event}
     */
    public Event getEvent() {
        return event;
    }

    /**
     * This method is used to dispatch a {@link RandomEvent}
     *
     * @param listener the {@link GameEventListener} to parse with the dispatch
     */
    @Override
    public void dispatch(final GameEventListener listener) {
        if (listener instanceof RandomListener)
            if (type == 0x00) {
                ((RandomListener) listener).onRandomStart(this);
            } else if (type == 0x01) {
                ((RandomListener) listener).onRandomFinish(this);
            }
    }

    /**
     * Event
     *
     * @author Pat-ji
     */
    public enum Event {
        WELCOME_SCREEN, BANK_PIN, EXP_LAMP, LOGIN, TALK_TO, WORLD_SWITCH;

        public static final Event[] values = values();

    }

}
