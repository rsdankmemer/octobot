package org.octobot.bot.game.loader.internal.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassDefinition
 *
 * @author Pat-ji
 */
public class ClassDefinition {
    public final String className;
    public final Map<String, GetterDefinition> getters;

    public ClassDefinition(final String className) {
        this.className = className;

        getters = new HashMap<String, GetterDefinition>();
    }

}
