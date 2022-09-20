package org.octobot.bot.game.client;

/**
 * RSNodeIterable
 *
 * @author Pat-ji
 */
public interface RSNodeIterable extends Iterable {

    public int getSize();

    public RSNode[] getNodes();

    public RSNode getCurrent();

}