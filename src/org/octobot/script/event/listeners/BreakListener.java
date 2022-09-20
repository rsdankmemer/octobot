package org.octobot.script.event.listeners;

import org.octobot.script.event.BreakEvent;

/**
 * BreakListener
 *
 * @author Pat-ji
 */
public interface BreakListener extends GameEventListener {

    /**
     * This method is called when a {@link BreakEvent} happens
     *
     * @param event the {@link BreakEvent} that happened
     */
    public void onBreak(final BreakEvent event);

}
