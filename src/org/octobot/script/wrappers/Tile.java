package org.octobot.script.wrappers;

import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Random;

import java.awt.*;

/**
 * Tile
 *
 * @author Pat-ji
 */
public class Tile implements Locatable {
    private int x, y, plane;
    private TileMatrix matrix;

    public Tile(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Tile(final int x, final int y, final int plane) {
        this(x, y);

        this.plane = plane;
    }

    /**
     * This method is used to get the x coordinate of the {@link Tile}
     *
     * @return the x coordinate of the {@link Tile}
     */
    public int getX() {
        return x;
    }

    /**
     * This method is used to get the y coordinate of the {@link Tile}
     *
     * @return the y coordinate of the {@link Tile}
     */
    public int getY() {
        return y;
    }

    /**
     * This method is used to get the plane of the {@link Tile}
     *
     * @return the plane of the {@link Tile}
     */
    public int getPlane() {
        return plane;
    }

    /**
     * This method is used to get the locaton of the {@link Tile}
     *
     * @return the location of the {@link Tile}
     */
    @Override
    public Tile getLocation() {
        return this;
    }

    /**
     * This method is used to get a new {@link TileMatrix}
     *
     * @param context the {@link ScriptContext} used for the {@link TileMatrix}
     * @return the {@link TileMatrix}
     */
    public TileMatrix getMatrix(final ScriptContext context) {
        if (matrix == null)
            matrix = new TileMatrix(this, context);

        return matrix;
    }

    /**
     * This method is used to add a x and y coordinate to the {@link Tile}s coordinates
     *
     * @param x the x you want to add
     * @param y the y you want to add
     * @return a new {@link Tile} with an offset to this one
     */
    public Tile add(final int x, final int y) {
        return new Tile(this.x + x, this.y + y, plane);
    }

    /**
     * This method is used to randomize the x and y of a {@link Tile} within a certain range
     *
     * @param random the x and y range
     * @return a {@link Tile} within a certain range
     */
    public Tile randomize(final int random) {
        return new Tile(x + Random.nextInt(-random, random), y + Random.nextInt(-random, random), plane);
    }

    /**
     * This method is used to get the distance to a {@link Locatable}
     *
     * @param locatable the locatable you want to calculate the distance to
     * @return the distance to the {@link Locatable}
     */
    public double distance(final Locatable locatable) {
        return distance(locatable.getLocation());
    }

    /**
     * This method is used to get the distance to a {@link Tile}
     *
     * @param tile the {@link Tile} you want to calculate the distance to
     * @return the distance to the given {@link Tile}
     */
    public double distance(final Tile tile) {
        return tile != null ? Math.sqrt(Math.pow(x - tile.getX(), 2) + Math.pow(y - tile.getY(), 2)) : 0;
    }

    /**
     * Highlights this tile with the specified color. Tiles off of the game screen are not highlighted.
     *
     * @param context The script context needed to get this tile's matrix.
     * @param g The graphics object that this tile should be drawn to.
     * @param color The color that this tile should be drawn with.
     * @param drawOnMinimap If set to true, an icon will also be drawn on the minimap for this tile.
     */
    public boolean render(ScriptContext context, Graphics2D g, Color color, boolean drawOnMinimap) {
        boolean success = false;
        TileMatrix matrix = getMatrix(context);
        if (matrix != null) {
            g.setColor(color);
            if (matrix.getBounds() != null && matrix.isOnGameScreen()) {
                g.fill(matrix.getBounds());
            }
            Player localPlayer = context.players.getLocal();
            if (drawOnMinimap && (localPlayer != null) && (localPlayer.getLocation().distance(this) < 20)) {
                Point minimapPoint = matrix.toMiniMap();
                g.fillOval(minimapPoint.x - 2, minimapPoint.y - 2, 4, 4);
            }
            return true;
        }
        return false;
    }

    /**
     * This method is used to make the {@link Tile}s location a {@link String}
     *
     * @return the {@link Tile}s location
     */
    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", plane=" + plane;
    }

    /**
     * This method is used to check if the {@link Tile} is equal to this
     *
     * @param obj the object you want to compare to the {@link Tile}
     * @return true if the {@link Tile} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Tile) {
            final Tile tile = (Tile) obj;
            return tile.x == x && tile.y == y && tile.plane == plane;
        }

        return false;
    }

}
