package org.octobot.script.collection;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.GroundItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * GroundItemQuery
 *
 * @author Pat-ji
 */
public class GroundItemQuery extends SceneNodeQuery<GroundItem, GroundItemQuery> {

    public GroundItemQuery(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to add a height {@link Filter} to the {@link Query}
     *
     * @param heights the heights to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for heights
     */
    public GroundItemQuery height(final int... heights) {
        filter(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem t) {
                return context().calculations.arrayContains(heights, t.getHeight());
            }
        });
        return this;
    }

    /**
     * This method is used to add a name {@link Filter} to the {@link Query}
     *
     * @param names the names to apply the {@link Filter} to
     * @return the same {@link Query} with an extra {@link Filter} for names
     */
    public GroundItemQuery name(final String... names) {
        filter(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                return context().calculations.arrayContains(names, item.getName());
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
    public GroundItemQuery ids(final int... ids) {
        filter(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                return context().calculations.arrayContains(ids, item.getId());
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
    public GroundItemQuery action(final String action) {
        filter(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                return context().calculations.arrayContains(item.getGroundActions(), action);
            }
        });
        return this;
    }

    /**
     * This method is used to get all the {@link GroundItem}s accepted by this {@link Query}
     *
     * @return all the {@link GroundItem}s accepted by this {@link Query}
     */
    @Override
    public GroundItem[] all() {
        final Filter<GroundItem> filter = new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                for (final Filter<GroundItem> filter : filters)
                    if (!filter.accept(item)) return false;

                return true;
            }
        };

        final List<GroundItem> list = new ArrayList<GroundItem>();
        Collections.addAll(list, context().groundItems.getLoaded(filter));
        if (list.size() > 0 && comparators.size() > 0)
            for (final Comparator<GroundItem> comparator : comparators)
                Collections.sort(list, comparator);

        return list.toArray(new GroundItem[list.size()]);
    }

}
