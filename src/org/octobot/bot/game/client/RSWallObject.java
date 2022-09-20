package org.octobot.bot.game.client;

/**
 * RSWallObject
 *
 * @author Pat-ji
 */
public interface RSWallObject extends RSGameObject {

    public int getHash();

    public int getInfo();

    public int getX();

    public int getY();

    public int getZ();

    public RSAnimable getAnimable();

    public int getFace();

    public int getHostFace();

    public RSAnimable getSecondaryAnimable();

}