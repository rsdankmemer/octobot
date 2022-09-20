package org.octobot.script.methods;

import org.octobot.script.*;
import org.octobot.script.collection.Filter;
import org.octobot.script.util.Time;
import org.octobot.script.util.Timer;
import org.octobot.script.wrappers.*;
import org.octobot.script.wrappers.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bank
 *
 * @author Pat-ji
 */
public class Bank extends ContextProvider {
    private static final String[] NPC_BANKERS = { "Banker", "Emerald Benedict" };
    private static final String[] BANK_BOOTHS = { "Bank booth" };
    private static final String[] BANK_CHESTS = { "Bank chest", "Open chest", "Closed chest" };

    public Bank(final ScriptContext context) {
        super(context);
    }

    /**
     * Amount
     *
     * @author Pat-ji
     */
    public enum Amount {
        ALL(0), ALL_BUT_ONE(-1), ONE(1), FIVE(5), TEN(10);

        private final int amount;

        private Amount(final int amount) {
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

    }

    /**
     * This method is used to check if the amount {@link Component} is visible
     *
     * @return <code>true</code> if the amount {@link Component} is visible
     */
    public boolean isAmountWidgetVisible() {
        final Component component = context().widgets.getComponent(548, 118);
        return component != null && !component.isHidden();
    }

    /**
     * This method is used to check if noted items is enabled
     *
     * @return <code>true</code> if noted items is enabled
     */
    public boolean isNotedEnabled() {
        return context().settings.get(115) == 1;
    }

    /**
     * This method is used to check if an {@link Item} is visible
     *
     * @param id the id of the {@link Item} to check
     * @return <code>true</code> if an {@link Item} with a specific id is visible
     */
    public boolean isVisible(final int id) {
        final Item item = getItem(id);
        return item != null && isVisible(item);
    }

    /**
     * This method is used to check if an {@link Item} is visible
     *
     * @param name the name of the {@link Item} to check
     * @return <code>true</code> if an {@link Item} with a specific id is name
     */
    public boolean isVisible(final String name) {
        final Item item = getItem(name);
        return item != null && isVisible(item);
    }

    /**
     * This method is used to check if an {@link Item} is visible
     *
     * @param item the {@link Item} to check
     * @return <code>true</code> if the item is visible
     */
    public boolean isVisible(final Item item) {
        final Point point = item.getCentralPoint();
        return point != null && point.y > 90 && point.y < 285;
    }
    /**
     * This method is used to check if the {@link Bank} is open
     *
     * @return <code>true</code> if the {@link Bank} is open
     */
    public boolean isOpen() {
        final Component component = context().widgets.getComponent(12, 6);
        return component != null && component.isVisible();
    }

    /**
     * Attempts to open the bank. If the bank is not on the screen, attempts to walk to the bank.
     *
     * @param node
     * @param action
     * @param name
     * @return
     */
    private boolean openBank(final SceneNode node, final String action, final String name) {
        if (node == null) return false;

        if (node.isOnGameScreen()) {
            if (node.interact(action, name)) {
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return !isOpen();
                    }
                }, 3000);

