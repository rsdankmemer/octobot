package org.octobot.script.event.listeners;

import org.octobot.script.event.MessageEvent;

/**
 * MessageListener
 *
 * @author Pat-ji
 */
public interface MessageListener extends GameEventListener {

    /**
     * This method is called when a {@link MessageEvent} happens
     *
     * @param event the {@link MessageEvent} that happened
     */
    public void messageReceived(final MessageEvent event);

}
