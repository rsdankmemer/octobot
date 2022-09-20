package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSWorld;

/**
 * World
 *
 * @author Pat-ji
 */
public class World extends Wrapper<RSWorld> implements RSWorld {

    public World(final RSWorld accessor) {
        super(accessor);
    }

    /**
     * This method is used to get the {@link World}s activity such as clan wars
     *
     * @return the activity of the given world as a {@link String}
     */
    @Override
    public String getActivity() {
        return getAccessor() != null ? getAccessor().getActivity() : "";
    }

    /**
     * This method is used to get the {@link World}s mask
     *
     * @return the {@link World}s mask
     */
    @Override
    public int getMask() {
        return getAccessor() != null ? getAccessor().getMask() : -1;
    }

    /**
     * This method is used to get the {@link World}s world
     *
     * @return the {@link World}s world
     */
    @Override
    public int getWorld() {
        return getAccessor() != null ? getAccessor().getWorld() : -1;
    }

    /**
     * This method is used to get the {@link World}s domain
     *
     * @return the {@link World}s domain
     */
    @Override
    public String getDomain() {
        return getAccessor() != null ? getAccessor().getDomain() : "";
    }

    /**
     * This method is used to get the {@link World}s server location
     *
     * @return the {@link World}s server location
     */
    @Override
    public int getServerLocation() {
        return getAccessor() != null ? getAccessor().getServerLocation() : -1;
    }

    /**
     * This method is used to get the {@link World}s player count
     *
     * @return the {@link World}s player count
     */
    @Override
    public int getPlayerCount() {
        return getAccessor() != null ? getAccessor().getPlayerCount() : -1;
    }

    /**
     * This method is used to check if the {@link World} is equal to this
     *
     * @param obj the object you want to compare to the {@link World}
     * @return true if the {@link World} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof World) {
            final World world = (World) obj;
            return world.getAccessor() == getAccessor();
        } else if (obj instanceof RSWorld) {
            return obj == getAccessor();
        }

        return false;
    }

}
