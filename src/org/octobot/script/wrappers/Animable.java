package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSAnimable;
import org.octobot.script.ScriptContext;

/**
 * Animable
 *
 * @author Pat-ji
 */
public abstract class Animable<T extends RSAnimable> extends SceneNode<T> implements RSAnimable {

    public Animable(final ScriptContext context, final T accessor) {
        super(context, accessor);
    }

	/**
	 * This method is used to get the height of the {@link Animable}
	 *
	 * @return the {@link Animable}'s height
	 */
    @Override
    public int getHeight() {
        return getAccessor() != null ? getAccessor().getHeight() : -1;
    }

    public Model getModelInstance() {
        return getAccessor() != null ? getAccessor().getModelInstance() : null;
    }

	/**
	 * This method is used to check if the {@link Animable} is equal to given object
	 *
	 * @param obj the object you want to compare the {@link Animable} to
	 * @return <code>true</code> if the {@link Animable} is equal to given object
	 */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Animable) {
            final Animable animable = (Animable) obj;
            return animable.getAccessor() == getAccessor();
        } else if (obj instanceof RSAnimable) {
            return obj == getAccessor();
        }

        return false;
    }

}
