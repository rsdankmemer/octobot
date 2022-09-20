package org.octobot.bot.game.client;

/**
 * RSSceneGraph
 *
 * @author Pat-ji
 */
public interface RSSceneGraph {

    public RSGroundTile[][][] getGroundTiles();

    public RSInteractableObject[] getAnimableObjects();

}