package org.octobot.bot.game.script.loader;

import org.octobot.bot.game.script.GameScript;
import org.octobot.bot.game.script.ScriptCategory;

/**
 * ScriptSource
 *
 * @author Pat-ji
 */
public interface ScriptSource {

    public GameScript getScript();

    public String name();

    public String description();

    public double version();

    public String authors();

    public ScriptCategory category();

}
