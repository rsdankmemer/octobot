package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSItemDefinition;

/**
 * ItemDefinition
 *
 * @author Pat-ji
 */
public class ItemDefinition extends Wrapper<RSItemDefinition> implements RSItemDefinition {

    public ItemDefinition(final RSItemDefinition accessor) {
        super(accessor);
    }

    /**
     * this method is used to get the name of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s name
     */
    @Override
    public String getName() {
        return getAccessor() != null ? getAccessor().getName() : "";
    }

    /**
     * this method is used to get the ground actions of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s ground actions
     */
    @Override
    public String[] getGroundActions() {
        return getAccessor() != null ? getAccessor().getGroundActions() : null;
    }

    /**
     * this method is used to get the inventory actions of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s inventory actions
     */
    @Override
    public String[] getInventoryActions() {
        return getAccessor() != null ? getAccessor().getInventoryActions() : null;
    }

    /**
     * this method is used to get the stacks ids of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s stack ids
     */
    @Override
    public int[] getStackIds() {
        return getAccessor() != null ? getAccessor().getStackIds() : null;
    }

    /**
     * this method is used to get the name of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s name
     */
    @Override
    public int[] getStackSizes() {
        return getAccessor() != null ? getAccessor().getStackSizes() : null;
    }

    /**
     * this method is used to get the noted index of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s noted index
     */
    @Override
    public int getNoteIndex() {
        return getAccessor() != null ? getAccessor().getNoteIndex() : -1;
    }

    /**
     * this method is used to get the model index of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s model index
     */
    @Override
    public int getModelIndex() {
        return getAccessor() != null ? getAccessor().getModelIndex() : -1;
    }

    /**
     * This method is used to check if an item is members only
     *
     * @return {@code true} if the items is members only
     */
    @Override
    public boolean isMembers() {
        return getAccessor() != null && getAccessor().isMembers();
    }

    /**
     * this method is used to get the note template index of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s note template index
     */
    @Override
    public int getNoteTemplateIndex() {
        return getAccessor() != null ? getAccessor().getNoteTemplateIndex() : -1;
    }

    /**
     * this method is used to get the store value of an {@link ItemDefinition}
     *
     * @return the {@link ItemDefinition}s store value
     */
    @Override
    public int getStoreValue() {
        return getAccessor() != null ? getAccessor().getStoreValue() : -1;
    }

    /**
     * This method is used to check if the {@link ItemDefinition} is equal to this
     *
     * @param obj the object you want to compare to the {@link ItemDefinition}
     * @return true if the {@link ItemDefinition} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof ItemDefinition) {
            final ItemDefinition definition = (ItemDefinition) obj;
            return definition.getAccessor() == getAccessor();
        } else if (obj instanceof RSItemDefinition) {
            return obj == getAccessor();
        }

        return false;
    }

}
