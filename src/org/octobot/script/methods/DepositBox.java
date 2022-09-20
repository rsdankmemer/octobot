package org.octobot.script.methods;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DepositBox
 *
 * @author Pat-ji
 */
public class DepositBox extends ContextProvider {
    private static final String[] DEPOSIT_BOX_NAMES = { "Deposit Box", "Bank Deposit Box" };

    public DepositBox(final ScriptContext context) {
        super(context);
    }

    /**
     * Amount
     *
     * @author Pat-ji
     */
    public enum Amount {
        ALL(0), ONE(1), FIVE(5), TEN(10);

        private final int amount;

        private Amount(final int amount) {
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

    }

    /**
     * This method is used to check if the {@link DepositBox} is open
     *
     * @return <code>true</code> if the {@link DepositBox} is open
     */
    public boolean isOpen() {
        return context().widgets.getComponent(11, 62) != null;
    }

    /**
     * This method is used to open the {@link DepositBox}
     *
     * @return <code>true</code> if the {@link DepositBox} has been successfully opened
     */
    public boolean open() {
        final GameObject object = context().objects.getNearest(GameObjects.INTERACTABLE_OBJECT, DEPOSIT_BOX_NAMES);
        if (object != null)
            if (object.isOnGameScreen()) {
                if (object.interact("Deposit", object.getName()))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return !isOpen();
                        }
                    }, 3000);
            } else if (context().movement.walkTileOnMap(object)) {
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return !object.isOnGameScreen();
                    }
                }, 6000);
            }

        return isOpen();
    }

    /**
     * This method is used to close the {@link DepositBox}
     *
     * @return <code>true</code> if the {@link DepositBox} has been successfully closed
     */
    public boolean close() {
        if (!isOpen()) return true;

        final Component component = context().widgets.getComponent(11, 62);
        if (component != null && component.click(true))
            Time.sleep(new Condition() {
                @Override
                public boolean validate() {
                    return isOpen();
                }
            }, 1000);

        return !isOpen();
    }

    /**
     * This method is used to check if the {@link DepositBox} contains an {@link Item} with a given id
     *
     * @param ids the ids of {@link Item} to check
     * @return <code>true</code> if the {@link DepositBox} contains an {@link Item} with a given id
     */
    public boolean contains(final int... ids) {
        return getItem(ids) != null;
    }

    /**
     * This method is used to check if the {@link DepositBox} contains all {@link Item}s with given ids
     *
     * @param ids the ids of {@link Item} to check
     * @return <code>true</code> if the {@link DepositBox} contains all {@link Item}s with given ids
     */
    public boolean containsAll(final int... ids) {
        for (final int id : ids)
            if (getItem(id) == null) return false;

        return true;
    }

    /**
     * This method is used to check if the {@link DepositBox} contains an {@link Item} with a given name
     *
     * @param names the names of {@link Item} to check
     * @return <code>true</code> if the {@link DepositBox} contains an {@link Item} with a given name
     */
    public boolean contains(final String... names) {
        return getItem(names) != null;
    }

    /**
     * This method is used to check if the {@link DepositBox} contains all {@link Item}s with given names
     *
     * @param names the names of {@link Item} to check
     * @return <code>true</code> if the {@link DepositBox} contains all {@link Item}s with given names
     */
    public boolean containsAll(final String... names) {
        for (final String name : names)
            if (getItem(name) == null) return false;

        return true;
    }

    /**
     * This method is used to get the count of all the {@link Item}s in the {@link DepositBox}
     *
     * @return the count of all the {@link Item}s in the {@link DepositBox}
     */
    public int getCount() {
        final Item[] items = getItems();
        return items != null ? items.length : 0;
    }

    /**
     * This method checks if the {@link DepositBox} is empty
     *
     * @return <code>true</code> if the {@link DepositBox} is empty
     */
    public boolean isEmpty() {
        return getCount() == 0;
    }

    /**
     * This method checks if the {@link DepositBox} is full
     *
     * @return <code>true</code> if the {@link DepositBox} is full
     */
    public boolean isFull() {
        return getCount() == 28;
    }

    /**
     * This method is used to get the count of an {@link Item} with a specific id
     *
     * @param id the id of the {@link Item} to check
     * @param stack <code>true</code> if this is a stacked {@link Item}
     * @return the count of an {@link Item} with a specific id
     */
    public int getCount(final int id, final boolean stack) {
        int count = 0;
        for (final Item item : getItems())
            if (item != null && id == item.getId())
                count += stack ? item.getStackSize() : 1;

        return count;
    }

    /**
     * This method is used to get the count of an {@link Item} with a specific name
     *
     * @param name the name of the {@link Item} to check
     * @param stack <code>true</code> if this is a stacked {@link Item}
     * @return the count of an {@link Item} with a specific name
     */
    public int getCount(final String name, final boolean stack) {
        int count = 0;
        for (final Item item : getItems())
            if (item != null && name.equals(item.getName()))
                count += stack ? item.getStackSize() : 1;

        return count;
    }

    /**
     * This method is used to get the {@link Item}s in the {@link DepositBox} with specific ids
     *
     * @param ids the ids of the {@link Item}s to get
     * @return the {@link Item}s in the {@link DepositBox} with specific ids
     */
    public Item getItem(final int... ids) {
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(ids, item.getId());
            }
        });
    }

    /**
     * This method is used to get the {@link DepositBox}s in the {@link Bank} with specific names
     *
     * @param names the names of the {@link Item}s to get
     * @return the {@link Item}s in the {@link DepositBox} with specific names
     */
    public Item getItem(final String... names) {
        return getItem(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(names, item.getName());
            }
        });
    }

    /**
     * This method is used to get an {@link Item} in the {@link DepositBox} with a specific {@link Filter}
     *
     * @param filter the {@link Filter} of the {@link Item} to get
     * @return an {@link Item} in the {@link DepositBox} with a specific {@link Filter}
     */
    public Item getItem(final Filter<Item> filter) {
        for (final Item item : getItems())
            if (item != null && filter.accept(item)) return item;

        return null;
    }

    /**
     * This method is used to get all the {@link Item}s in the {@link DepositBox}
     *
     * @return all the {@link Item}s in the {@link DepositBox}
     */
    public Item[] getItems() {
        final Component component = context().widgets.getComponent(11, 61);
        return component != null ? component.getWidgetItems() : null;
    }

    /**
     * This method is used to check if the amount {@link Component} is visible
     *
     * @return <code>true</code> if the amount {@link Component} is visible
     */
    public boolean isAmountWidgetVisible() {
        final Component component = context().widgets.getComponent(548, 117);
        return component != null && !component.isHidden();
    }

    /**
     * This method is used to deposit all the {@link Item}s from the {@link DepositBox}
     */
    public void depositAll() {
        depositAllExcept(0);
    }

    /**
     * This method is used to deposit all {@link DepositBox} {@link Item}s except some selected by ids
     *
     * @param ids the ids of the {@link Item}s to ignore
     */
    public void depositAllExcept(final int... ids) {
        depositAllExcept(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(ids, item.getId());
            }
        });
    }

    /**
     * This method is used to deposit all {@link DepositBox} {@link Item}s except some selected by names
     *
     * @param names the names of the {@link Item}s to ignore
     */
    public void depositAllExcept(final String... names) {
        depositAllExcept(new Filter<Item>() {
            @Override
            public boolean accept(final Item item) {
                return context().calculations.arrayContains(names, item.getName());
            }
        });
    }

    /**
     * This method is used to deposit all {@link DepositBox} {@link Item}s except some selected by a {@link Filter}
     *
     * @param filter the {@link Filter} of the {@link Item}s to ignore
     */
    public void depositAllExcept(final Filter<Item> filter) {
        final List<Integer> items = new ArrayList<Integer>();
        for (final Item item : getItems()) {
            int id;
            if (item != null && !filter.accept(item) && !items.contains((id = item.getId())))
                items.add(id);
        }

        int count = getCount();
        out: for (final int id : items) {
            for (int tries = 0; tries < 5; tries++) {
                deposit(getItem(id), Amount.ALL);
                Time.sleep(500, 700);
                final int current = getCount();
                if (current < count) {
                    count = current;
                    continue out;
                }
            }
        }
    }

    /**
     * This method is used to deposit a selected {@link Amount} of an {@link Item}
     *
     * @param name the name of the {@link Item} to deposit
     * @param amount the {@link Amount} to deposit
     * @return <code>true</code> if successfully deposited the {@link Item}
     */
    public boolean deposit(final String name, final Amount amount) {
        return deposit(name, amount.getAmount());
    }

    /**
     * This method is used to deposit a selected amount of an {@link Item}
     *
     * @param name the name of the {@link Item} to deposit
     * @param amount the amount to deposit
     * @return <code>true</code> if successfully deposited the {@link Item}
     */
    public boolean deposit(final String name, final int amount) {
        return deposit(getItem(name), amount);
    }

    /**
     * This method is used to deposit a selected {@link Amount} of an {@link Item}
     *
     * @param id the id of the {@link Item} to deposit
     * @param amount the {@link Amount} to deposit
     * @return <code>true</code> if successfully deposited the {@link Item}
     */
    public boolean deposit(final int id, final Amount amount) {
        return deposit(id, amount.getAmount());
    }

    /**
     * This method is used to deposit a selected amount of an {@link Item}
     *
     * @param id the id of the {@link Item} to deposit
     * @param amount the amount to deposit
     * @return <code>true</code> if successfully deposited the {@link Item}
     */
    public boolean deposit(final int id, final int amount) {
        return deposit(getItem(id), amount);
    }

    /**
     * This method is used to deposit a selected {@link Amount} of an {@link Item}
     *
     * @param item the {@link Item} to deposit
     * @param amount the {@link Amount} to deposit
     * @return <code>true</code> if successfully deposited the {@link Item}
     */
    public boolean deposit(final Item item, final Amount amount) {
        return deposit(item, amount.getAmount());
    }

    /**
     * This method is used to deposit a selected amount of an {@link Item}
     *
     * @param item the {@link Item} to deposit
     * @param amount the amount to deposit
     * @return <code>true</code> if successfully deposited the {@link Item}
     */
    public boolean deposit(final Item item, final int amount) {
        if (item != null) {
            switch (amount) {
                case 0:
                    return item.interact("Deposit All");
                case 1:
                    return item.interact("Deposit 1");
                case 5:
                    return item.interact("Deposit 5");
                case 10:
                    return item.interact("Deposit 10");
                default:
                    if (isAmountWidgetVisible()) {
                        return context().keyboard.sendText("" + amount, true);
                    } else if (item.hover()) {
                        if (!context().menu.contains("Deposit " + amount)) {
                            if (item.interact("Deposit X")) {
                                Time.sleep(new Condition() {
                                    @Override
                                    public boolean validate() {
                                        return !isAmountWidgetVisible();
                                    }
                                }, 2000);

                                Time.sleep(200, 400);
                            }

                            return deposit(item, amount);
                        }
                    }

                    return item.interact("Deposit-" + amount);
            }
        }

        return false;
    }

}
