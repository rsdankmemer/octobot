package org.octobot.bot.game.client;

/**
 * RSNodeDeque
 *
 * @author Pat-ji
 */
public interface RSNodeDeque {

    public RSNode getCurrent();

    public RSNode getHead();

    public RSNode[] getBuckets();

}