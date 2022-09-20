package org.octobot.bot.game.script;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskScript
 *
 * @author Pat-ji
 */
public abstract class TaskScript extends GameScript {
    private final List<Task> tasks = new ArrayList<Task>();

    private Task active;
    private boolean activePriority;

    /**
     * This method is used to get all the {@link Task}s in the {@link TaskScript}
     *
     * @return all the {@link Task}s in the {@link TaskScript}
     */
    public final List<Task> getTasks() {
        return tasks;
    }

    /**
     * This method is used to get the active {@link Task} in the {@link TaskScript}
     *
     * @return the active {@link Task} in the {@link TaskScript}
     */
    public final Task getActive() {
        return active;
    }

    /**
     * This method is used to set active priority.
     * This means that the active {@link Task} keeps executing until it is no longer validate
     *
     * @param enabled <code>true</code> if the {@link TaskScript} will use active priority
     */
    public final void setActivePriority(final boolean enabled) {
        activePriority = enabled;
    }

    /**
     * This method is used to provide a {@link Task} for the {@link TaskScript} to use
     *
     * @param task the {@link Task} to provide
     */
    public final void provide(final Task task) {
        addGameEventListener(task);
        tasks.add(task);
    }

    /**
     * This method is used to execute the {@link Task}s in the {@link TaskScript}
     *
     * @return the time the executor will sleep after executing the {@link Task}
     */
    @Override
    public int execute() {
        if (activePriority)
            if (active != null && active.validate())
                return active.execute();

        for (final Task task : tasks)
            if (task.validate()) {
                if (!task.equals(active)) {
                    if (active != null)
                        active.onSwitch(SwitchState.STOP);

                    active = task;
                    active.onSwitch(SwitchState.START);
                }

                return active.execute();
            }

        return 100;
    }

}
