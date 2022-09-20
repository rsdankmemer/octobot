package org.octobot.bot.game.client;

/**
 * RSClassData
 *
 * @author Pat-ji
 */
public interface RSClassData {

    public java.lang.reflect.Method[] getMethods();

    public java.lang.reflect.Field[] getFields();

    public byte[][][] getBytes();

}