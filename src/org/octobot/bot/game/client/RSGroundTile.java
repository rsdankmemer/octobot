package org.octobot.bot.game.client;

/**
 * RSGroundTile
 *
 * @author Pat-ji
 */
public interface RSGroundTile {

    public RSInteractableObject[] getInteractableObjects();

    public RSGroundDecoration getGroundDecoration();

    public RSWallDecoration getWallDecoration();

    public RSWallObject getWallObject();

    public RSItemPile getItemPile();

}