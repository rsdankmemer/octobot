package org.octobot.script.collection;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.SceneNode;

/**
 * SceneNodeQuery
 *
 * @author Pat-ji
 */
@SuppressWarnings("unchecked")
public abstract class SceneNodeQuery<T extends SceneNode, E extends SceneNodeQuery> extends LocatableQuery<T, E> {

    public SceneNodeQuery(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to add a validation {@link Filter} to this {@link Query}
     *
     * @return the same {@link Query} with an extra {@link Filter} for validation
     */
    public E valid() {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return t.isValid();
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a on screen {@link Filter} to this {@link Query}
     *
     * @return the same {@link Query} with an extra {@link Filter} for an on screen check
     */
    public E onScreen() {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return t.isOnScreen();
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a on game screen {@link Filter} to this {@link Query}
     *
     * @return the same {@link Query} with an extra {@link Filter} for an on game screen check
     */
    public E onGameScreen() {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return t.isOnGameScreen();
            }
        });
        return (E) this;
    }

}
