package org.octobot.script.event;

import org.octobot.bot.GameDefinition;
import org.octobot.script.event.listeners.GameEventListener;
import org.octobot.script.event.listeners.MessageListener;

/**
 * MessageEvent
 *
 * @author Pat-ji
 */
public class MessageEvent extends GameEvent {
    private final int id;
    private final String sender;
    private final String message;

    public MessageEvent(final GameDefinition definition, final int id, final String sender, final String message) {
        this.id = id;
        this.sender = sender;
        this.message = message;

        definition.eventQueue.dispatchEvent(this);
    }

    /**
     * This method is used to get the {@link MessageEvent}s id
     *
     * @return the {@link MessageEvent}s id
     */
    public int getId() {
        return id;
    }

    /**
     * This method is used to get the {@link MessageEvent}s sender
     *
     * @return the {@link MessageEvent}s sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * This method is used to get the {@link MessageEvent}s message
     *
     * @return the {@link MessageEvent}s message
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method is used to dispatch a {@link MessageEvent}
     *
     * @param listener the {@link GameEventListener} to parse with the dispatch
     */
    @Override
    public void dispatch(final GameEventListener listener) {
        if (listener instanceof MessageListener)
            ((MessageListener) listener).messageReceived(this);
    }

    public static void create(final GameDefinition definition, final int id, final String sender, final String message) {
        new MessageEvent(definition, id, sender, message);
    }

}
