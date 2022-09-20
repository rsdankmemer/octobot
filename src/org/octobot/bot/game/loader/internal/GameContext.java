package org.octobot.bot.game.loader.internal;

import org.octobot.bot.game.loader.internal.definition.ClassDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * GameContext
 *
 * @author Pat-ji
 */
public class GameContext {
    public static final Map<String, ClassDefinition> CLASSES;

    static {
        CLASSES = new HashMap<String, ClassDefinition>();
    }

}
