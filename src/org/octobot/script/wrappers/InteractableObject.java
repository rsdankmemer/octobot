package org.octobot.script.wrappers;

import org.octobot.bot.game.client.*;
import org.octobot.script.ScriptContext;

/**
 * InteractableObject
 *
 * @author Pat-ji
 */
public class InteractableObject extends GameObject implements RSInteractableObject {
    private final RSInteractableObject accessor;

    public InteractableObject(final ScriptContext context, final RSInteractableObject accessor, final int x, final int y) {
        super(context, x, y);

        this.accessor = accessor;
    }

    @Override
    public RSInteractableObject getAccessor() {
        return accessor;
    }

    /**
     * This method is used to get the hash of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s hash
     */
    @Override
    public int getHash() {
        return getAccessor() != null ? getAccessor().getHash() : -1;
    }

    /**
     * This method is used to get the information of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s information
     */
    @Override
    public int getInfo() {
        return getAccessor() != null ? getAccessor().getInfo() : -1;
    }

    /**
     * This method is used to get the z of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s z
     */
    @Override
    public int getZ() {
        return getAccessor() != null ? getAccessor().getZ() : -1;
    }

    /**
     * This method is used to get the animable of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s animable
     */
    @Override
    public RSAnimable getAnimable() {
        return getAccessor() != null ? getAccessor().getAnimable() : null;
    }

    /**
     * This method is used to get the orientation of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s orientation
     */
    @Override
    public int getOrientation() {
        return getAccessor() != null ? getAccessor().getOrientation() : -1;
    }

    /**
     * This method is used to get the end x of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s enx x
     */
    @Override
    public int getEndX() {
        return getAccessor() != null ? getAccessor().getEndX() : -1;
    }

    /**
     * This method is used to get the end y of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s end y
     */
    @Override
    public int getEndY() {
        return getAccessor() != null ? getAccessor().getEndY() : -1;
    }

    /**
     * This method is used to get the model of an {@link InteractableObject}
     *
     * @return the {@link InteractableObject}s model
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
            model.rotate(getOrientation());
        } else if (animable instanceof RSModel) {
            model = new Model((RSModel) animable);
            model.setContext(context);
            model.setX(getAccessor().getX());
            model.setY(getAccessor().getY());
            model.rotate(getOrientation());
        }

        /*if (model != null)
            model.rotate(getOrientation());*/

        return model;
    }

    /**
     * This method is used to check if the {@link InteractableObject} is equal to this
     *
     * @param obj the object you want to compare to the {@link InteractableObject}
     * @return true if the {@link InteractableObject} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof InteractableObject) {
            final InteractableObject object = (InteractableObject) obj;
            return object.getAccessor() == getAccessor();
        } else if (obj instanceof RSInteractableObject) {
            return obj == getAccessor();
        }

        return false;
    }

}
