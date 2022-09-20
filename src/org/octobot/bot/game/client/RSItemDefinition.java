package org.octobot.bot.game.client;

/**
 * RSItemDefinition
 *
 * @author Pat-ji
 */
public interface RSItemDefinition {

    public String getName();

    public String[] getGroundActions();

    public String[] getInventoryActions();

    public int[] getStackIds();

    public int[] getStackSizes();

    public int getNoteIndex();

    public int getModelIndex();

    public boolean isMembers();

    public int getNoteTemplateIndex();

    public int getStoreValue();

}