package org.octobot.bot.game.loader.internal.wrappers.input;

import org.octobot.bot.GameDefinition;

/**
 * Input
 *
 * @author Pat-ji
 */
public interface Input {
    public WrapperContext context = new WrapperContext();

    /**
     * WrapperContext
     *
     * @author Pat-ji
     */
    public final class WrapperContext {

        public GameDefinition getBot(final Object object) {
            final ClassLoader classLoader = object.getClass().getClassLoader();
            for (final GameDefinition definition : GameDefinition.definitions)
                if (definition.client != null && classLoader.equals(definition.client.getClass().getClassLoader()))
                    return definition;

            return null;
        }

    }

}
