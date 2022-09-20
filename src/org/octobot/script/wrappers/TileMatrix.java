package org.octobot.script.wrappers;

import org.octobot.script.Interactable;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.methods.Movement;

import java.awt.*;

/**
 * TileMatrix
 *
 * @author Pat-ji
 */
public class TileMatrix implements Interactable, Locatable {
    private final Tile tile;
    private final ScriptContext context;

    public TileMatrix(final Tile tile, final ScriptContext context) {
        this.tile = tile;
        this.context = context;
    }

    /**
     * This method is used to get the location of a {@link TileMatrix}
     *
     * @return the {@link TileMatrix}' location
     */
    @Override
    public Tile getLocation() {
        return tile;
    }

    /**
     * This method is used to check of the {@link TileMatrix} is on screen, this includes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link TileMatrix} is on screen
     */
    @Override
    public boolean isOnScreen() {
        return context.calculations.isOnScreen(getCentralPoint());
    }

    /**
     * This method is used to check of the {@link TileMatrix} is on the screen, this  includes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link TileMatrix} is on the game screen
     */
    @Override
    public boolean isOnGameScreen() {
        return context.calculations.isOnGameScreen(getCentralPoint());
    }

    /**
     * This method is used to check if the {@link TileMatrix} is on the mini map
     *
     * @return <code>true</code> if the {@link TileMatrix} is on the mini map
     */
    public boolean isOnMiniMap() {
        return context.game.getPlane() == tile.getPlane() && tile.distance(context.players.getLocal()) < 18;
    }

    /**
     * This method is used to transfer a {@link TileMatrix} to a {@link java.awt.Point} on the mini map
     *
     * @return the {@link java.awt.Point} on the mini map
     */
    public Point toMiniMap() {
        return context.calculations.tileToMiniMap(tile);
    }

    /**
     * This method is used to get the central {@link java.awt.Point} of the {@link TileMatrix}
     *
     * @return the central {@link java.awt.Point} of the {@link TileMatrix}
     */
    @Override
    public Point getCentralPoint() {
        return getPoint(.5, .5, 0);
    }

    /**
     * This method is used to get a {@link java.awt.Point} on the {@link TileMatrix} with a given offset
     *
     * @param xOff the x offset to use
     * @param yOff the y offset to use
     * @param height the height offset to use
     * @return the {@link java.awt.Point} on the {@link TileMatrix}
     */
    public Point getPoint(final double xOff, final double yOff, final int height) {
        return context.calculations.groundToScreen(
                (int) ((tile.getX() - context.game.getBaseX() + xOff) * 128),
                (int) ((tile.getY() - context.game.getBaseY() + yOff) * 128),
                tile.getPlane(), -height);
    }

    /**
     * This method is used to get the bounds of the {@link TileMatrix}
     *
     * @return the {@link TileMatrix}' bounds
     */
    public Polygon getBounds() {
        final Point nw = getPoint(0.0, 0.0, 0);
        final Point ne = getPoint(1.0, 0.0, 0);
        final Point se = getPoint(0.0, 1.0, 0);
        final Point sw = getPoint(1.0, 1.0, 0);
        if (context.calculations.isOnScreen(nw) && context.calculations.isOnScreen(ne) && context.calculations.isOnScreen(se) && context.calculations.isOnScreen(sw)) {
            final Polygon polygon = new Polygon();
            polygon.addPoint(nw.x, nw.y);
            polygon.addPoint(ne.x, ne.y);
            polygon.addPoint(sw.x, sw.y);
            polygon.addPoint(se.x, se.y);
            return polygon;
        }

        return null;
    }

    /**
     * This method is used to hover the mouse over the {@link TileMatrix}
     *
     * @return <code>true</code> if the {@link TileMatrix} has been successfully hovered
     */
    @Override
    public boolean hover() {
        final Point point = getCentralPoint();
        return point != null && context.mouse.move(point);
    }

    /**
     * This method is used to click the {@link TileMatrix}
     *
     * @param left true if the click should be a left, false if it should be a right click
     * @return <code>true</code> if the {@link TileMatrix} has been successfully clicked
     */
    @Override
    public boolean click(final boolean left) {
        final Point point = getCentralPoint();
        return point != null && context.mouse.click(point.x, point.y, true);
    }

    /**
     * This method is used to interact with the {@link TileMatrix}
     *
     * @param action what action the client should use to interact with the {@link TileMatrix} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link TileMatrix}
     */
    @Override
    public boolean interact(final String action) {
        return interact(action, "");
    }

    /**
     * This method is used to interact with the {@link TileMatrix}
     *
     * @param action what action the client should use to interact with the {@link TileMatrix} as a {@link String}
     * @param option what option the client should use when interacting with the {@link TileMatrix} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link TileMatrix}
     */
    @Override
    public boolean interact(final String action, final String option) {
        final Point point = getCentralPoint();
        if (point != null)
            return hover() && (action.equals("Walk here") ? context.menu.interact(action, option, false) : context.menu.interact(action, option));

        return click(true);
    }

    /**
     * This method is used to see if you can stand on the {@link TileMatrix}
     *
     * @return <code>true</code> if you can stand on the {@link TileMatrix}
     */
    public boolean isWalkable() {
        final int flag = context.movement.getCollisionFlags(context.game.getPlane())[tile.getX() - context.game.getBaseX()][tile.getY() - context.game.getBaseY()];
        return (flag & Movement.Flags.BLOCKED) == 0;
    }

    /**
     * This method is used to check if a {@link TileMatrix} consists of water
     *
     * @return <code>true</code> if the {@link TileMatrix} consists of water
     */
    public boolean isWater() {
        final int flag = context.movement.getCollisionFlags(context.game.getPlane())[tile.getX() - context.game.getBaseX()][tile.getY() - context.game.getBaseY()];
        return (flag & Movement.Flags.WATER) != 0;
    }

    /**
     * This method is used to render the {@link Tile}
     *
     * @param graphics the {@link java.awt.Graphics} to render with
     */
    public void render(final Graphics graphics) {
        final Polygon bounds = getBounds();
        if (bounds != null)
            graphics.fillPolygon(bounds);
    }

}
