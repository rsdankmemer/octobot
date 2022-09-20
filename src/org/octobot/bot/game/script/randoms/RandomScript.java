package org.octobot.bot.game.script.randoms;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.script.GameScript;
import org.octobot.bot.handler.RandomHandler;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;

/**
 * RandomScript
 *
 * @author Pat-ji
 */
public abstract class RandomScript extends GameScript implements Condition {
    protected GameDefinition definition;

    @Override
    public boolean start() {
        return true; // not used
    }

    public void initialize(final GameDefinition definition) {
        this.definition = definition;
    }

    protected RandomHandler handler() {
        return definition.randomHandler;
    }

    public abstract RandomEvent.Event getEvent();

}
