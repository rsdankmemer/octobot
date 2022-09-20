package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSObjectDefinition;

/**
 * ObjectDefinition
 *
 * @author Pat-ji
 */
public class ObjectDefinition extends Wrapper<RSObjectDefinition> implements RSObjectDefinition {

    public ObjectDefinition(final RSObjectDefinition accessor) {
        super(accessor);
    }

    /**
     * this method is used to get the name of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s name
     */
    @Override
    public String getName() {
        return getAccessor() != null ? getAccessor().getName() : "";
    }

    /**
     * this method is used to get the actions of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s actions
     */
    @Override
    public String[] getActions() {
        return getAccessor() != null ? getAccessor().getActions() : null;
    }
    /**
     * this method is used to get the model ids of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s model ids
     */
    @Override
    public int[] getModelIds() {
        return getAccessor() != null ? getAccessor().getModelIds() : null;
    }
    /**
     * this method is used to get the x size of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s x size
     */
    @Override
    public int getXSize() {
        return getAccessor() != null ? getAccessor().getXSize() : -1;
    }
    /**
     * this method is used to get the y size of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s y size
     */
    @Override
    public int getYSize() {
        return getAccessor() != null ? getAccessor().getYSize() : -1;
    }

    /**
     * This method is used to check if you can stand on the {@link ObjectDefinition}
     *
     * @return {@code true} if you can stand on the {@link ObjectDefinition}
     */
    @Override
    public boolean isWalkable() {
        return getAccessor() != null && getAccessor().isWalkable();
    }
    /**
     * this method is used to get the animation of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s animation
     */
    @Override
    public int getAnimation() {
        return getAccessor() != null ? getAccessor().getAnimation() : -1;
    }

    /**
     * This method is used to check if the {@link ObjectDefinition} is rotated
     *
     * @return {@code true} if the {@link ObjectDefinition} is rotated
     */
    @Override
    public boolean isRotated() {
        return getAccessor() != null && getAccessor().isRotated();
    }

    /**
     * this method is used to get the x model size of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s x model size
     */
    @Override
    public int getXModelSize() {
        return getAccessor() != null ? getAccessor().getXModelSize() : -1;
    }

    /**
     * this method is used to get the y model size of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s y model size
     */
    @Override
    public int getYModelSize() {
        return getAccessor() != null ? getAccessor().getYModelSize() : -1;
    }

    /**
     * this method is used to get the z model size of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s z model size
     */
    @Override
    public int getZModelSize() {
        return getAccessor() != null ? getAccessor().getZModelSize() : -1;
    }

    /**
     * this method is used to get the face of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s face
     */
    @Override
    public int getFace() {
        return getAccessor() != null ? getAccessor().getFace() : -1;
    }

    /**
     * this method is used to get the transform ids of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s transform ids
     */
    @Override
    public int[] getTransformIds() {
        return getAccessor() != null ? getAccessor().getTransformIds() : null;
    }

    /**
     * this method is used to get the id of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s id
     */
    @Override
    public int getId() {
        return getAccessor() != null ? getAccessor().getId() : -1;
    }

    /**
     * this method is used to get the icon of an {@link ObjectDefinition}
     *
     * @return the {@link ObjectDefinition}s icon
     */
    @Override
    public int getIcon() {
        return getAccessor() != null ? getAccessor().getIcon() : -1;
    }

    /**
     * This method is used to check if the {@link ObjectDefinition} is equal to this
     *
     * @param obj the object you want to compare to the {@link ObjectDefinition}
     * @return true if the {@link ObjectDefinition} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof ObjectDefinition) {
            final ObjectDefinition definition = (ObjectDefinition) obj;
            return definition.getAccessor() == getAccessor();
        } else if (obj instanceof RSObjectDefinition) {
            return obj == getAccessor();
        }

        return false;
    }

}
