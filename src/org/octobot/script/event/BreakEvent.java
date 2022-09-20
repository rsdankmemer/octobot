package org.octobot.script.event;

import org.octobot.script.event.listeners.BreakListener;
import org.octobot.script.event.listeners.GameEventListener;

/**
 * BreakEvent
 *
 * @author Pat-ji
 */
public class BreakEvent extends GameEvent {
    private final int breakTime, breakDeviation;
    private final int scriptTime, scriptDeviation;

    public BreakEvent(final int breakTime, final int breakDeviation, final int scriptTime, final int scriptDeviation) {
        this.breakTime = breakTime;
        this.breakDeviation = breakDeviation;
        this.scriptTime = scriptTime;
        this.scriptDeviation = scriptDeviation;
    }

    /**
     * This method is used to get the {@link BreakEvent}s break time
     *
     * @return the {@link BreakEvent}s break time
     */
    public int getBreakTime() {
        return breakTime;
    }

    /**
     * This method is used to get the {@link BreakEvent}s break deviation
     *
     * @return the {@link BreakEvent}s break deviation
     */
    public int getBreakDeviation() {
        return breakDeviation;
    }

    /**
     * This method is used to get the {@link BreakEvent}s script time
     *
     * @return the {@link BreakEvent}s script time
     */
    public int getScriptTime() {
        return scriptTime;
    }

    /**
     * This method is used to get the {@link BreakEvent}s script deviation
     *
     * @return the {@link BreakEvent}s script deviation
     */
    public int getScriptDeviation() {
        return scriptDeviation;
    }

    /**
     * This method is used to dispatch a {@link BreakEvent}
     *
     * @param listener the {@link GameEventListener} to parse with the dispatch
     */
    @Override
    public void dispatch(final GameEventListener listener) {
        if (listener instanceof BreakListener)
            ((BreakListener) listener).onBreak(this);
    }

    /**
     * This method is used to get a {@link String} containing all the data of this {@link BreakEvent}
     *
     * @return a {@link String} containing all the data of this {@link BreakEvent}
     */
    @Override
    public String toString() {
        return String.format("[B:%s,BD:%s,S:%s,SD:%s]", breakTime, breakDeviation, scriptTime, scriptDeviation);
    }

}
