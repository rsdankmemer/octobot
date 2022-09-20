package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSAnimable;
import org.octobot.bot.game.client.RSModel;
import org.octobot.bot.game.client.RSWallDecoration;
import org.octobot.script.ScriptContext;

/**
 * WallDecoration
 *
 * @author Pat-ji
 */
public class WallDecoration extends GameObject implements RSWallDecoration {
    private final RSWallDecoration accessor;

    public WallDecoration(final ScriptContext context, final RSWallDecoration accessor, final int x, final int y) {
        super(context, x, y);

        this.accessor = accessor;
    }

    @Override
    public RSWallDecoration getAccessor() {
        return accessor;
    }

    /**
     * This method is used to get the {@link WallDecoration}s hash
     *
     * @return the {@link WallDecoration}s hash
     */
    @Override
    public int getHash() {
        return getAccessor() != null ? getAccessor().getHash() : -1;
    }

    /**
     * This method is used to get the {@link WallDecoration}s information
     *
     * @return the {@link WallDecoration}s information
     */
    @Override
    public int getInfo() {
        return getAccessor() != null ? getAccessor().getInfo() : -1;
    }

    /**
     * This method is used to get the {@link WallDecoration}s z
     *
     * @return the {@link WallDecoration}s z
     */
    @Override
    public int getZ() {
        return getAccessor() != null ? getAccessor().getZ() : -1;
    }

    /**
     * This method is used to get the {@link WallDecoration}s animable
     *
     * @return the {@link WallDecoration}s animable
     */
    @Override
    public RSAnimable getAnimable() {
        return getAccessor() != null ? getAccessor().getAnimable() : null;
    }

    /**
     * This method is used to get the {@link WallDecoration}s face
     *
     * @return the {@link WallDecoration}s face
     */
    @Override
    public int getFace() {
        return getAccessor() != null ? getAccessor().getFace() : -1;
    }

    /**
     * This method is used to get the {@link WallDecoration}s host face
     *
     * @return the {@link WallDecoration}s host face
     */
    @Override
    public int getHostFace() {
        return getAccessor() != null ? getAccessor().getHostFace() : -1;
    }

    /**
     * This method is used to get the {@link WallDecoration}s secondary animable
     *
     * @return the {@link WallDecoration}s secondary animable
     */
    @Override
    public RSAnimable getSecondaryAnimable() {
        return getAccessor() != null ? getAccessor().getSecondaryAnimable() : null;
    }

    /**
     * This method is used to get the {@link WallDecoration}s model
     *
     * @return the {@link WallDecoration}s model
     */
    @Override
    public Model getModel() {
        RSAnimable animable = getAnimable();
        if (animable == null) animable = getSecondaryAnimable();
        if (animable == null) return null;

        /*Model model = null;
        if (animable instanceof RSModel) {
            model = new Model((RSModel) animable);
            model.setContext(context);
            model.setX(getAccessor().getX());
            model.setY(getAccessor().getY());
        } else if (animable instanceof RSGameAnimableObject) {
            try {
                final RSModel animated = ((RSGameAnimableObject) animable).getAnimatedModel();
                if (animated != null) {
                    model = new Model(animated);
                    model.setContext(context);
                    model.setX(getAccessor().getX());
                    model.setY(getAccessor().getY());
                }
            } catch (final Exception ignored) {}
        }
*/
        Model model = animable.getModelInstance();
        if (model != null) {
            model.setContext(context);
            model.setX(getAccessor().getX());
            model.setY(getAccessor().getY());
        } else if (animable instanceof RSModel) {
            model = new Model((RSModel) animable);
            model.setContext(context);
            model.setX(getAccessor().getX());
            model.setY(getAccessor().getY());
        }

        return model;
    }

    /**
     * This method is used to check if the {@link WallDecoration} is equal to this
     *
     * @param obj the object you want to compare to the {@link WallDecoration}
     * @return true if the {@link WallDecoration} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof WallDecoration) {
            final WallDecoration object = (WallDecoration) obj;
            return object.getAccessor() == getAccessor();
        } else if (obj instanceof RSWallDecoration) {
            return obj == getAccessor();
        }

        return false;
    }

}
