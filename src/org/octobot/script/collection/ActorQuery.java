package org.octobot.script.collection;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Actor;

/**
 * ActorQuery
 *
 * @author pat-ji
 */
@SuppressWarnings("unchecked")
public abstract class ActorQuery<T extends Actor, E extends ActorQuery> extends SceneNodeQuery<T, E> {

    public ActorQuery(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to add a height {@link Filter} to the {@link Query}
     *
     * @param heights the heights to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for heights
     */
    public E height(final int... heights) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return context().calculations.arrayContains(heights, t.getHeight());
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a animation {@link Filter} to the {@link Query}
     *
     * @param animations the animations to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for animations
     */
    public E animation(final int... animations) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return context().calculations.arrayContains(animations, t.getAnimation());
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a name {@link Filter} to the {@link Query}
     *
     * @param names the names to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for names
     */
    public E name(final String... names) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return context().calculations.arrayContains(names, t.getName());
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a interacting {@link Filter} to the {@link Query}
     *
     * @param indexes the interacting indexes to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for interacting indexes
     */
    public E interacting(final int... indexes) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return context().calculations.arrayContains(indexes, t.getInteractingIndex());
            }
        });
        return (E) this;
    }

    /**
     * This method is used to add a actor {@link Filter} to the {@link Query}
     *
     * @param actor the actor to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for actor
     */
    public E interacting(final Actor actor) {
        filter(new Filter<T>() {
            @Override
            public boolean accept(final T t) {
                return actor.equals(t.getInteracting());
            }
        });
        return (E) this;
    }

}
