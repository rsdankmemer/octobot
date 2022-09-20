package org.octobot.script.collection;

import org.octobot.script.ScriptContext;
import org.octobot.script.methods.GameObjects;
import org.octobot.script.wrappers.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * GameObjectQuery
 *
 * @author Pat-ji
 */
public class GameObjectQuery extends SceneNodeQuery<GameObject, GameObjectQuery> {
    private int mask;

    public GameObjectQuery(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to add a mask to the {@link Query}
     *
     * @param mask the mask to add to the {@link Query}
     * @return the same {@link Query} with an extra {@link Filter} for mask
     */
    public GameObjectQuery mask(final int mask) {
        this.mask += mask;
        return this;
    }

    /**
     * This method is used to add a name {@link Filter} to the {@link Query}
     *
     * @param names the names to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for names
     */
    public GameObjectQuery name(final String... names) {
        filter(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                return context().calculations.arrayContains(names, object.getName());
            }
        });
        return this;
    }

    /**
     * This method is used to add a id {@link Filter} to the {@link Query}
     *
     * @param ids the ids to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for ids
     */
    public GameObjectQuery ids(final int... ids) {
        filter(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                return context().calculations.arrayContains(ids, object.getId());
            }
        });
        return this;
    }

    /**
     * This method is used to add a action {@link Filter} to the {@link Query}
     *
     * @param action the action to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for the action
     */
    public GameObjectQuery action(final String action) {
        filter(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                return context().calculations.arrayContains(object.getActions(), action);
            }
        });
        return this;
    }

    /**
     * This method is used to get all the {@link GameObject}s accepted by this {@link Query}
     *
     * @return all the {@link GameObject}s accepted by this {@link Query}
     */
    @Override
    public GameObject[] all() {
        final Filter<GameObject> filter = new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                for (final Filter<GameObject> filter : filters)
                    if (!filter.accept(object)) return false;

                return true;
            }
        };

        final List<GameObject> list = new ArrayList<GameObject>();
        Collections.addAll(list, context().objects.getLoaded(filter, mask == 0 ? GameObjects.ALL : mask));
        if (list.size() > 0 && comparators.size() > 0)
            for (final Comparator<GameObject> comparator : comparators)
                Collections.sort(list, comparator);

        return list.toArray(new GameObject[list.size()]);
    }

}
