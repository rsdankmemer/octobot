package org.octobot.script;

/**
 * Executable
 *
 * @author Pat-ji
 */
public interface Executable {

    /**
     * This method is used to execute an {@link Executable} for example a {@link org.octobot.bot.game.script.GameScript} or a Random
     *
     * @return the execute of this {@link Executable}
     */
    public int execute();

}
