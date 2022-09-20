package org.octobot.bot.game.client;

/**
 * RSPlayer
 *
 * @author Pat-ji
 */
public interface RSPlayer extends RSActor {

    public String getName();

    public int getPrayerIcon();

    public int getSkullIcon();

    public int getTeam();

    public int getCombatLevel();

    public int getTotalLevel();

    public RSPlayerDefinition getDefinition();

    public RSModel getAnimatedModel();

}