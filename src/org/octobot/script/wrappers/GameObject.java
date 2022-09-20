package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSCache;
import org.octobot.bot.game.client.RSGameObject;
import org.octobot.bot.game.client.RSObjectDefinition;
import org.octobot.script.Identifiable;
import org.octobot.script.Nameable;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Nodes;

import java.awt.*;

/**
 * GameObject
 *
 * @author Pat-ji
 */
public abstract class GameObject extends SceneNode<RSGameObject> implements Nameable, Identifiable {
    private final int x, y;
    private Tile location;

    public GameObject(final ScriptContext context, final int x, final int y) {
        super(context, null);

        this.x = x;
        this.y = y;
    }

    /**
     * This method is used to get the hash of a {@link GameObject}
     *
     * @return the {@link GameObject}s hash
     */
    public abstract int getHash();

    /**
     * This method is used to get the information of a {@link GameObject}
     *
     * @return the {@link GameObject}s information
     */
    public abstract int getInfo();

    /**
     * This method is used to get the id of a {@link GameObject}
     *
     * @return the {@link GameObject}s id
     */
    @Override
    public int getId() {
        return (getHash() >> 14 & 0x7FFF);
    }

    /**
     * This method is used to get the x coordinate of a {@link GameObject}
     *
     * @return the {@link GameObject}s x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * This method is used to get the y coordinate of a {@link GameObject}
     *
     * @return the {@link GameObject}s y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * This method is used to get the plane of a {@link GameObject}
     *
     * @return the {@link GameObject}s plane
     */
    public int getPlane() {
        return context.game.getPlane();
    }

    /**
     * This method is used to get the orientation of the {@link GameObject}
     *
     * @return the {@link GameObject}s orientation
     */
    public int getOrientation() {
        return getInfo() >> 6 & 0x3;
    }

    /**
     * This method is used to get the name of a {@link GameObject}
     *
     * @return the {@link GameObject}s name
     */
    @Override
    public String getName() {
        final ObjectDefinition definition = getDefinition();
        return definition != null ? definition.getName() : "";
    }

    /**
     * This method is used to get the actions of a {@link GameObject}
     *
     * @return the {@link GameObject}s actions
     */
    public String[] getActions() {
        final ObjectDefinition definition = getDefinition();
        return definition != null ? definition.getActions() : null;
    }

    /**
     * This method is used to get the location of a {@link GameObject}
     *
     * @return the {@link GameObject}s location
     */
    @Override
    public Tile getLocation() {
        if (location == null)
            location = new Tile(x, y, context.game.getPlane());

        return location;
    }

    /**
     * This method is used to get the definition of a {@link GameObject}
     *
     * @return the {@link GameObject}s definition
     */
    public ObjectDefinition getDefinition() {
        final RSCache cache = context.client.getGameObjectDefinitionCache();
        if (cache != null) {
            Object node = Nodes.lookup(cache.getNodeDeque(), getId());
            if (node != null && node instanceof RSObjectDefinition) {
                RSObjectDefinition definition = (RSObjectDefinition) node;
                final int[] transformIds = definition.getTransformIds();
                if (transformIds == null) {
                    return new ObjectDefinition(definition);
                } else {
                    for (final int transformId : transformIds) {
                        node = Nodes.lookup(cache.getNodeDeque(), transformId);
                        if (node != null && node instanceof RSObjectDefinition) {
                            definition = (RSObjectDefinition) node;
                            if (definition.getTransformIds() == null) return new ObjectDefinition(definition);
                        }
                    }
                }
            } else {
                //context.client.getObjectDefinition(getId());
            }
        }

        return null;
    }

    /**
     * This method is used to check if a {@link GameObject} is valid
     *
     * @return {@code true} if the {@link GameObject} is valid
     */
    @Override
    public boolean isValid() {
        final GameObject[] objects = context.objects.getAllAt(location);
        for (final GameObject object : objects)
            if (equals(object)) return true;

        return false;
    }

    /**
     * This method is used to render the {@link org.octobot.script.wrappers.GameObject}
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

}
