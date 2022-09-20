package org.octobot.script.methods;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;

/**
 * Shop
 *
 * @author Pat-ji
 */
public class Shop extends ContextProvider {

    public Shop(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to close the {@link Shop}
     *
     * @return <code>true</code> if the {@link Shop} is closed
     */
    public boolean close() {
        if (!isOpen()) return true;

        final Component component = context().widgets.getComponent(300, 91);
        if (component != null && component.click(true))
            Time.sleep(new Condition() {
                @Override
                public boolean validate() {
                    return isOpen();
                }
            }, 1500);

        return !isOpen();
    }

    /**
     * This method is used to get all the {@link Item}s in the {@link Shop}
     *
     * @return an array with all the {@link Item}s in the {@link Shop}
     */
    public Item[] getItems() {
        final Component component = context().widgets.getComponent(300, 75);
        return component != null ? component.getWidgetItems() : new Item[0];
    }

    /**
     * This method is used to check if the {@link Shop} is open
     *
     * @return <code>true</code> if the {@link Shop} is open
     */
    public boolean isOpen() {
        final Component component = context().widgets.getComponent(300, 75);
        return component != null && component.isVisible();
    }

    /**
     * This method is used to get an {@link Item} from the {@link Shop}
     *
     * @param filter the {@link Filter} to use in the loading
     * @return an {@link Item} selected by the {@link Filter}
     */
    public Item getItem(final Filter<Item> filter) {
        for (final Item item : getItems())
            if (item != null && filter.accept(item)) return item;

        return null;
    }

    /**
     * This method is used to get an {@link Item} from the {@link Shop}
     *
     * @param ids the id of the {@link Item}s to get
     * @return a {@link Item} with a specific id
     */
    public Item getItem(final int... ids) {
        if (ids == null) return null;

        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(ids, item.getId());
            }
        });
    }

    /**
     * This method is used to get an {@link Item} from the {@link Shop}
     *
     * @param names the name of the {@link Item}s to get
     * @return a {@link Item} with a specific name
     */
    public Item getItem(final String... names) {
        if (names == null) return null;

        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(names, item.getName());
            }
        });
    }

}
