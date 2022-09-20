package org.octobot.bot.game.client;

/**
 * RSObjectDefinition
 *
 * @author Pat-ji
 */
public interface RSObjectDefinition {

    public String getName();

    public String[] getActions();

    public int[] getModelIds();

    public int getXSize();

    public int getYSize();

    public boolean isWalkable();

    public int getAnimation();

    public boolean isRotated();

    public int getXModelSize();

    public int getYModelSize();

    public int getZModelSize();

    public int getFace();

    public int[] getTransformIds();

    public int getId();

    public int getIcon();

}