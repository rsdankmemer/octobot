package org.octobot.bot.game.client;

/**
 * RSSkin
 *
 * @author Pat-ji
 */
public interface RSSkin extends RSNode {

    public int getCount();

    public int[] getOpcodes();

    public int[][] getSkinList();

}