package org.octobot.bot.game.client;

/**
 * RSAnimation
 *
 * @author Pat-ji
 */
public interface RSAnimation {

    public RSSkin getSkin();

    public int getStepCount();

    public int[] getOpcodeTable();

    public int[] getModifier1();

    public int[] getModifier2();

    public int[] getModifier3();

}