package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSPlayerDefinition;

/**
 * PlayerDefinition
 *
 * @author Pat-ji
 */
public class PlayerDefinition extends Wrapper<RSPlayerDefinition> implements RSPlayerDefinition {

    public PlayerDefinition(final RSPlayerDefinition accessor) {
        super(accessor);
    }

    /**
     * This method is used to get the {@link PlayerDefinition}s appearance
     *
     * @return the {@link PlayerDefinition}s appearance
     */
    @Override
    public int[] getAppearance() {
        return getAccessor() != null ? getAccessor().getAppearance() : null;
    }

    /**
     * This method is used to check if the {@link PlayerDefinition} is equal to this
     *
     * @param obj the object you want to compare to the {@link PlayerDefinition}
     * @return true if the {@link PlayerDefinition} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof PlayerDefinition) {
            final PlayerDefinition definition = (PlayerDefinition) obj;
            return definition.getAccessor() == getAccessor();
        } else if (obj instanceof RSPlayerDefinition) {
            return obj == getAccessor();
        }

        return false;
    }

}
