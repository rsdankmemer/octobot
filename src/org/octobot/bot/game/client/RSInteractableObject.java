package org.octobot.bot.game.client;

/**
 * RSInteractableObject
 *
 * @author Pat-ji
 */
public interface RSInteractableObject extends RSGameObject {

    public int getHash();

    public int getInfo();

    public int getX();

    public int getY();

    public int getZ();

    public RSAnimable getAnimable();

    public int getOrientation();

    public int getEndX();

    public int getEndY();

}