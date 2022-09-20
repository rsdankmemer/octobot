package org.octobot.bot.game.client;

/**
 * RSCacheNode
 *
 * @author Pat-ji
 */
public interface RSCacheNode extends RSNode {

    public RSCacheNode getNext();

    public RSCacheNode getPrevious();

}