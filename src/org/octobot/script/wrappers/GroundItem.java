package org.octobot.script.wrappers;

import org.octobot.bot.game.client.*;
import org.octobot.script.Identifiable;
import org.octobot.script.Locatable;
import org.octobot.script.Nameable;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Nodes;

import java.awt.*;

/**
 * GroundItem
 *
 * @author Pat-ji
 */
public class GroundItem extends Animable<RSGroundItem> implements RSGroundItem, Locatable, Nameable, Identifiable {
    private final int x, y, plane, heightOffset, pileIndex;

    public GroundItem(final ScriptContext context, final RSGroundItem accessor, final int x, final int y, final int plane, final int heightOffset, final int pileIndex) {
        super(context, accessor);

        this.x = x;
        this.y = y;
        this.plane = plane;
        this.heightOffset = heightOffset;
        this.pileIndex = pileIndex;
    }

    /**
     * This method is used to get the id of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s id
     */
    @Override
    public int getId() {
        return getAccessor() != null ? getAccessor().getId() : -1;
    }

    /**
     * This method is used to get the quantity of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s quantity
     */
    @Override
    public int getQuantity() {
        return getAccessor() != null ? getAccessor().getQuantity() : -1;
    }

    /**
     * This method is used to get the animated model of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s animated model
     */
    @Override
    public RSModel getAnimatedModel() {
        return getAccessor() != null ? getAccessor().getAnimatedModel() : null;
    }

    /**
     * This method is used to get the height of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s height
     */
    @Override
    public int getHeight() {
        return getAccessor() != null ? getAccessor().getHeight() : -1;
    }

    /**
     * This method is used to get the name of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s name
     */
    @Override
    public String getName() {
        final ItemDefinition definition = getDefinition();
        return definition != null ? definition.getName() : "";
    }

    /**
     * This method is used to get the ground actions of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s ground actions
     */
    public String[] getGroundActions() {
        final ItemDefinition definition = getDefinition();
        return definition != null ? definition.getGroundActions() : null;
    }

    /**
     * This method is used to get the x coordinate of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s x coordinate
     */
    public int getX () {
        return x;
    }

    /**
     * This method is used to get the y coordinate of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * This method is used to get the plane of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s plane
     */
    public int getPlane() {
        return plane;
    }

    /**
     * This method is used to get the height offset of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s height offset
     */
    public int getHeightOffset() {
        return heightOffset;
    }

    /**
     * This method is used to get the pile index of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s pile index
     */
    public int getPileIndex() {
        return pileIndex;
    }

    /**
     * This method is used to get the location of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s location
     */
    @Override
    public Tile getLocation() {
        return new Tile(x, y, plane);
    }

    /**
     * This method is used to get the definition of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s definition
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
     * This method is used to get the model of a {@link GroundItem}
     *
     * @return the {@link GroundItem}s model
     */
    @Override
    public Model getModel() {
        Model model = null;
        final RSCache cache = context.client.getGroundItemModelCache();
        if (cache != null) {
            final Object node = Nodes.lookup(cache.getNodeDeque(), getId());
            if (node != null && node instanceof RSModel) {
                model = new Model((RSModel) node);
                model.setContext(context);
                model.setX(((x - context.game.getBaseX()) << 7) + 64);
                model.setY(((y - context.game.getBaseY()) << 7) + 64);
            }
        }

        if (model == null) {
            /*
            final RSModel animable = getAnimatedModel();
            if (animable != null) {
                model = new Model(animable);
                model.setContext(context);
                model.setX(((x - context.game.getBaseX()) << 7) + 64);
                model.setY(((y - context.game.getBaseY()) << 7) + 64);
            }*/
            model = getModelInstance();
            if (model != null) {
                model.setContext(context);
                model.setX(((x - context.game.getBaseX()) << 7) + 64);
                model.setY(((y - context.game.getBaseY()) << 7) + 64);
                return model;
            }

            return null;
        }

        if (model != null)
            model.setHeightOffset(heightOffset);
        return model;
    }

    /**
     * This method is used to check if an {@link GroundItem} is valid
     *
     * @return {@code true} if the {@link GroundItem} is valid
     */
    @Override
    public boolean isValid() {
        final RSNodeList list = context.client.getGroundItems()[plane][x - context.game.getBaseX()][y - context.game.getBaseY()];
        if (list == null) return false;

        RSNode head;
        if ((head = list.getHead()) != null) {
            RSNode next = null;
            while ((next = (next != null ? next.getNext() : head.getNext())) != null && next instanceof RSGroundItem) {
                if (equals(next)) return true;
            }
        }

        return false;
    }

    /**
     * This method is used to render the {@link org.octobot.script.wrappers.GroundItem}
     *
     * @param graphics the {@link java.awt.Graphics} to render with
     */
    public void render(final Graphics graphics) {
        final Model model = getModel();
        if (model != null) {
            final Point[] points = model.getPoints();
            if (points != null && points.length > 0)
                for (final Point point : points)
                    if (point != null)
                        graphics.fillRect(point.x, point.y, 2, 2);
        }
    }

    /**
     * This method is used to check if the {@link GroundItem} is equal to this
     *
     * @param obj the object you want to compare to the {@link GroundItem}
     * @return true if the {@link GroundItem} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof GroundItem) {
            final GroundItem item = (GroundItem) obj;
            return item.getAccessor() == getAccessor();
        } else if (obj instanceof RSGroundItem) {
            return obj == getAccessor();
        }

        return false;
    }

}
