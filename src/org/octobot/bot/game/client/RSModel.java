package org.octobot.bot.game.client;

/**
 * RSModel
 *
 * @author Pat-ji
 */
public interface RSModel extends RSAnimable {

    public int[] getXVertices();

    public int[] getYVertices();

    public int[] getZVertices();

    public int[] getXTriangles();

    public int[] getYTriangles();

    public int[] getZTriangles();

    public int getVerticesCount();

    public int getTriangleCount();

}