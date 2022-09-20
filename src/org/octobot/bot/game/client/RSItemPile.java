package org.octobot.bot.game.client;

/**
 * RSItemPile
 *
 * @author Pat-ji
 */
public interface RSItemPile {

    public int getHash();

    public int getInfo();

    public int getX();

    public int getY();

    public int getZ();

    public RSAnimable getTop();

    public RSAnimable getMiddle();

    public RSAnimable getBottom();

}