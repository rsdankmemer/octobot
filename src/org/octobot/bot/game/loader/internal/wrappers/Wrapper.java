package org.octobot.bot.game.loader.internal.wrappers;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.loader.internal.GameContext;
import org.octobot.bot.game.loader.internal.definition.ClassDefinition;
import org.octobot.bot.game.loader.internal.definition.GetterDefinition;

import java.lang.reflect.Field;

/**
 * Wrapper
 *
 * @author Pat-ji
 */
public class Wrapper<E> {
    protected final GameDefinition definition;

    public Wrapper(final GameDefinition definition) {
        this.definition = definition;
    }

    protected Object getObject(final Class<?> accessor, final String className, final String fieldName) {
        try {
            return getField(accessor, className,fieldName).get(accessor);
        } catch (final IllegalAccessException ignored) { }

        return null;
    }

    protected Field getField(Class<?> accessor, final String className, final String fieldName) {
        try {
            final ClassDefinition classDefinition = GameContext.CLASSES.get(className);
            final GetterDefinition getterDefinition = classDefinition.getters.get(fieldName);

            if (accessor == null)
                accessor = definition.classLoader.loadClass(classDefinition.className);

            final Field field = accessor.getDeclaredField(getterDefinition.fieldName);
            field.setAccessible(true);
            return field;
        } catch (final Exception ignored) { }

        return null;
    }

    protected int getInt(Class<?> accessor, final String className, final String fieldName) {
        try {
            final ClassDefinition classDefinition = GameContext.CLASSES.get(className);
            final GetterDefinition getterDefinition = classDefinition.getters.get(fieldName);

            if (accessor == null)
                accessor = definition.classLoader.loadClass(classDefinition.className);

            final Field field = accessor.getDeclaredField(getterDefinition.fieldName);
            field.setAccessible(true);
            return field.getInt(accessor) * (Integer) getterDefinition.multiplier;
        } catch (final Exception ignored) { }

        return -1;
    }

    protected long getLong(Class<?> accessor, final String className, final String fieldName) {
        try {
            final ClassDefinition classDefinition = GameContext.CLASSES.get(className);
            final GetterDefinition getterDefinition = classDefinition.getters.get(fieldName);

            if (accessor == null)
                accessor = definition.classLoader.loadClass(classDefinition.className);

            final Field field = accessor.getDeclaredField(getterDefinition.fieldName);
            field.setAccessible(true);
            return field.getLong(accessor) * (Long) getterDefinition.multiplier;
        } catch (final Exception ignored) { }

        return -1;
    }

}