                return isOpen();
            }
        } else if (context().movement.walkTileOnMap(node)) {
            Time.sleep(new Condition() {
                @Override
                public boolean validate() {
                    return !node.isOnGameScreen();
                }
            }, 6000);
        }

        return false;
    }

    @Deprecated
    private boolean open(final SceneNode node, final String action, final String name) {
        if (node == null) return false;

        if (node.isOnGameScreen()) {
            if (node.interact(action, name)) {
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return !isOpen();
                    }
                }, 3000);

                return isOpen();
            }
        } else if (context().movement.walkTileOnMap(node)) {
            Time.sleep(new Condition() {
                @Override
                public boolean validate() {
                    return !node.isOnGameScreen();
                }
            }, 6000);
        }

        return false;
    }

    /**
     * This method is used to open the {@link Bank}
     *
     * @return <code>true</code> if the {@link Bank} is open
     */
    public boolean open() {
        SceneNode entity = context().objects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                final ObjectDefinition definition = object.getDefinition();
                return definition != null && context().calculations.arrayContains(BANK_BOOTHS, definition.getName())
                        && context().calculations.arrayContains(definition.getActions(), "Bank");
            }
        }, GameObjects.INTERACTABLE_OBJECT);

        if (entity != null) return open(entity, "Bank", ((GameObject) entity).getName());

        entity = context().npcs.getNearest(NPC_BANKERS);
        if (entity != null) return open(entity, "Bank", ((NPC) entity).getName());

        entity = context().objects.getNearest(GameObjects.INTERACTABLE_OBJECT, BANK_CHESTS);
        if (entity != null) return open(entity, "Use", "Bank chest") || open(entity, "Open", "Closed chest") || open(entity, "Bank", "Open chest");

        return isOpen();
    }

    /**
     * This method is used to close the {@link Bank}
     *
     * @return <code>true</code> if the {@link Bank} is closed
     */
    public boolean close() {
        if (!isOpen()) return true;

        final Component component = context().widgets.getComponent(12, 3).getChild(11);
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
     * This method is used to deposit all {@link Inventory} items by using the button
     *
     * @return <code>true</code> if the button has been clicked
     */
    public boolean depositInventory() {
        final Component component = context().widgets.getComponent(12, 27);
        return component != null && component.click(true);
    }

    /**
     * This method is used to deposit all {@link Equipment} items by using the button
     *
     * @return <code>true</code> if the button has been clicked
     */
    public boolean depositEquipment() {
        final Component component = context().widgets.getComponent(12, 29);
        return component != null && component.click(true);
    }

    /**
     * This method is used to get the {@link Bank}s current tab
     *
     * @return the {@link Bank}s current tab
     */
    public int getCurrentTab() {
        final int setting = context().settings.get(115);
        return setting == 0 ? 0 : setting / 4;
    }

    /**
     * This method is used to deposit all {@link Item}s from the {@link Inventory}
     */
    public void depositAll() {
        depositAllExcept(0);
    }

    /**
     * This method is used to deposit all {@link Inventory} {@link Item}s except some selected by ids
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
     * This method is used to deposit all {@link Inventory} {@link Item}s except some selected by names
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
     * This method is used to deposit all {@link Inventory} {@link Item}s except some selected by a {@link Filter}
     *
     * @param filter the {@link Filter} of the {@link Item}s to ignore
     */
    public void depositAllExcept(final Filter<Item> filter) {
        final List<Integer> items = new ArrayList<Integer>();
        for (final Item item : context().inventory.getItems()) {
            int id;
            if (item != null && !filter.accept(item) && !items.contains((id = item.getId())))
                items.add(id);
        }

        int count = context().inventory.getCount();
        out: for (final int id : items) {
            for (int tries = 0; tries < 5; tries++) {
                deposit(context().inventory.getItem(id), Amount.ALL);
                Time.sleep(500, 700);
                final int current = context().inventory.getCount();
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
                case -1:
                    return item.interact("Deposit-All-but-one");
                case 0:
                    return item.interact("Deposit-All");
                case 1:
                    return item.interact("Deposit-1");
                case 5:
                    return item.interact("Deposit-5");
                case 10:
                    return item.interact("Deposit-10");
                default:
                    if (isAmountWidgetVisible()) {
                        return context().keyboard.sendText("" + amount, true);
                    } else if (item.hover()) {
                        if (!context().menu.contains("Deposit-" + amount)) {
                            if (item.interact("Deposit-X")) {
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

    /**
     * This method is used to withdraw a selected {@link Amount} of an {@link Item}
     *
     * @param name the name of the {@link Item} to withdraw
     * @param amount the {@link Amount} to withdraw
     * @return <code>true</code> if successfully withdrew the {@link Item}
     */
    public boolean withdraw(final String name, final Amount amount) {
        return withdraw(name, amount.getAmount());
    }

    /**
     * This method is used to withdraw a selected amount of an {@link Item}
     *
     * @param name the name of the {@link Item} to withdraw
     * @param amount the amount to withdraw
     * @return <code>true</code> if successfully withdrew the {@link Item}
     */
    public boolean withdraw(final String name, final int amount) {
        return withdraw(getItem(name), amount);
    }

    /**
     * This method is used to withdraw a selected {@link Amount} of an {@link Item}
     *
     * @param id the id of the {@link Item} to withdraw
     * @param amount the {@link Amount} to withdraw
     * @return <code>true</code> if successfully withdrew the {@link Item}
     */
    public boolean withdraw(final int id, final Amount amount) {
        return withdraw(id, amount.getAmount());
    }

    /**
     * This method is used to withdraw a selected amount of an {@link Item}
     *
     * @param id the id of the {@link Item} to withdraw
     * @param amount the amount to withdraw
     * @return <code>true</code> if successfully withdrew the {@link Item}
     */
    public boolean withdraw(final int id, final int amount) {
        return withdraw(getItem(id), amount);
    }

    /**
     * This method is used to withdraw a selected {@link Amount} of an {@link Item}
     *
     * @param item the {@link Item} to withdraw
     * @param amount the {@link Amount} to withdraw
     * @return <code>true</code> if successfully withdrew the {@link Item}
     */
    public boolean withdraw(final Item item, final Amount amount) {
        return withdraw(item, amount.getAmount());
    }

    /**
     * This method is used to withdraw a selected amount of an {@link Item}
     *
     * @param item the {@link Item} to withdraw
     * @param amount the amount to withdraw
     * @return <code>true</code> if successfully withdrew the {@link Item}
     */
    public boolean withdraw(final Item item, final int amount) {
        if (item != null) {
            if (isVisible(item)) {
                switch (amount) {
                    case -1:
                        return item.interact("Withdraw-All-but-one");
                    case 0:
                        return item.interact("Withdraw-All");
                    case 1:
                        return item.interact("Withdraw-1");
                    case 5:
                        return item.interact("Withdraw-5");
                    case 10:
                        return item.interact("Withdraw-10");
                    default:
                        if (isAmountWidgetVisible()) {
                            return context().keyboard.sendText("" + amount, true);
                        } else if (item.hover()) {
                            if (!context().menu.contains("Withdraw-" + amount)) {
                                if (item.interact("Withdraw-X")) {
                                    Time.sleep(new Condition() {
                                        @Override
                                        public boolean validate() {
                                            return !isAmountWidgetVisible();
                                        }
                                    }, 1500);

                                    Time.sleep(200, 400);
                                }

                                return withdraw(item, amount);
                            }
                        }

                        return item.interact("Withdraw-" + amount);
                }
            } else {
                scrollTo(item);
            }
        }

        return false;
    }

    /**
     * This method is used to scroll to an {@link Item}
     *
     * @param item the {@link Item} to scroll to
     * @return <code>true</code> if successfully scrolled to the {@link Item}
     */
    public boolean scrollTo(final Item item) {
        // TODO: REWRITE WTF IS THIS PAT
        if (item == null) return false;

        final int x = 492;
        int y = 90;

        final Timer timer = new Timer(15000);
        if (item.getCentralPoint().y < 70) {
            if (context().mouse.move(x, y) && context().mouse.press(x, y, true)) {
                while (isOpen() && !isVisible(item) && timer.isRunning()) {
                    Time.sleep(50);
                }

                context().mouse.release(x, y, true);
            }
        } else {
            y = 284;
            if (context().mouse.move(x, y) && context().mouse.press(x, y, true)) {
                while (isOpen() && !isVisible(item) && timer.isRunning()) {
                    Time.sleep(50);
                }

                context().mouse.release(x, y, true);
            }
        }

        return false;
    }

    /**
     * This method is used to check if the {@link Bank} contains an {@link Item} with a given id
     *
     * @param ids the ids of {@link Item} to check
     * @return <code>true</code> if the {@link Bank} contains an {@link Item} with a given id
     */
    public boolean contains(final int... ids) {
        return getItem(ids) != null;
    }

    /**
     * This method is used to check if the {@link Bank} contains all {@link Item}s with given ids
     *
     * @param ids the ids of {@link Item} to check
     * @return <code>true</code> if the {@link Bank} contains all {@link Item}s with given ids
     */
    public boolean containsAll(final int... ids) {
        for (final int id : ids)
            if (getItem(id) == null) return false;

        return true;
    }

    /**
     * This method is used to check if the {@link Bank} contains an {@link Item} with a given name
     *
     * @param names the names of {@link Item} to check
     * @return <code>true</code> if the {@link Bank} contains an {@link Item} with a given name
     */
    public boolean contains(final String... names) {
        return getItem(names) != null;
    }

    /**
     * This method is used to check if the {@link Bank} contains all {@link Item}s with given names
     *
     * @param names the names of {@link Item} to check
     * @return <code>true</code> if the {@link Bank} contains all {@link Item}s with given names
     */
    public boolean containsAll(final String... names) {
        for (final String name : names)
            if (getItem(name) == null) return false;

        return true;
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
     * This method is used to get the {@link Item}s in the {@link Bank} with specific ids
     *
     * @param ids the ids of the {@link Item}s to get
     * @return the {@link Item}s in the {@link Bank} with specific ids
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
     * This method is used to get the {@link Item}s in the {@link Bank} with specific names
     *
     * @param names the names of the {@link Item}s to get
     * @return the {@link Item}s in the {@link Bank} with specific names
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
     * This method is used to get an {@link Item} in the {@link Bank} with a specific {@link Filter}
     *
     * @param filter the {@link Filter} of the {@link Item} to get
     * @return an {@link Item} in the {@link Bank} with a specific {@link Filter}
     */
    public Item getItem(final Filter<Item> filter) {
        for (final Item item : getItems())
            if (item != null && filter.accept(item)) return item;

        return null;
    }

    /**
     * This method is used to get all the {@link Item}s in the {@link Bank}
     *
     * @return all the {@link Item}s in the {@link Bank}
     */
    public Item[] getItems() {
        final List<Item> result = new ArrayList<Item>();
        final Component component = context().widgets.getComponent(12, 12);
        if (component != null) {
            final Component[] items = component.getChildren();
            if (items != null) {
                int index = 0;
                for (final Component item : items) {
                    if (item != null && item.getComponentId() != -1)
                        result.add(new Item(context(), index, item));

                    index++;
                }
            }
        }

        return result.toArray(new Item[result.size()]);
    }

}
