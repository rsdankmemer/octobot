package org.octobot.bot.game.client;

/**
 * RSGroundDecoration
 *
 * @author Pat-ji
 */
public interface RSGroundDecoration extends RSGameObject {

    public int getHash();

    public int getInfo();

    public int getX();

    public int getY();

    public int getZ();

    public RSAnimable getAnimable();

}