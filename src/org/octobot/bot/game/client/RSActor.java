package org.octobot.bot.game.client;

/**
 * RSActor
 *
 * @author Pat-ji
 */
public interface RSActor extends RSAnimable {

    public int getAnimation();

    public int getWalkingQueue();

    public int getX();

    public int getY();

    public String getOverheadText();

    public int getInteractingIndex();

    public int getOrientation();

    public int getHealth();

    public int getMaxHealth();

    public int[] getHitDamages();

    public int[] getHitTypes();

    public int[] getHitCycles();

    public int getIdleAnimation();

    public int getWalkAnimation();

    public int getRunAnimation();

    public int getLoopCycle();

}