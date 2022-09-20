package org.octobot.script.methods;

import org.octobot.bot.game.client.RSPlayer;
import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.util.Random;
import org.octobot.script.wrappers.Tile;
import org.octobot.script.wrappers.TileMatrix;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

/**
 * Calculations
 *
 * @author Pat-ji
 */
public class Calculations extends ContextProvider {
    public static final int[] SIN_TABLE = new int[2048];
    public static final int[] COS_TABLE = new int[2048];

    static {
        for (int i = 0; i < 2048; i++) {
            SIN_TABLE[i] = (int) (Math.sin((double) i * 0.0030679615D) * 65536.0D);
            COS_TABLE[i] = (int) (Math.cos((double) i * 0.0030679615D) * 65536.0D);
        }
    }

    public Calculations(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to check if a {@link java.awt.Point} is on screen, this includes the chat box, inventory and mini map
     *
     * @param point the {@link java.awt.Point} to check
     * @return <code>true</code> if the {@link java.awt.Point} is on the screen
     */
    public boolean isOnScreen(final Point point) {
        return point != null && isOnScreen(point.x, point.y);
    }

    /**
     * This method is used to check if a point is on screen, this includes the chat box, inventory and mini map
     *
     * @param x the x position to check
     * @param y the y position to check
     * @return <code>true</code> if the point is on the screen
     */
    public boolean isOnScreen(final int x, final int y) {
        final Canvas canvas = context().game.getCanvas();
        return canvas != null && (x > 0 && y > 0 && x < canvas.getWidth() && y < canvas.getHeight());
    }

    /**
     * This method is used to check if a {@link java.awt.Point} is on game screen, this excludes the chat box, inventory and mini map
     *
     * @param point the {@link java.awt.Point} to check
     * @return <code>true</code> if the {@link java.awt.Point} is on the game screen
     */
    public boolean isOnGameScreen(final Point point) {
        return isOnGameScreen(point.x, point.y);
    }

    /**
     * This method is used to check if a point is on game screen, this excludes the chat box, inventory and mini map
     *
     * @param x the x position to check
     * @param y the y position to check
     * @return <code>true</code> if the point is on the game screen
     */
    public boolean isOnGameScreen(final int x, final int y) {
        return x > 10 && y > 10 && x < 506 && y < 330;
    }

    /**
     * This method is used to check if an array contains a value
     *
     * @param array the array to check in
     * @param value the value to check for
     * @return <code>true</code> if the array contains the value
     */
    public boolean arrayContains(final int[] array, final int value) {
        if (array == null || array.length == 0) return false;

        for (final int index : array)
            if (index == value) return true;

        return false;
    }

    /**
     * This method is used to check if an array contains a value
     *
     * @param array the array to check in
     * @param value the value to check for
     * @return <code>true</code> if the array contains the value
     */
    public boolean arrayContains(final String[] array, final String value) {
        if (array == null || array.length == 0 || value == null) return false;

        for (final String index : array)
            if (value.equals(index)) return true;

        return false;
    }

    /**
     * This method is used to check if an array contains a value
     *
     * @param array the array to check in
     * @param value the value to check for
     * @return <code>true</code> if the array contains the value
     */
    public boolean arrayContains(final Object[] array, final Object value) {
        if (array == null || array.length == 0 || value == null) return false;

        for (final Object index : array)
            if (value.equals(index)) return true;

        return false;
    }

    /**
     * This method is used to make an array from a {@link java.util.Collection}
     *
     * @param collection the {@link java.util.Collection} to get the array from
     * @return an array from the {@link java.util.Collection}
     */
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(final Collection<E> collection) {
        if (collection == null || collection.isEmpty()) return null;

        return collection.toArray((E[]) Array.newInstance(collection.iterator().next().getClass(), collection.size()));
    }

    private <E> boolean isAccepted(final E element, final Filter<E>... filters) {
        for (final Filter<E> filter : filters)
            if (filter.accept(element))
                return true;

        return false;
    }

    /**
     * This method is used to filter elements out of an array
     *
     * @param array the array to filter from
     * @param filters the {@link Filter}s to use
     * @return an array with elements that are accepted by the {@link Filter}s
     */
    public <E> E[] filter(final E[] array, final Filter<E>... filters) {
        if (array == null || array.length == 0) return null;

        final List<E> list = new ArrayList<E>() {
            @Override
            public boolean add(final E e) {
                return isAccepted(e, filters) && super.add(e);
            }
        };

        Collections.addAll(list, array);
        return toArray(list);
    }

    /**
     * This method is used to reverse the array
     *
     * @param array the array to reverse
     * @return the same array, but reversed
     */
    public <E> E[] reverse(final E[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            final int j = array.length - (1 + i);
            final E element = array[j];
            array[j] = array[i];
            array[i] = element;
        }

        return array;
    }

    /**
     * This method is used to reflect a {@link Tile} to the mini map
     *
     * @param tile the {@link Tile} to reflect to the mini map
     * @return a {@link java.awt.Point} on the mini map, reflecting the given {@link Tile}
     */
    public Point tileToMiniMap(final Tile tile) {
        final int x = tile.getX() - context().game.getBaseX();
        final int y = tile.getY() - context().game.getBaseY();
        final RSPlayer local = context().client.getLocalPlayer();
        return local != null ? worldToMiniMap(x * 4 + 2 - local.getX() / 32, y * 4 + 2 - local.getY() / 32) : null;
    }

    /**
     * This method is used to reflect a region offset to the mini map
     *
     * @param regionX the region x offset to use
     * @param regionY the region y offset to use
     * @return a {@link java.awt.Point} on the mini map, reflecting the region offset
     */
    private Point worldToMiniMap(final int regionX, final int regionY) {
        final int angle = context().camera.getViewRotation() + context().camera.getMiniMapRotation() & 0x7FF;
        final int sin = SIN_TABLE[angle] * 256 / (context().camera.getMiniMapZoom() + 256);
        final int cos = COS_TABLE[angle] * 256 / (context().camera.getMiniMapZoom() + 256);
        return new Point(644 + (regionY * sin + regionX * cos >> 16), 82 - (regionY * cos - regionX * sin >> 16));
    }

    /**
     * This method is used to reflect a x, y and height to the screen from the world
     *
     * @param x the x to reflect
     * @param y the y to reflect
     * @param height the height to reflect
     * @return a {@link java.awt.Point} on the screen, reflecting the x, y and height
     */
    public Point worldToScreen(int x, int y, int height) {
        x -= context().camera.getX();
        y -= context().camera.getY();
        height -= context().camera.getZ();

        final int sinCurveY = SIN_TABLE[context().camera.getCurveY()];
        final int cosCurveY = COS_TABLE[context().camera.getCurveY()];
        final int sinCurveX = SIN_TABLE[context().camera.getCurveX()];
        final int cosCurveX = COS_TABLE[context().camera.getCurveX()];

        int curves = sinCurveX * y + cosCurveX * x >> 16;
        y = y * cosCurveX - sinCurveX * x >> 16;
        x = curves;
        curves = cosCurveY * height - y * sinCurveY >> 16;
        y = sinCurveY * height + y * cosCurveY >> 16;
        return y == 0 ? new Point(-1, -1) : new Point((x << 9) / y + 259, (curves << 9) / y + 171);
    }

    /**
     * This method is used to reflect a x and y from the world to the mini map
     *
     * @param x the x to reflect
     * @param y the y to reflect
     * @return a {@link java.awt.Point} on the mini map, reflecting the x and y from the world
     */
    public Point worldToMiniMap(double x, double y) {
        x -= context().game.getBaseX();
        y -= context().game.getBaseY();
        final RSPlayer local = context().client.getLocalPlayer();
        return local != null ? worldToMiniMap((int) x * 4 + 2 - local.getX() / 32, 2 + (int) y * 4 - local.getY() / 32) : null;
    }

    /**
     * This method is used to reflect a ground location to the screen
     *
     * @param x the ground x to reflect
     * @param y the ground y to reflect
     * @param plane the plane to reflect
     * @param height the ground height to reflect
     * @return a {@link java.awt.Point} on the screen, reflecting a ground location
     */
    public Point groundToScreen(final int x, final int y, final int plane, final int height) {
        return x < 512 || y < 512 || x > 52224 || y > 52224 ? new Point(-1, -1) : worldToScreen(x, y, tileHeight(x, y, plane) - height);
    }

    /**
     * This method is used to get the height from the tile height array
     *
     * @param x the x of the region to get
     * @param y the y of the region to get
     * @param plane the plane of the region to get
     * @return the height from the tile height array
     */
    public int tileHeight(final int x, final int y, final int plane) {
        final int[][][] groundHeights = context().game.getTileHeights();
        if (groundHeights == null) return 0;

        final int x1 = x >> 7;
        final int y1 = y >> 7;
        if (x1 < 0 || y1 < 0 || x1 >= 104 || y1 >= 104)  return 0;

        final int x2 = x & 0x7F;
        final int y2 = y & 0x7F;
        int zIndex = plane;
        if (zIndex < 3 && (context().game.getTileSettings()[1][x1][y1] & 0x2) == 2)
            zIndex++;

        final int xx = (-x2 + 128) * groundHeights[zIndex][x1][y1] + x2 * groundHeights[zIndex][x1 + 1][y1] >> 7;
        final int yy = groundHeights[zIndex][x1][y1 + 1] * (128 - x2) + groundHeights[zIndex][x1 + 1][y1 + 1] * x2 >> 7;
        return xx * (128 - (y & 0x7F)) - -(y2 * yy) >> 7;
    }

    /**
     * This method is used to get the {@link Tile} under the {@link Mouse} location
     *
     * @return the {@link Tile} under the {@link Mouse} location
     */
    public Tile getTileUnderMouse() {
        if (!isOnGameScreen(context().mouse.getLocation())) return null;

        final int baseX = context().game.getBaseX();
        final int baseY = context().game.getBaseY();
        final int plane = context().game.getPlane();
        for (int x = 1; x < 102; x++)
            for (int y = 1; y < 102; y++) {
                final Tile tile = new Tile(x + baseX, y + baseY, plane);
                final TileMatrix matrix = tile.getMatrix(context());
                if (!matrix.isOnGameScreen()) continue;

                final Polygon bounds = matrix.getBounds();
                if (bounds != null && bounds.contains(context().mouse.getLocation())) return tile;
            }

        return null;
    }

    /**
     * This method is used to calculate the screen distance between two points
     *
     * @param base the base point to calculate from
     * @param x the x to calculate to
     * @param y the y to calculate to
     * @return the screen distance between two points
     */
    public double distance(final Point base, final int x, final int y) {
        return base != null ? Math.sqrt(Math.pow(base.getX() - x, 2) + Math.pow(base.getY() - y, 2)) : -1;
    }

    /**
     * This method is used to calculate the screen distance between two points
     *
     * @param base the base point to calculate from
     * @param dest the destination point to calculate to
     * @return the screen distance between two points
     */
    public double distance(final Point base, final Point dest) {
        return base != null && dest != null ? Math.sqrt(Math.pow(base.getX() - dest.getX(), 2) + Math.pow(base.getY() - dest.getY(), 2)) : -1;
    }

    /**
     * This method is used to calculate the distance to a {@link Locatable}
     *
     * @param locatable the {@link Locatable} to calculate the distance to
     * @return the distance to a {@link Locatable}
     */
    public double distanceTo(final Locatable locatable) {
        return distanceTo(locatable.getLocation());
    }

    /**
     * This method is used to calculate the distance to a {@link Tile}
     *
     * @param tile the {@link Tile} to calculate the distance to
     * @return the distance to a {@link Tile}
     */
    public double distanceTo(final Tile tile) {
        return tile != null ? tile.distance(context().players.getLocal()) : -1;
    }

    /**
     * Gets a random {@link java.awt.Point} inside a {@link java.awt.Rectangle}. The returned
     * point follows a uniform distribution, and as such is not recommended for use with
     * interacting methods.
     *
     * @param rectangle the {@link java.awt.Rectangle} to get the random point from
     * @return a random {@link java.awt.Point} inside a {@link java.awt.Rectangle}
     */
    public Point getRandomPoint(final Rectangle rectangle) {
        return rectangle != null ? new Point(rectangle.x + Random.nextInt(0, rectangle.width), rectangle.y + Random.nextInt(0, rectangle.height)) : null;
    }
//
//    public Point getRandomConcentricPoint(final Rectangle rectangle, final Point origin) {
//        double angle = Random.nextDouble(0d, 360d);
//        if (angle )
//
//        while (result == null || !rectangle.contains(result)) {
//            double angle = Random.nextDouble(0d, 360d);
//            double radius = Random.nextDouble(0, maxRadius);
//            double componentX = radius * Math.cos(angle) + origin.x;
//            double componentY = radius * Math.sin(angle) + origin.y;
//            result = new Point((int)(Math.floor(componentX)), (int)(Math.floor(componentY)));
//        }
//        return result;
//    }

}
