package org.octobot.script.wrappers;

import org.octobot.bot.game.client.*;
import org.octobot.script.ScriptContext;

/**
 * WallObject
 *
 * @author Pat-ji
 */
public class WallObject extends GameObject implements RSWallObject {
    private final RSWallObject accessor;

    public WallObject(final ScriptContext context, final RSWallObject accessor, final int x, final int y) {
        super(context, x, y);

        this.accessor = accessor;
    }

    @Override
    public RSWallObject getAccessor() {
        return accessor;
    }

    /**
     * This method is used to get the hash of the {@link WallObject}
     *
     * @return the {@link WallObject}s hash
     */
    @Override
    public int getHash() {
        return getAccessor() != null ? getAccessor().getHash() : -1;
    }

    /**
     * This method is used to get the information of the {@link WallObject}
     *
     * @return the {@link WallObject}s information
     */
    @Override
    public int getInfo() {
        return getAccessor() != null ? getAccessor().getInfo() : -1;
    }

    /**
     * This method is used to get the z of the {@link WallObject}
     *
     * @return the {@link WallObject}s z
     */
    @Override
    public int getZ() {
        return getAccessor() != null ? getAccessor().getZ() : -1;
    }

    /**
     * This method is used to get the animable of the {@link WallObject}
     *
     * @return the {@link WallObject}s animable
     */
    @Override
    public RSAnimable getAnimable() {
        return getAccessor() != null ? getAccessor().getAnimable() : null;
    }

    /**
     * This method is used to get the face of the {@link WallObject}
     *
     * @return the {@link WallObject}s face
     */
    @Override
    public int getFace() {
        return getAccessor() != null ? getAccessor().getFace() : -1;
    }

    /**
     * This method is used to get the host face of the {@link WallObject}
     *
     * @return the {@link WallObject}s host face
     */
    @Override
    public int getHostFace() {
        return getAccessor() != null ? getAccessor().getHostFace() : -1;
    }

    /**
     * This method is used to get the secondary animable of the {@link WallObject}
     *
     * @return the {@link WallObject}s secondary animable
     */
    @Override
    public RSAnimable getSecondaryAnimable() {
        return getAccessor() != null ? getAccessor().getSecondaryAnimable() : null;
    }

    /**
     * This method is used to get the model of the {@link WallObject}
     *
     * @return the {@link WallObject}s model
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
        }*/

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
     * This method is used to check if the {@link WallObject} is equal to this
     *
     * @param obj the object you want to compare to the {@link WallObject}
     * @return true if the {@link WallObject} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof WallObject) {
            final WallObject object = (WallObject) obj;
            return object.getAccessor() == getAccessor();
        } else if (obj instanceof RSWallObject) {
            return obj == getAccessor();
        }

        return false;
    }

}
