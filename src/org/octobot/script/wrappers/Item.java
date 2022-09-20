package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSCache;
import org.octobot.bot.game.client.RSItemDefinition;
import org.octobot.script.Identifiable;
import org.octobot.script.Interactable;
import org.octobot.script.Nameable;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Nodes;
import org.octobot.script.util.Time;

import java.awt.*;

/**
 * Item
 *
 * @author Pat-ji
 */
public class Item implements Identifiable, Nameable, Interactable {
    private final ScriptContext context;
    private final int index;
    private final Component component;

    public Item(final ScriptContext context, final int index, final Component component) {
        this.context = context;
        this.index = index;
        this.component = component;
    }

    /**
     * This method is used to get the id of an {@link Item}
     *
     * @return the {@link Item}s id
     */
    @Override
    public int getId() {
        final int[] items = component.getItems();
        return items != null && items.length > 0 ? (items[index] - 1) : component.getComponentId();
    }

    /**
     * This method is used to get the name of an {@link Item}
     *
     * @return the {@link Item}s name
     */
    @Override
    public String getName() {
        final ItemDefinition definition = getDefinition();
        return definition != null ? definition.getName() : "";
    }

    /**
     * This method is used to get the component of an {@link Item}
     *
     * @return the {@link Item}s component
     */
    public Component getComponent() {
        return component;
    }

    /**
     * This method is used to get the bounds of an {@link Item}
     *
     * @return the {@link Item}s bounds
     */
    public Rectangle getBounds() {
        int x, y;
        final int height = component.getHeight();
        if (height == 7) {
            x = 565 + ((index % 4) * 42);
            y = 215 + ((index / 4) * 36);
            return new Rectangle(x, y, 26, 26);
        } else if (height == 5) {
            x = 79 + ((index % 8) * 47);
            y = 74 + ((index / 8) * 46);
            return new Rectangle(x, y, 26, 26);
        } else if (height == 4) {
            x = 132 + ((index % 7) * 40);
            y = 77 + ((index / 7) * 40);
            return new Rectangle(x, y, 26, 26);
        }

        return component.getBounds();
    }

    /**
     * This method is used to get the stack size of an {@link Item}
     *
     * @return the {@link Item}s stack size
     */
    public int getStackSize() {
        final int[] items = component.getItemsStacks();
        return items != null && items.length > 0 ? items[index] : component.getItemStackSize();
    }

    /**
     * This method is used to get the {@link ItemDefinition} of an {@link Item}
     *
     * @return the {@link Item}s {@link ItemDefinition}
     */
    public ItemDefinition getDefinition() {
        final RSCache cache = context.client.getGroundItemDefinitionCache();
        if (cache != null) {
            final Object node = Nodes.lookup(cache.getNodeDeque(), getId());
            if (node != null && node instanceof RSItemDefinition) return new ItemDefinition((RSItemDefinition) node);
        }

        return null;
    }

    /**
     * This method is used to use an {@link Item} on a different {@link Item} by id
     *
     * @param targetId the id of the {@link Item} you want to use your {@link Item} on
     * @return {@code true} if the client has successfully used to {@link Item} on the targeted {@link Item}
     */
    public boolean useItemOn(final int... targetId) {
        return useItemOn(context.inventory.getItem(targetId));
    }

    /**
     * This method is used to use an {@link Item} on a different {@link Item} by name
     *
     * @param targetName the name of the {@link Item} you want to use your {@link Item} on
     * @return {@code true} if the client has successfully used to {@link Item} on the targeted {@link Item}
     */
    public boolean useItemOn(final String... targetName) {
        return useItemOn(context.inventory.getItem(targetName));
    }

    /**
     * This method is used to use an {@link Item} on a different {@link Interactable}
     *
     * @param target the {@link Interactable} you want to use your {@link Item} on
     * @return {@code true} if the client has successfully used to {@link Item} on the targeted {@link Interactable}
     */
    public boolean useItemOn(final Interactable target) {
        return useItemOn(target, "");
    }

    /**
     * This method is used to use an {@link Item} on a different {@link Interactable} by the {@link Interactable}s name
     *
     * @param interactable the {@link Interactable} you want to use your {@link Item} on
     * @param name the name of the {@link Interactable} you want to use the {@link Item} on
     * @return {@code true} if the client has successfully used to {@link Item} on the targeted {@link Interactable}
     */
    public boolean useItemOn(final Interactable interactable, final String name) {
        return context.inventory.useItemOn(this, interactable, name);
    }

    /**
     * This method is used to check of the {@link Item} is on screen, this includes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link Item} is on screen
     */
    @Override
    public boolean isOnScreen() {
        final Point point = getCentralPoint();
        return point != null && context.calculations.isOnScreen(point);
    }

    /**
     * This method is used to check of the {@link Item} is on screen, this excludes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link Item} is on game screen
     */
    @Override
    public boolean isOnGameScreen() {
        final Point point = getCentralPoint();
        return point != null && context.calculations.isOnGameScreen(point);
    }

    /**
     * This method is used to get the central {@link java.awt.Point} of the {@link Item}
     *
     * @return the {@link Item}s central {@link java.awt.Point}
     */
    @Override
    public Point getCentralPoint() {
        int x, y;
        final int height = component.getHeight();
        if (height == 7) {
            x = 578 + ((index % 4) * 42);
            y = 228 + ((index / 4) * 36);
            return new Point(x, y);
        } else if (height == 5) {
            x = 92 + ((index % 8) * 47);
            y = 87 + ((index / 8) * 46);
            return new Point(x, y);
        } else if (height == 4) {
            x = 145 + ((index % 7) * 40);
            y = 90 + ((index / 7) * 40);
            return new Point(x, y);
        }

        return component.getCentralPoint();
    }

    /**
     * This method is used to get a random {@link java.awt.Point} of the {@link Item}
     *
     * @return a random {@link java.awt.Point} of the {@link Item}
     */
    public Point getRandomPoint() {
        return context.calculations.getRandomPoint(getBounds());
    }

    /**
     * This method is used to hover the mouse over an {@link Item}
     *
     * @return {@code true} if the client is successfully hovering the {@link Item}
     */
    @Override
    public boolean hover() {
        if (getBounds().contains(context.mouse.getLocation())) return true;

        final Point point = getRandomPoint();
        return point != null && context.mouse.move(point.x, point.y);
    }

    /**
     * This method is used to click on an {@link Item}
     *
     * @param left true if the click should be a left, false if it should be a right click
     * @return {@code true} is the client has successfully clicked on the {@link Item}
     */
    @Override
    public boolean click(final boolean left) {
        return hover() && context.mouse.click(left);
    }

    /**
     * This method is used to interact with the {@link Item}
     *
     * @param action what action the client should use to interact with the {@link Item} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link Item}
     */
    @Override
    public boolean interact(final String action) {
        return interact(action, "");
    }

    /**
     * This method is used to interact with the {@link Item}
     *
     * @param action what action the client should use to interact with the {@link Item} as a {@link String}
     * @param option what option the client should use when interacting with the {@link Item} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link Item}
     */
    @Override
    public boolean interact(final String action, final String option) {
        for (int tries = 0; tries < 2; tries++) {
            if (hover()) {
                Time.sleep(100, 150);
                if (context.menu.interact(action, option, false)) return true;
            }
        }

        return false;
    }

    /**
     * This method is used to check if the {@link Item} is equal to this
     *
     * @param obj the object you want to compare to the {@link Item}
     * @return true if the {@link Item} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Item) {
            final Item item = (Item) obj;
            return item.getCentralPoint().equals(getCentralPoint());
        }

        return false;
    }

}
