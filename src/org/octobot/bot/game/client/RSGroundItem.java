package org.octobot.bot.game.client;

/**
 * RSGroundItem
 *
 * @author Pat-ji
 */
public interface RSGroundItem extends RSAnimable {

    public int getId();

    public int getQuantity();

    public RSModel getAnimatedModel();

}