package org.octobot.bot.game.client;

/**
 * RSNode
 *
 * @author Pat-ji
 */
public interface RSNode {

    public RSNode getPrevious();

    public RSNode getNext();

    public long getId();

}