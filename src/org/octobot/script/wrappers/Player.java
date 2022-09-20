package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSModel;
import org.octobot.bot.game.client.RSPlayer;
import org.octobot.script.ScriptContext;

/**
 * Player
 *
 * @author Pat-ji
 */
public class Player extends Actor<RSPlayer> implements RSPlayer {

    public Player(final ScriptContext context, final RSPlayer accessor) {
        super(context, accessor);
    }

    /**
     * This method is used to get the {@link Player}s model
     *
     * @return the {@link Player}s model
     */
    @Override
    public Model getModel() {
        final Model model = getModelInstance();
        if (model != null) {
            model.setContext(context);
            model.setX(getLocalX());
            model.setY(getLocalY());
            model.rotate(getRotation() & 0x3fff);
            return model;
        }

        return null;
    }

    /**
     * This method is used to get the {@link Player}s name
     *
     * @return the {@link Player}s name
     */
    @Override
    public String getName() {
        return getAccessor() != null ? getAccessor().getName() : null;
    }

    /**
     * This method is used to get the {@link Player}s overhead prayer icon
     *
     * @return the {@link Player}s overhead prayer icon
     */
    @Override
    public int getPrayerIcon() {
        return getAccessor() != null ? getAccessor().getPrayerIcon() : -1;
    }

    /**
     * This method is used to get the {@link Player}s skill icon
     *
     * @return the {@link Player}s skull icon
     */
    @Override
    public int getSkullIcon() {
        return getAccessor() != null ? getAccessor().getSkullIcon() : -1;
    }

    /**
     * This method is used to get the {@link Player}s team
     *
     * @return the {@link Player}s team
     */
    @Override
    public int getTeam() {
        return getAccessor() != null ? getAccessor().getTeam() : -1;
    }

    /**
     * This method is used to get the {@link Player}s comat leve
     *
     * @return the {@link Player}s combat level
     */
    @Override
    public int getCombatLevel() {
        return getAccessor() != null ? getAccessor().getCombatLevel() : -1;
    }

    /**
     * This method is used to get the {@link Player}s total level
     *
     * @return the {@link Player}s total level
     */
    @Override
    public int getTotalLevel() {
        return getAccessor() != null ? getAccessor().getTotalLevel() : -1;
    }

    /**
     * This method is used to get the {@link Player}s definition
     *
     * @return the {@link Player}s definition
     */
    @Override
    public PlayerDefinition getDefinition() {
        return getAccessor() != null ? new PlayerDefinition(getAccessor().getDefinition()) : null;
    }

    /**
     * This method is used to get the {@link Player}s animated model
     *
     * @return the {@link Player}s animated model
     */
    @Override
    public RSModel getAnimatedModel() {
        return getAccessor() != null ? getAccessor().getAnimatedModel() : null;
    }

    /**
     * This method is used to check if a {@link Player} is valid
     *
     * @return <code>true</code> if the {@link Player} is valid
     */
    @Override
    public boolean isValid() {
        for (final RSPlayer player : context.client.getPlayerArray())
            if (equals(player)) return true;

        return equals(context.client.getLocalPlayer());
    }

    /**
     * This method is used to check if the {@link Player} is equal to this
     *
     * @param obj the object you want to compare to the {@link Player}
     * @return true if the {@link Player} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Player) {
            final Player player = (Player) obj;
            return player.getAccessor() == getAccessor();
        } else if (obj instanceof RSPlayer) {
            return obj == getAccessor();
        }

        return false;
    }

}
