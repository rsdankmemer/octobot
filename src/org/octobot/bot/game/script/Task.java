package org.octobot.bot.game.script;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.Executable;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.listeners.GameEventListener;

/**
 * Task
 *
 * @author Pat-ji
 */
public abstract class Task extends ContextProvider implements GameEventListener, Condition, Executable {

    public Task(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is called on a switch of {@link Task} in the {@link TaskScript}
     *
     * @param state the {@link SwitchState} of the call
     */
    public void onSwitch(final SwitchState state) {
    }

}
