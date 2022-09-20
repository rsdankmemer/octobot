package org.octobot.script.methods;

import org.octobot.bot.game.client.*;
import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.collection.GroundItemQuery;
import org.octobot.script.wrappers.GroundItem;
import org.octobot.script.wrappers.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * GroundItems
 *
 * @author Pat-ji
 */
public class GroundItems extends ContextProvider {
    public static final Filter<GroundItem> ALL_FILTER = new Filter<GroundItem>() {
        @Override
        public boolean accept(final GroundItem item) {
            return true;
        }
    };

    public GroundItems(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param ids the ids of the {@link GroundItem} to get
     * @return a {@link GroundItem} with a specific id
     */
    public GroundItem getNearest(final int... ids) {
        return getNearest(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                return context().calculations.arrayContains(ids, item.getId());
            }
        });
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param names the names of the {@link GroundItem} to get
     * @return a {@link GroundItem} with a specific name
     */
    public GroundItem getNearest(final String... names) {
        return getNearest(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                return context().calculations.arrayContains(names, item.getName());
            }
        });
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link GroundItem} that is accepted by the {@link Filter}
     */
    public GroundItem getNearest(final Filter<GroundItem> filter) {
        return getNearest(context().players.getLocal().getLocation(), filter);
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param locatable the {@link Locatable} to look at
     * @return a {@link GroundItem} at a specific {@link Locatable}
     */
    public GroundItem getNearest(final Locatable locatable) {
        return getNearest(locatable.getLocation());
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param tile the {@link Tile} to look at
     * @return a {@link GroundItem} at a specific {@link Tile}
     */
    public GroundItem getNearest(final Tile tile) {
        return getNearest(tile, ALL_FILTER);
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param locatable the {@link Locatable} to look at
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link GroundItem} that is accepted by the {@link Filter} at a specific {@link Locatable}
     */
    public GroundItem getNearest(final Locatable locatable, final Filter<GroundItem> filter) {
        return getNearest(locatable.getLocation(), filter);
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param tile the {@link Tile} to look at
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link GroundItem} that is accepted by the {@link Filter} at a specific {@link Tile}
     */
    public GroundItem getNearest(final Tile tile, final Filter<GroundItem> filter) {
        final GroundItem[] loaded = getLoaded(filter);
        if (loaded == null || loaded.length < 1) return null;

        GroundItem result = null;
        double dist = Float.MAX_VALUE;
        for (final GroundItem object : loaded) {
            final double ddist = object.getLocation().distance(tile);
            if (ddist < dist) {
                dist = ddist;
                result = object;
            }
        }

        return result;
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param tile the {@link Tile} to look at
     * @return a {@link GroundItem} at a specific {@link Tile}
     */
    public GroundItem getAt(final Tile tile) {
        return getAt(tile.getX(), tile.getY());
    }

    /**
     * This method is used to get a {@link GroundItem}
     *
     * @param x the x location to look at
     * @param y the y location to look at
     * @return a {@link GroundItem} at a specific x and y location
     */
    public GroundItem getAt(final int x, final int y) {
        final GroundItem[] at = getAllAt(x - context().game.getBaseX(), y - context().game.getBaseY());
        return at != null && at.length > 0 ? at[0] : null;
    }

    /**
     * This method is used to get all loaded {@link GroundItem}s
     *
     * @param tile the {@link Tile} to look at
     * @return an array with all loaded {@link GroundItem}s at a specific {@link Tile}
     */
    public GroundItem[] getAllAt(final Tile tile) {
        return getAllAt(tile.getX() - context().game.getBaseX(), tile.getY() - context().game.getBaseY());
    }

    /**
     * This method is used to get all loaded {@link GroundItem}s
     *
     * @param xOffset the x offset to look at
     * @param yOffset the y offset to look at
     * @return an array with all loaded {@link GroundItem}s at a specific x and y region offset
     */
    public GroundItem[] getAllAt(final int xOffset, final int yOffset) {
        final List<GroundItem> result = new ArrayList<GroundItem>();

        if (xOffset < 0 || yOffset < 0 || xOffset >= 104 || yOffset >= 104) return null;

        final int plane = context().game.getPlane();
        final RSGroundTile tile = context().client.getSceneGraph().getGroundTiles()[plane][xOffset][yOffset];
        final RSNodeList deque = context().client.getGroundItems()[plane][xOffset][yOffset];

        RSNode head;
        if (deque != null && (head = deque.getHead()) != null) {
            RSNode next = null;

            int index = 0;
            while ((next = (next != null ? next.getNext() : head.getNext())) != null && next instanceof RSGroundItem) {
                result.add(new GroundItem(context(), // context
                        (RSGroundItem) next, // accessor
                        xOffset + context().game.getBaseX(), // x
                        yOffset + context().game.getBaseY(), // y
                        plane, // plane
                        tile != null && tile.getItemPile() != null ? -tile.getItemPile().getZ() : 0, // heightOffset
                        index++)); //index
            }
        }

        return result.toArray(new GroundItem[result.size()]);
    }

    /**
     * This method is used to get all loaded {@link GroundItem}s
     *
     * @return an array with all loaded {@link GroundItem}s
     */
    public GroundItem[] getLoaded() {
        return getLoaded(ALL_FILTER);
    }

    /**
     * This method is used to get all loaded {@link GroundItem}s
     *
     * @param ids the ids of the {@link GroundItem}s to get
     * @return an array with all loaded {@link GroundItem}s with specific ids
     */
    public GroundItem[] getLoaded(final int... ids) {
        return getLoaded(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                return context().calculations.arrayContains(ids, item.getId());
            }
        });
    }

    /**
     * This method is used to get all loaded {@link GroundItem}s
     *
     * @param names the names of the {@link GroundItem}s to get
     * @return an array with all loaded {@link GroundItem}s with specific names
     */
    public GroundItem[] getLoaded(final String... names) {
        return getLoaded(new Filter<GroundItem>() {
            @Override
            public boolean accept(final GroundItem item) {
                return context().calculations.arrayContains(names, item.getName());
            }
        });
    }

    /**
     * This method is used to get all loaded {@link GroundItem}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @return an array with all loaded {@link GroundItem}s that are accepted by the {@link Filter}
     */
    public GroundItem[] getLoaded(final Filter<GroundItem> filter) {
        return getLoaded(filter, 51);
    }

    /**
     * This method is used to get all loaded {@link GroundItem}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @param range the maximum range to look at
     * @return an array with all loaded {@link GroundItem}s that are accepted by the {@link Filter} with a maximum range from the local {@link org.octobot.script.wrappers.Player}
     */
    public GroundItem[] getLoaded(final Filter<GroundItem> filter, final int range) {
        final List<GroundItem> result = new ArrayList<GroundItem>();

        final Tile centre = context().players.getLocal().getLocation();
        if (centre == null) return null;

        final int minX = centre.getX() - range, minY = centre.getY() - range;
        final int maxX = centre.getX() + range, maxY = centre.getY() + range;
        final int baseX = context().game.getBaseX();
        final int baseY = context().game.getBaseY();
        final int plane = context().game.getPlane();

        final RSNodeList[][][] ground = context().client.getGroundItems();
        if (ground == null) return null;

        final RSSceneGraph graph = context().client.getSceneGraph();
        if (graph == null) return null;

        final RSGroundTile[][][] tiles = graph.getGroundTiles();
        if (tiles == null) return null;

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                final RSGroundTile tile = tiles[plane][x - minX][y - minY];
                final RSNodeList deque = ground[plane][x - minX][y - minY];

                RSNode head;
                if (deque != null && (head = deque.getHead()) != null) {
                    RSNode next = null;

                    int index = 0;
                    while ((next = (next != null ? next.getNext() : head.getNext())) != null && next instanceof RSGroundItem) {
                        final GroundItem item = new GroundItem(context(), // context
                                (RSGroundItem) next, // accessor
                                baseX + (x - minX), // x
                                baseY + (y - minY), // y
                                plane,  // plane
                                tile != null && tile.getItemPile() != null ? -tile.getItemPile().getZ() : 0, // heightOffset
                                index); // pileIndex
                        if (filter.accept(item))
                            result.add(item);

                        index++;
                    }
                }
            }
        }

        return result.toArray(new GroundItem[result.size()]);
    }

    /**
     * This method is used to create a new {@link GroundItemQuery}
     *
     * @return a new {@link GroundItemQuery}
     */
    public final GroundItemQuery find() {
        return new GroundItemQuery(context());
    }

}
