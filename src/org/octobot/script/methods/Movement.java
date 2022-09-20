package org.octobot.script.methods;

import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.*;
import org.octobot.script.wrappers.Component;

import java.awt.*;

/**
 * Movement
 *
 * @author Pat-ji
 */
public class Movement extends ContextProvider {

    public Movement(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the x destination
     *
     * @return the x destination
     */
    public int getDestinationX() {
        return context().client.getDestinationX() == 0 ? 0 : context().client.getDestinationX() + context().game.getBaseX();
    }

    /**
     * This method is used to get the y destination
     *
     * @return the y destination
     */
    public int getDestinationY() {
        return context().client.getDestinationY() == 0 ? 0 : context().client.getDestinationY() + context().game.getBaseY();
    }

    /**
     * This method is used to get the destination
     *
     * @return the destination
     */
    public Tile getDestination() {
        return new Tile(getDestinationX(), getDestinationY(), context().game.getPlane());
    }

    /**
     * This method is used to get the current energy
     *
     * @return the current energy
     */
    public int getEnergy() {
        return context().client.getEnergy();
    }

    /**
     * This method is used to check if running is enabled
     *
     * @return <code>true</code> if running is enabled
     */
    public boolean isRunEnabled() {
        return context().settings.get(173) == 1;
    }

    /**
     * This method is used to get the current collision flags
     *
     * @param plane the plane to get the collision flags from
     * @return the current collision flags from a given plane
     */
    public int[][] getCollisionFlags(final int plane) {
        return context().client.getCollisionMaps()[plane].getFlags();
    }

    /**
     * This method is used to set run status
     *
     * @param enabled <code>true</code> to enable running
     * @return <code>true</code> if run enabled is equal to the given enabled
     */
    public boolean setRun(final boolean enabled) {
        if (isRunEnabled() == enabled) return true;

        if (context().bank.isOpen()) {
            if (context().bank.close()) return setRun(enabled);
        } else {
            if (((context().settings.get(1055) >> 0x03) & 0x01) == 0) {
                final Component component = context().widgets.getComponent(548, 93);
                if (component != null && component.interact("Toggle Run"))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return !isRunEnabled() == enabled;
                        }
                    }, 600);
            } else if (context().tabs.isOpen(Tabs.Tab.SETTINGS)) {
                final Component settings = context().widgets.getComponent(261, 53);
                if (settings != null && settings.click(true))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return !isRunEnabled() == enabled;
                        }
                    }, 600);
            } else if (context().tabs.open(Tabs.Tab.SETTINGS)) {
                Time.sleep(200, 400);
                return setRun(enabled);
            }
        }

        return isRunEnabled() == enabled;
    }

    /**
     * This method is used to get a closest {@link Tile} on the mini map
     *
     * @param tile the {@link Tile} to get the closest on the mini map from
     * @return a closest {@link Tile} on the mini map
     */
    public Tile getClosestOnMap(Tile tile) {
        if (tile.getMatrix(context()).isOnMiniMap()) return tile;

        final Tile location = context().players.getLocal().getLocation();
        tile = tile.add(-location.getX(), -location.getY());

        final double angle = Math.atan2(tile.getY(), tile.getX());
        return new Tile(location.getX() + (int) (16.0D * Math.cos(angle)), location.getY() + (int) (16.0D * Math.sin(angle)), tile.getPlane());
    }

    /**
     * This method is used to get the furthest {@link Tile} on the screen
     *
     * @param tiles the {@link Tile}s to check in
     * @return the furthest {@link Tile} on the screen
     */
    public Tile getFurthestTileOnScreen(final Tile[] tiles) {
        for (int i = tiles.length - 1; i >= 0; i--)
            if (tiles[i] != null && tiles[i].getMatrix(context()).isOnGameScreen()) return tiles[i];

        return null;
    }

    /**
     * This method is used to get the closest {@link Tile} on the screen
     *
     * @param tiles the {@link Tile}s to check in
     * @return the closest {@link Tile} on the screen
     */
    public Tile getClosestTileOnScreen(final Tile[] tiles) {
        for (final Tile tile : tiles)
            if (tile != null && tile.getMatrix(context()).isOnGameScreen()) return tile;

        return null;
    }

    /**
     * This method is used to walk to a {@link Locatable} on the screen
     *
     * @param locatable the {@link Locatable} to walk to
     * @return <code>true</code> if successfully walked to the {@link Locatable}
     */
    public boolean walkTileOnScreen(final Locatable locatable) {
        return walkTileOnMap(locatable.getLocation());
    }

    /**
     * This method is used to walk to a {@link Tile} on the screen
     *
     * @param tile the {@link Tile} to walk to
     * @return <code>true</code> if successfully walked to the {@link Tile}
     */
    public boolean walkTileOnScreen(final Tile tile) {
        final TileMatrix matrix = tile.getMatrix(context());
        return matrix.isOnGameScreen() && matrix.interact("Walk here");
    }

    /**
     * This method is used to walk to a {@link Locatable} on the mini map
     *
     * @param locatable the {@link Locatable} to walk to
     * @return <code>true</code> if successfully walked to the {@link Locatable}
     */
    public boolean walkTileOnMap(final Locatable locatable) {
        return walkTileOnMap(locatable.getLocation());
    }

    /**
     * This method is used to walk to a {@link Tile} on the mini map
     *
     * @param tile the {@link Tile} to walk to
     * @return <code>true</code> if successfully walked to the {@link Tile}
     */
    public boolean walkTileOnMap(Tile tile) {
        if (tile == null) return false;

        TileMatrix matrix = tile.getMatrix(context());
        if (!matrix.isOnMiniMap()) {
            tile = getClosestOnMap(tile);
            matrix = tile.getMatrix(context());
        }

        final Point point = matrix.toMiniMap();
        return point != null && context().mouse.click(point.x, point.y, true);
    }

    public Path findLocalPath(final Locatable start, final Locatable end) {
        return findLocalPath(start, end, false);
    }

    public Path findLocalPath(final Locatable start, final Locatable end, final boolean isObject) {
        return findLocalPath(start.getLocation(), end.getLocation(), isObject);
    }

    public Path findLocalPath(final Tile start, final Tile end) {
        return findLocalPath(start, end, false);
    }

    public Path findLocalPath(final Tile start, final Tile end, final boolean isObject) {
        return new Path(context(), new PathFinder(context(), start, end, isObject).getPath());
    }

    public Path findLocalPath(final Locatable start, final Locatable end, final int[][] flags) {
        return findLocalPath(start.getLocation(), end.getLocation(), flags);
    }

    public Path findLocalPath(final Locatable start, final Locatable end, final int[][] flags, final boolean isObject) {
        return findLocalPath(start.getLocation(), end.getLocation(), flags, isObject);
    }

    public Path findLocalPath(final Tile start, final Tile end, final int[][] flags) {
        return findLocalPath(start, end, flags, false);
    }

    public Path findLocalPath(final Tile start, final Tile end, final int[][] flags, final boolean isObject) {
        return new Path(context(), new PathFinder(context(), start, end, flags, isObject).getPath());
    }

    /**
     * This method is used to check if a {@link Locatable} can be reached
     *
     * @param locatable the {@link Locatable} to check
     * @return <code>true</code> if the {@link Locatable} can be reached
     */
    public boolean canReach(final Locatable locatable) {
        return canReach(locatable, false);
    }

    /**
     * This method is used to check if a {@link Locatable} can be reached
     *
     * @param locatable the {@link Locatable} to check
     * @param isObject use <code>true</code> if there is a {@link GameObject} at the {@link Locatable}
     * @return <code>true</code> if the {@link Locatable} can be reached
     */
    public boolean canReach(final Locatable locatable, final boolean isObject) {
        return canReach(locatable.getLocation(), isObject);
    }

    /**
     * This method is used to check if a {@link Tile} can be reached
     *
     * @param tile the {@link Tile} to check
     * @return <code>true</code> if the {@link Tile} can be reached
     */
    public boolean canReach(final Tile tile) {
        return canReach(tile, false);
    }

    /**
     * This method is used to check if a {@link Tile} can be reached
     *
     * @param tile the {@link Tile} to check
     * @param isObject use <code>true</code> if there is a {@link GameObject} at the {@link Tile}
     * @return <code>true</code> if the {@link Tile} can be reached
     */
    public boolean canReach(final Tile tile, final boolean isObject) {
        final PathFinder pathFinder = new PathFinder(context(), context().players.getLocal().getLocation(), tile, isObject);
        return pathFinder.getPath() != null && pathFinder.getPath().length > 0;
    }

    /**
     * Flags
     *
     * @author Pat-ji
     */
    public static class Flags {
        public static final int NORTH_WALL = 0x1280102;
        public static final int EAST_WALL = 0x1280108;
        public static final int SOUTH_WALL = 0x1280120;
        public static final int WEST_WALL = 0x1280180;
        public static final int BLOCKED = 0x100;
        public static final int WATER = 0x1280100;

        public static final int WALL_NORTHWEST = 0x1;
        public static final int WALL_NORTH = 0x2;
        public static final int WALL_NORTHEAST = 0x4;
        public static final int WALL_EAST = 0x8;
        public static final int WALL_SOUTHEAST = 0x10;
        public static final int WALL_SOUTH = 0x20;
        public static final int WALL_SOUTHWEST = 0x40;
        public static final int WALL_WEST = 0x80;

    }

}
