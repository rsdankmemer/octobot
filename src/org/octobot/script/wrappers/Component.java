package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSWidget;
import org.octobot.script.Interactable;
import org.octobot.script.ScriptContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Component
 *
 * @author Pat-ji
 */
public class Component extends Wrapper<RSWidget> implements RSWidget, Interactable {
    private final ScriptContext context;

    private final int index;

    public Component(final ScriptContext context, final RSWidget accessor, final int index) {
        super(accessor);

        this.context = context;
        this.index = index;
    }

	/**
	 * This method is used to get the {@link Component}s index
	 *
	 * @return the {@link Component}s index
	 */
    public int getIndex() {
        return index;
    }

	/**
	 * This method is used to get the {@link Component}s id
	 *
	 * @return the {@link Component}s id
	 */
    @Override
    public int getId() {
        return getAccessor() != null ? getAccessor().getId() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s parent id
	 *
	 * @return the {@link Component}s parent id
	 */
    @Override
    public int getParentId() {
        return getAccessor() != null ? getAccessor().getParentId() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s child
	 *
	 * @return the {@link Component}s child
	 */
    public Component getChild(final int index) {
        final Component[] children = getChildren();
        return index >= 0 && index < children.length ? children[index] : null;
    }

	/**
	 * This method is used to get the {@link Component}s children
	 *
	 * @return the {@link Component}s children
	 */
    @Override
    public Component[] getChildren() {
        final List<Component> result = new ArrayList<Component>();
        if (getAccessor() != null) {
            int index = 0;
            final RSWidget[] children = getAccessor().getChildren();
            if (children != null)
                for (final RSWidget child : children)
                    if (child != null)
                        result.add(new Component(context, child, index++));
        }

        return result.toArray(new Component[result.size()]);
    }

	/**
	 * This method is used to get all the {@link Item}s in this {@link Component}
	 *
	 * @return all the {@link Item}s in this {@link Component}
	 */
    public Item[] getWidgetItems() {
        final List<Item> result = new ArrayList<Item>();
        final int[] items = getItems();
        if (items != null)
            for (int i = 0; i < items.length; i++)
                if (items[i] > 0)
                    result.add(new Item(context, i, this));

        return result.toArray(new Item[result.size()]);
    }

	/**
	 * This method is used to get the {@link Component}s items
	 *
	 * @return the {@link Component}s items
	 */
    @Override
    public int[] getItems() {
        return getAccessor() != null ? getAccessor().getItems() : null;
    }

	/**
	 * This method is used to get the {@link Component}s item stacks
	 *
	 * @return the {@link Component}s item stacks
	 */
    @Override
    public int[] getItemsStacks() {
        return getAccessor() != null ? getAccessor().getItemsStacks() : null;
    }

	/**
	 * This method is used to get the {@link Component}s x coordinate
	 *
	 * @return the {@link Component}s x coordinate
	 */
    @Override
    public int getX() {
        return getAccessor() != null ? getAccessor().getX() + getAccessor().getMasterX() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s y coordinate
	 *
	 * @return the {@link Component}s y coordinate
	 */
    @Override
    public int getY() {
        return getAccessor() != null ? getAccessor().getY() + getAccessor().getMasterY() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s width
	 *
	 * @return the {@link Component}s width
	 */
    @Override
    public int getWidth() {
        return getAccessor() != null ? getAccessor().getWidth() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s height
	 *
	 * @return the {@link Component}s height
	 */
    @Override
    public int getHeight() {
        return getAccessor() != null ? getAccessor().getHeight() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s model type
	 *
	 * @return the {@link Component}s model type
	 */
    @Override
    public int getModelType() {
        return getAccessor() != null ? getAccessor().getModelType() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s texture id
	 *
	 * @return the {@link Component}s texture id
	 */
    @Override
    public int getTextureId() {
        return getAccessor() != null ? getAccessor().getTextureId() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s model id
	 *
	 * @return the {@link Component}s model id
	 */
    @Override
    public int getModelId() {
        return getAccessor() != null ? getAccessor().getModelId() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s possible actions
	 *
	 * @return the {@link Component}s possible actions
	 */
    @Override
    public String[] getActions() {
        return getAccessor() != null ? getAccessor().getActions() : null;
    }

	/**
	 * This method is used to get the {@link Component}s border thickness
	 *
	 * @return the {@link Component}s border thickness
	 */
    @Override
    public int getBorderThickness() {
        return getAccessor() != null ? getAccessor().getBorderThickness() : -1;
    }

	/**
	 * This method is used to check if the {@link Component} is hidden
	 *
	 * @return <code>true</code> if the {@link Component} is hidden
	 */
    @Override
    public boolean isHidden() {
        return getAccessor() != null && getAccessor().isHidden();
    }

	/**
	 * This method is used to get the {@link Component}s children's positions
	 *
	 * @return the {@link Component}s children's positions
	 */
    @Override
    public int[][] getChildPositions() {
        return getAccessor() != null ? getAccessor().getChildPositions() : null;
    }

	/**
	 * This method is used to get the {@link Component}s bound index
	 *
	 * @return the {@link Component}s bound index
	 */
    @Override
    public int getBoundsIndex() {
        return getAccessor() != null ? getAccessor().getBoundsIndex() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s type
	 *
	 * @return the {@link Component}s type
	 */
    @Override
    public int getType() {
        return getAccessor() != null ? getAccessor().getType() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s text
	 *
	 * @return the {@link Component}s text
	 */
    @Override
    public String getText() {
        return getAccessor() != null ? getAccessor().getText() : "";
    }

	/**
	 * This method is used to get the {@link Component}s text color
	 *
	 * @return the {@link Component}s text color
	 */
    @Override
    public int getTextColor() {
        return getAccessor() != null ? getAccessor().getTextColor() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s opacity
	 *
	 * @return the {@link Component}s opacity
	 */
    @Override
    public int getOpacity() {
        return getAccessor() != null ? getAccessor().getOpacity() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s scroll bars H
	 *
	 * @return the {@link Component}s scroll bars H
	 */
    @Override
    public int getScrollBarH() {
        return getAccessor() != null ? getAccessor().getScrollBarH() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s scroll bars V
	 *
	 * @return the {@link Component}s scroll bars V
	 */
    @Override
    public int getScrollBarV() {
        return getAccessor() != null ? getAccessor().getScrollBarV() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s component name
	 *
	 * @return the {@link Component}s component name
	 */
    @Override
    public String getComponentName() {
        return getAccessor() != null ? getAccessor().getComponentName() : "";
    }

	/**
	 * This method is used to get the {@link Component}s component id
	 *
	 * @return the {@link Component}s component id
	 */
    @Override
    public int getComponentId() {
        return getAccessor() != null ? getAccessor().getComponentId() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s items stack size
	 *
	 * @return the {@link Component}s items stack size
	 */
    @Override
    public int getItemStackSize() {
        return getAccessor() != null ? getAccessor().getItemStackSize() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s master x coordinate
	 *
	 * @return the {@link Component}s master x coordinate
	 */
    @Override
    public int getMasterX() {
        return getAccessor() != null ? getAccessor().getMasterX() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s master y coordinate
	 *
	 * @return the {@link Component}s master y coordinate
	 */
    @Override
    public int getMasterY() {
        return getAccessor() != null ? getAccessor().getMasterY() : -1;
    }

	/**
	 * This method is used to get the {@link Component}s bounds
	 *
	 * @return the {@link Component}s bounds
	 */
    public Rectangle getBounds() {
        return getWidth() == -1 && getHeight() == -1 ? null : new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

	/**
	 * This method is used to check if the {@link Component} is visible, note: this doesn't mean that it's visible on your screen
	 *
	 * @return <code>true</code> if the {@link Component} is visible
	 */
    public boolean isVisible() {
        return getBoundsIndex() != -1;
    }

	/**
	 * This method is used to check if the {@link Component} is on the screen including the chat box, mini map and inventory
	 *
	 * @return <code>true</code> if the {@link Component} is on the screen including the chat box, mini map and inventory
	 */
    @Override
    public boolean isOnScreen() {
        return context.calculations.isOnScreen(getCentralPoint());
    }

	/**
	 * This method is used to check if the {@link Component} is on the screen excluding the chat box, mini map and inventory
	 *
	 * @return <code>true</code> if the {@link Component} is on the screen excluding the chat box, mini map and inventory
	 */
    @Override
    public boolean isOnGameScreen() {
        return context.calculations.isOnGameScreen(getCentralPoint());
    }

	/**
	 * This method is used to get the {@link Component}s central point
	 *
	 * @return the {@link Component}s central point
	 */
    @Override
    public Point getCentralPoint() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

	/**
	 * This method is used to hover over the {@link Component}
	 *
	 * @return <code>true</code> if the client has successfully hovered over the {@link Component}
	 */
    @Override
    public boolean hover() {
        final Point point = getCentralPoint();
        return point != null && context.mouse.move(point.x, point.y);
    }

	/**
	 * This method is used to click on the {@link Component}
	 *
	 * @return <code>true</code> if the client has successfully clicked on the {@link Component}
	 */
    @Override
    public boolean click(final boolean left) {
        final Point point = getCentralPoint();
        return point != null && context.mouse.click(point.x, point.y, left);
    }

	/**
	 * This method is used to interact with the {@link Component}
	 *
	 * @param action what action the client should use to interact with the {@link Component} as a {@link String}
	 * @return <code>true</code> if the client has successfully interacted with the {@link Component}
	 */
    @Override
    public boolean interact(final String action) {
        return interact(action, "");
    }

	/**
	 * This method is used to interact with the {@link Component}
	 *
	 * @param action what action the client should use to interact with the {@link Component} as a {@link String}
	 * @param option what option the client should use when interacting with the {@link Component} as a {@link String}
	 * @return <code>true</code> if the client has successfully interacted with the {@link Component}
	 */
    @Override
    public boolean interact(final String action, final String option) {
        final Point point = getCentralPoint();
        return point != null && context.mouse.move(point.x, point.y) && context.menu.interact(action, option, false);
    }

	/**
	 * This method is used to check if the {@link Tile} is equal to this
	 *
	 * @param obj the object you want to compare to the {@link Tile}
	 * @return true if the {@link Tile} and the object are equal
	 */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Component) {
            final Component component = (Component) obj;
            return component.getAccessor() == getAccessor();
        } else if (obj instanceof RSWidget) {
            return obj == getAccessor();
        }

        return false;
    }

}