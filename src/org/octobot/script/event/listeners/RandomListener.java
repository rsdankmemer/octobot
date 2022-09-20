package org.octobot.script.event.listeners;

import org.octobot.script.event.RandomEvent;

/**
 * RandomListener
 *
 * @author Pat-ji
 */
public interface RandomListener extends GameEventListener {

    /**
     * This method is called at the start of a {@link RandomEvent}
     *
     * @param event the {@link RandomEvent} to start
     */
    public void onRandomStart(final RandomEvent event);

    /**
     * This method is called at the finish of a {@link RandomEvent}
     *
     * @param event the {@link RandomEvent} that finished
     */
    public void onRandomFinish(final RandomEvent event);

}
