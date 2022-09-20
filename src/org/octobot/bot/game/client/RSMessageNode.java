package org.octobot.bot.game.client;

/**
 * RSMessageNode
 *
 * @author Pat-ji
 */
public interface RSMessageNode extends RSCacheNode {

    public int getSourceId();

    public String getMessage();

    public String getSender();

    public String getClan();

}