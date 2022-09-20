package org.octobot.script.wrappers;

import org.octobot.bot.game.client.*;
import org.octobot.script.ScriptContext;

/**
 * GroundDecoration
 *
 * @author Pat-ji
 */
public class GroundDecoration extends GameObject implements RSGroundDecoration {
    private final RSGroundDecoration accessor;

    public GroundDecoration(final ScriptContext context, final RSGroundDecoration accessor, final int x, final int y) {
        super(context, x, y);

        this.accessor = accessor;
    }

    @Override
    public RSGroundDecoration getAccessor() {
        return accessor;
    }

    /**
     * This method is used to get the hash of a {@link GroundDecoration}
     *
     * @return the {@link GroundDecoration}s hash
     */
    @Override
    public int getHash() {
        return getAccessor() != null ? getAccessor().getHash() : -1;
    }

    /**
     * This method is used to get the information of a {@link GroundDecoration}
     *
     * @return the {@link GroundDecoration}s information
     */
    @Override
    public int getInfo() {
        return getAccessor() != null ? getAccessor().getInfo() : -1;
    }

    /**
     * This method is used to get the z of a {@link GroundDecoration}
     *
     * @return the {@link GroundDecoration}s z
     */
    @Override
    public int getZ() {
        return getAccessor() != null ? getAccessor().getZ() : -1;
    }

    /**
     * This method is used to get the animable of a {@link GroundDecoration}
     *
     * @return the {@link GroundDecoration}s animable
     */
    @Override
    public RSAnimable getAnimable() {
        return getAccessor() != null ? getAccessor().getAnimable() : null;
    }

    /**
     * This method is used to get the model of a {@link GroundDecoration}
     *
     * @return the {@link GroundDecoration}s model
     */
    @Override
    public Model getModel() {
        final RSAnimable animable = getAnimable();
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
     * This method is used to check if the {@link GroundDecoration} is equal to this
     *
     * @param obj the object you want to compare to the {@link GroundDecoration}
     * @return true if the {@link GroundDecoration} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof GroundDecoration) {
            final GroundDecoration object = (GroundDecoration) obj;
            return object.getAccessor() == getAccessor();
        } else if (obj instanceof RSGroundDecoration) {
            return obj == getAccessor();
        }

        return false;
    }

}
