package org.octobot.bot.game.client;

/**
 * RSNPCDefinition
 *
 * @author Pat-ji
 */
public interface RSNPCDefinition {

    public String getName();

    public int getLevel();

    public int getId();

    public String[] getActions();

    public int[] getHeadIcons();

    public int[] getTransformIds();

    public int getSettingId();

    public int getSessionSettingId();

    public RSNPCDefinition getChildDefinition();

}