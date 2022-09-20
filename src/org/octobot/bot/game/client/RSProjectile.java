package org.octobot.bot.game.client;

/**
 * RSProjectile
 *
 * @author Pat-ji
 */
public interface RSProjectile extends RSAnimable {

    public boolean isMoving();

    public int getTargetId();

    public double getX();

    public double getY();

    public double getCurrentHeight();

    public double getSpeedVectorX();

    public double getSpeedVectorY();

    public double getSpeedVectorZ();

    public double getSpeedVectorScalar();

    public double getHeightOffset();

    public RSModel getAnimatedModel();

}