package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSNPCDefinition;

/**
 * NPCDefinition
 *
 * @author Pat-ji
 */
public class NPCDefinition extends Wrapper<RSNPCDefinition> implements RSNPCDefinition {

    public NPCDefinition(final RSNPCDefinition accessor) {
        super(accessor);
    }

    /**
     * this method is used to get the name of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s name
     */
    @Override
    public String getName() {
        return getAccessor() != null ? getAccessor().getName() : "";
    }

    /**
     * this method is used to get the level of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s level
     */
    @Override
    public int getLevel() {
        return getAccessor() != null ? getAccessor().getLevel() : -1;
    }

    /**
     * this method is used to get the id of an {@link PlayerDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s id
     */
    @Override
    public int getId() {
        return getAccessor() != null ? getAccessor().getId() : -1;
    }

    /**
     * this method is used to get the actions of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s actions
     */
    @Override
    public String[] getActions() {
        return getAccessor() != null ? getAccessor().getActions() : null;
    }

    /**
     * this method is used to get the head icons of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s head icons
     */
    @Override
    public int[] getHeadIcons() {
        return getAccessor() != null ? getAccessor().getHeadIcons() : null;
    }

    /**
     * this method is used to get the transform ids of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s transform ids
     */
    public int[] getTransformIds() {
        return getAccessor() != null ? getAccessor().getTransformIds() : null;
    }

    /**
     * this method is used to get the setting id of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s setting id
     */
    public int getSettingId() {
        return getAccessor() != null ? getAccessor().getSettingId() : -1;
    }

    /**
     * this method is used to get the session setting id of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s session setting id
     */
    public int getSessionSettingId() {
        return getAccessor() != null ? getAccessor().getSessionSettingId() : -1;
    }

    /**
     * this method is used to get the child definition of an {@link org.octobot.script.wrappers.NPCDefinition}
     *
     * @return the {@link org.octobot.script.wrappers.NPCDefinition}s child definition
     */
    @Override
    public RSNPCDefinition getChildDefinition() {
        return getAccessor() != null ? getAccessor().getChildDefinition() : null;
    }

    /**
     * This method is used to check if the {@link org.octobot.script.wrappers.NPCDefinition} is equal to this
     *
     * @param obj the object you want to compare to the {@link org.octobot.script.wrappers.NPCDefinition}
     * @return true if the {@link org.octobot.script.wrappers.NPCDefinition} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof NPCDefinition) {
            final NPCDefinition definition = (NPCDefinition) obj;
            return definition.getAccessor() == getAccessor();
        } else if (obj instanceof RSNPCDefinition) {
            return obj == getAccessor();
        }

        return false;
    }

}
