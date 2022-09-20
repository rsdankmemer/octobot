package org.octobot.bot.handler;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.script.randoms.RandomScript;
import org.octobot.script.Condition;
import org.octobot.script.Executable;
import org.octobot.script.event.BreakEvent;
import org.octobot.script.methods.Game;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * BreakHandler
 *
 * @author Pat-ji
 */
public class BreakHandler implements Condition, Executable {
    private final GameDefinition definition;
    public final List<BreakEvent> breakEvents;

    private BreakEvent event;
    private long breakTime = -1, scripTime = -1;

    public BreakHandler(final GameDefinition definition) {
        this.definition = definition;

        breakEvents = new ArrayList<BreakEvent>();
    }

    private RandomScript getLoginHandler() {
        return definition.randomHandler.getRandom("Login");
    }

    @Override
    public boolean validate() {
        if (event != null) return true;

        for (final BreakEvent event : breakEvents)
            if (event != null && canStart(event)) {
                this.event = event;
                definition.eventQueue.dispatchEvent(event);
                return true;
            }

        return false;
    }

    public void reset() {
        event = null;
        breakTime = -1;
        setup();
    }

    public void setup() {
        scripTime = System.currentTimeMillis();
    }

    private int toMillis(final int time) {
        return time * 1000 * 60;
    }

    private boolean canStart(final BreakEvent event) {
        return scripTime != -1 && System.currentTimeMillis() > scripTime + toMillis(event.getScriptTime() + Random.nextInt(0, event.getBreakDeviation()));
    }

    private boolean isCompleted() {
        return System.currentTimeMillis() > breakTime;
    }

    @Override
    public int execute() {
        while (event != null) {
            if (breakTime == -1)
                breakTime = System.currentTimeMillis() + toMillis(event.getBreakTime() + Random.nextInt(0, event.getBreakDeviation()));

            if (isCompleted()) {
                if (definition.context.game.getGameState() == Game.State.LOGGED_IN) {
                    reset();
                    return 100;
                } else {
                    Time.sleep(getLoginHandler().execute());
                }
            } else {
                if (definition.context.game.isLoggedIn()) {
                    definition.context.game.logout();
                } else {
                    Time.sleep(1000);
                }
            }
        }

        return 100;
    }

}
