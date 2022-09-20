package org.octobot.script.methods;

import org.octobot.script.*;
import org.octobot.script.collection.Filter;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;

/**
 * Inventory
 *
 * @author Pat-ji
 */
public class Inventory extends ContextProvider {
    public static final int[] DROP_INDEXES = {
            0, 4, 8, 12, 16, 20, 24,
            1, 5, 9, 13, 17, 21, 25,
            2, 6, 10, 14, 18, 22, 26,
            3, 7, 11, 15, 19, 23, 27
    };

    public Inventory(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to see if there is an {@link Item} selected in the {@link Inventory}
     *
     * @return <code>true</code> if there is an {@link Item} selected in the {@link Inventory}
     */
    public boolean hasItemSelected() {
        return context().client.getItemSelected() == 1;
    }

    /**
     * This method is used to get the selected {@link Item}
     *
     * @return the selected {@link Item}
     */
    public Item getSelectedItem() {
        if (!hasItemSelected()) return null;

        final Component component = context().widgets.getComponent(149, 0);
        return component != null ? new Item(context(), context().client.getSelectedItemIndex(), component) : null;
    }

    /**
     * This method is used to get all the {@link Item}s in the {@link Inventory}
     *
     * @return an array with all the {@link Item}s in the {@link Inventory}
     */
    public Item[] getItems() {
        final Component component = context().widgets.getComponent(149, 0);
        return component != null ? component.getWidgetItems() : null;
    }

    /**
     * This method is used to check if the {@link Inventory} contains a requested {@link Item}
     *
     * @param identifiables the identifiables to look for
     * @return <code>true</code> if the {@link Inventory} contains a requested {@link Item}
     */
    public boolean contains(final Identifiable... identifiables) {
        for (final Identifiable identifiable : identifiables)
            if (contains(identifiable.getId())) return true;

        return true;
    }

    /**
     * This method is used to check if the {@link Inventory} contains a requested {@link Item}
     *
     * @param ids the ids to check for
     * @return <code>true</code> if the {@link Inventory} contains a requested {@link Item}
     */
    public boolean contains(final int... ids) {
        return getItem(ids) != null;
    }

    /**
     * This method is used to check if the {@link Inventory} contains all requested {@link Item}s
     *
     * @param identifiables the identifiables to look for
     * @return <code>true</code> if the {@link Inventory} contains all requested {@link Item}s
     */
    public boolean containsAll(final Identifiable... identifiables) {
        for (final Identifiable identifiable : identifiables)
            if (getItem(identifiable) == null) return false;

        return true;
    }

    /**
     * This method is used to check if the {@link Inventory} contains all requested {@link Item}s
     *
     * @param ids the ids to look for
     * @return <code>true</code> if the {@link Inventory} contains all requested {@link Item}s
     */
    public boolean containsAll(final int... ids) {
        for (final int id : ids)
            if (getItem(id) == null) return false;

        return true;
    }

    /**
     * This method is used to check if the {@link Inventory} contains a requested {@link Item}
     *
     * @param nameables the nameables to check for
     * @return <code>true</code> if the {@link Inventory} contains a requested {@link Item}
     */
    public boolean contains(final Nameable... nameables) {
        for (final Nameable nameable : nameables)
            if (contains(nameable.getName())) return true;

        return true;
    }

    /**
     * This method is used to check if the {@link Inventory} contains a requested {@link Item}
     *
     * @param names the names to check for
     * @return <code>true</code> if the {@link Inventory} contains a requested {@link Item}
     */
    public boolean contains(final String... names) {
        return getItem(names) != null;
    }

    /**
     * This method is used to check if the {@link Inventory} contains all requested {@link Item}s
     *
     * @param nameables the nameables to look for
     * @return <code>true</code> if the {@link Inventory} contains all requested {@link Item}s
     */
    public boolean containsAll(final Nameable... nameables) {
        for (final Nameable nameable : nameables)
            if (getItem(nameable) == null) return false;

        return true;
    }

    /**
     * This method is used to check if the {@link Inventory} contains all requested {@link Item}s
     *
     * @param names the names to look for
     * @return <code>true</code> if the {@link Inventory} contains all requested {@link Item}s
     */
    public boolean containsAll(final String... names) {
        for (final String name : names)
            if (getItem(name) == null) return false;

        return true;
    }

    /**
     * This method is used to get an {@link Item} at a specific index
     *
     * @param index the index to get the {@link Item} at
     * @return an {@link Item} at a specific index
     */
    public Item getItemAt(final int index) {
        final Component component = context().widgets.getComponent(149, 0);
        return component != null ? new Item(context(), index, component) : null;
    }

    /**
     * This method is used to get an {@link Item}
     *
     * @param identifiables the identifiables to look for
     * @return an {@link Item} with a specific id
     */
    public Item getItem(final Identifiable... identifiables) {
        if (identifiables == null) return null;

        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                final int id = item.getId();
                for (final Identifiable identifiable : identifiables)
                    if (id == identifiable.getId()) return true;

                return false;
            }
        });
    }

    /**
     * This method is used to get an {@link Item}
     *
     * @param ids the ids to look for
     * @return an {@link Item} with a specific id
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
     * This method is used to get an {@link Item}
     *
     * @param nameables the nameables to look for
     * @return an {@link Item} with a specific id
     */
    public Item getItem(final Nameable... nameables) {
        if (nameables == null) return null;

        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                final String name = item.getName();
                for (final Nameable nameable : nameables)
                    if (nameable.getName().equals(name)) return true;

                return false;
            }
        });
    }

    /**
     * This method is used to get an {@link Item}
     *
     * @param names the names to look for
     * @return an {@link Item} with a specific id
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

    /**
     * This method is used to get an {@link Item}
     *
     * @param filter the {@link Filter} to use in the selection
     * @return an {@link Item} that is accepted by the {@link Filter}
     */
    public Item getItem(final Filter<Item> filter) {
        final Item[] items = getItems();
        if (items == null) return null;

        for (final Item item : items)
            if (item != null && filter.accept(item)) return item;

        return null;
    }

    /**
     * This method is used to get the current {@link Inventory} {@link Item} count
     *
     * @return the current {@link Inventory} {@link Item} count
     */
    public int getCount() {
        final Item[] items = getItems();
        return items != null ? items.length : 0;
    }

    /**
     * This method is used to check if the {@link Inventory} is full
     *
     * @return <code>true</code> if the {@link Inventory} is full
     */
    public boolean isFull() {
        return getCount() == 28;
    }

    /**
     * This method is used to check if the {@link Inventory} is empty
     *
     * @return <code>true</code> if the {@link Inventory} is empty
     */
    public boolean isEmpty() {
        return getCount() == 0;
    }

    /**
     * This method is used to get the count of a specific {@link Item}
     *
     * @param identifiable the id of the {@link Item} to get the count from
     * @param stack use <code>true</code> if this {@link Item} is in a stack
     * @return the count of a specific {@link Item}
     */
    public int getCount(final Identifiable identifiable, final boolean stack) {
        return getCount(identifiable.getId(), stack);
    }

    /**
     * This method is used to get the count of a specific {@link Item}
     *
     * @param id the id of the {@link Item} to get the count from
     * @param stack use <code>true</code> if this {@link Item} is in a stack
     * @return the count of a specific {@link Item}
     */
    public int getCount(final int id, final boolean stack) {
        final Item[] items = getItems();
        if (items == null) return 0;

        int count = 0;
        for (final Item item : items)
            if (item != null && id == item.getId())
                count += stack ? item.getStackSize() : 1;

        return count;
    }

    /**
     * This method is used to get the count of a specific {@link Item}
     *
     * @param nameable the name of the {@link Item} to get the count from
     * @param stack use <code>true</code> if this {@link Item} is in a stack
     * @return the count of a specific {@link Item}
     */
    public int getCount(final Nameable nameable, final boolean stack) {
        return getCount(nameable.getName(), stack);
    }

    /**
     * This method is used to get the count of a specific {@link Item}
     *
     * @param name the name of the {@link Item} to get the count from
     * @param stack use <code>true</code> if this {@link Item} is in a stack
     * @return the count of a specific {@link Item}
     */
    public int getCount(final String name, final boolean stack) {
        final Item[] items = getItems();
        if (items == null) return 0;

        int count = 0;
        for (final Item item : items)
            if (item != null && name.equals(item.getName()))
                count += stack ? item.getStackSize() : 1;

        return count;
    }

    /**
     * This method is used to get the count of all {@link Item}s
     *
     * @param stack use <code>true</code> to include {@link Item} stacks
     * @return the count of all {@link Item}s
     */
    public int getCount(final boolean stack) {
        final Item[] items = getItems();
        if (items == null) return 0;

        int count = 0;
        for (final Item item : items)
            if (item != null)
                count += stack ? item.getStackSize() : 1;

        return count;
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param id the id of the {@link Item} to use
     * @param targetId the id of the {@link Item} to use the {@link Item} on
     * @return <code>true</code> if the {@link Item} is successfully used on the other {@link Item}
     */
    public boolean useItemOn(final int id, final int targetId) {
        return useItemOn(id, getItem(targetId));
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param name the name of the {@link Item} to use
     * @param targetName the name of the {@link Item} to use the {@link Item} on
     * @return <code>true</code> if the {@link Item} is successfully used on the other {@link Item}
     */
    public boolean useItemOn(final String name, final String targetName) {
        return useItemOn(name, getItem(targetName), targetName);
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param id the id of the {@link Item} to use
     * @param target the {@link Interactable} to use the {@link Item} on
     * @return <code>true</code> if the {@link Item} is successfully used on the {@link Interactable}
     */
    public boolean useItemOn(final int id, final Interactable target) {
        return useItemOn(getItem(id), target);
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param id the id of the {@link Item} to use
     * @param target the {@link Interactable} to use the {@link Item} on
     * @param targetName the targets name
     * @return <code>true</code> if the {@link Item} is successfully used on the {@link Interactable}
     */
    public boolean useItemOn(final int id, final Interactable target, final String targetName) {
        return useItemOn(getItem(id), target, targetName);
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param name the name of the {@link Item} to use
     * @param target the {@link Interactable} to use the {@link Item} on
     * @return <code>true</code> if the {@link Item} is successfully used on the {@link Interactable}
     */
    public boolean useItemOn(final String name, final Interactable target) {
        return useItemOn(getItem(name), target);
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param name the name of the {@link Item} to use
     * @param target the {@link Interactable} to use the {@link Item} on
     * @param targetName the targets name
     * @return <code>true</code> if the {@link Item} is successfully used on the {@link Interactable}
     */
    public boolean useItemOn(final String name, final Interactable target, final String targetName) {
        return useItemOn(getItem(name), target, targetName);
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param item the item to use
     * @param target the {@link Interactable} to use the {@link Item} on
     * @return <code>true</code> if the {@link Item} is successfully used on the {@link Interactable}
     */
    public boolean useItemOn(final Item item, final Interactable target) {
        return useItemOn(item, target, "");
    }

    /**
     * This method is used to use an {@link Item} on an {@link Interactable}
     *
     * @param item the item to use
     * @param target the {@link Interactable} to use the {@link Item} on
     * @param targetName the targets name
     * @return <code>true</code> if the {@link Item} is successfully used on the {@link Interactable}
     */
    public boolean useItemOn(final Item item, final Interactable target, final String targetName) {
        if (item == null || target == null) return false;

        if (hasItemSelected()) {
            if (target.interact("Use", targetName)) {
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return hasItemSelected();
                    }
                }, 1500);

                return !hasItemSelected();
            }
        } else if (item.interact("Use")) {
            Time.sleep(new Condition() {
                @Override
                public boolean validate() {
                    return !hasItemSelected();
                }
            }, 1500);

            return useItemOn(item, target);
        }

        return false;
    }

    /**
     * This method is used to drop all {@link Item}s from the {@link Inventory}
     */
    public void dropAll() {
        dropAll(true);
    }

    /**
     * This method is used to drop all {@link Item}s from the {@link Inventory}
     *
     * @param vertical <code>true</code> if the dropping should be done vertical
     */
    public void dropAll(final boolean vertical) {
        dropAllExcept(vertical);
    }

    /**
     * This method is used to drop all {@link Item}s
     *
     * @param ids the ids of the {@link Item}s to ignore
     */
    public void dropAllExcept(final int... ids) {
        dropAllExcept(true, ids);
    }

    /**
     * This method is used to drop all the {@link Item}s
     *
     * @param vertical <code>true</code> if the dropping should be done vertical
     * @param ids the ids of the {@link Item}s to ignore
     */
    public void dropAllExcept(final boolean vertical, final int... ids) {
        final Item[] items = getItems();
        if (vertical) {
            for (final int index : DROP_INDEXES) {
                final Item item = index < items.length ? items[index] : null;
                if (item != null && !context().calculations.arrayContains(ids, item.getId()))
                    if (item.interact("Drop"))
                        Time.sleep(100, 200);
            }
        } else {
            for (final Item item : items) {
                if (item != null && !context().calculations.arrayContains(ids, item.getId()))
                    if (item.interact("Drop"))
                        Time.sleep(100, 200);
            }
        }
    }

    /**
     * This method is used to check if the {@link org.octobot.script.methods.Inventory} contains any other items then given
     *
     * @param ids the ids of the {@link org.octobot.script.wrappers.Item}s to ignore
     * @return <code>true</code> if the {@link org.octobot.script.methods.Inventory} contains any other items then given
     */
    public boolean hasAnythingExcept(final int... ids) {
        return getCountExcluding(ids) > 0;
    }

    /**
     * This method is used to get the count of the {@link org.octobot.script.wrappers.Item}s other then given
     *
     * @param excluding the ids of the {@link org.octobot.script.wrappers.Item}s to ignore
     * @return the count of the {@link org.octobot.script.wrappers.Item}s other then given
     */
    public int getCountExcluding(final int... excluding) {
        int count = 0;

        o: for (final Item item : getItems()) {
            if (item != null)
                for (final int id : excluding)
                    if (item.getId() == id) continue o;

            count++;
        }

        return count;
    }

}
