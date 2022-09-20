package org.octobot.bot.game.client;

/**
 * RSWallDecoration
 *
 * @author Pat-ji
 */
public interface RSWallDecoration extends RSGameObject {

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