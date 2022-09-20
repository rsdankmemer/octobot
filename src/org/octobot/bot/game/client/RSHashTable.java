package org.octobot.bot.game.client;

/**
 * RSHashTable
 *
 * @author Pat-ji
 */
public interface RSHashTable {

    public RSNode getCurrent();

    public RSNode getHead();

    public RSNode[] getBuckets();

}