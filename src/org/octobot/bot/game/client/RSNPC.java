package org.octobot.bot.game.client;

/**
 * RSNPC
 *
 * @author Pat-ji
 */
public interface RSNPC extends RSActor {

    public RSNPCDefinition getDefinition();

    public RSModel getAnimatedModel();

}