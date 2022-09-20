package org.octobot.script.methods;

import org.octobot.bot.game.client.*;
import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.collection.GameObjectQuery;
import org.octobot.script.wrappers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * GameObjects
 *
 * @author Pat-ji
 */
public class GameObjects extends ContextProvider {
    public static final int INTERACTABLE_OBJECT = 1;
    public static final int GROUND_DECORATION = 2;
    public static final int WALL_DECORATION = 4;
    public static final int WALL_OBJECT = 8;
    public static final int ALL = INTERACTABLE_OBJECT + GROUND_DECORATION + WALL_DECORATION + WALL_OBJECT;

    public static final Filter<GameObject> ALL_FILTER = new Filter<GameObject>() {
        @Override
        public boolean accept(final GameObject object) {
            return true;
        }
    };

    public GameObjects(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param ids the id of the {@link GameObject}s to get
     * @return a {@link GameObject} with a specific id
     */
    public GameObject getNearest(final int... ids) {
        return getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                return context().calculations.arrayContains(ids, object.getId());
            }
        });
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param names the name of the {@link GameObject}s to get
     * @return a {@link GameObject} with a specific name
     */
    public GameObject getNearest(final String... names) {
        return getNearest(ALL, names);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param mask the mask of the {@link GameObject} to get
     * @param names the name of the {@link GameObject} to get
     * @return a {@link GameObject} with a specific mask and name
     */
    public GameObject getNearest(final int mask, final String... names) {
        return getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                return context().calculations.arrayContains(names, object.getName());
            }
        }, mask);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link GameObject} selected by a {@link Filter}
     */
    public GameObject getNearest(final Filter<GameObject> filter) {
        return getNearest(filter, ALL);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param filter the {@link Filter} to use in the loading
     * @param mask the mask of the {@link GameObject} to get
     * @return a {@link GameObject} selected by a {@link Filter} with a specific mask
     */
    public GameObject getNearest(final Filter<GameObject> filter, final int mask) {
        return getNearest(context().players.getLocal().getLocation(), filter, mask);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param locatable the {@link Locatable} to look at
     * @return a {@link GameObject} on a specific {@link Locatable}
     */
    public GameObject getNearest(final Locatable locatable) {
        return getNearest(locatable.getLocation());
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param tile the {@link Tile} to look at
     * @return a {@link GameObject} on a specific {@link Tile}
     */
    public GameObject getNearest(final Tile tile) {
        return getNearest(tile, ALL_FILTER, ALL);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param tile the {@link Tile} to look at
     * @param mask the mask of the {@link GameObject} to get
     * @return a {@link GameObject} on a specific {@link Tile} with a specific mask
     */
    public GameObject getNearest(final Tile tile, final int mask) {
        return getNearest(tile, ALL_FILTER, mask);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param locatable the {@link Locatable} to look at
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link GameObject} selected by a {@link Filter} at a specific {@link Locatable}
     */
    public GameObject getNearest(final Locatable locatable, final Filter<GameObject> filter) {
        return getNearest(locatable, filter, ALL);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param locatable the {@link Locatable} to look at
     * @param filter the {@link Filter} to use in the loading
     * @param mask the mask of the {@link GameObject} to get
     * @return a {@link GameObject} selected by a {@link Filter} at a specific {@link Locatable} and a specific mask
     */
    public GameObject getNearest(final Locatable locatable, final Filter<GameObject> filter, final int mask) {
        return getNearest(locatable.getLocation(), filter, mask);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param tile the {@link Tile} to look at
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link GameObject} selected by a {@link Filter} at a specific {@link Tile}
     */
    public GameObject getNearest(final Tile tile, final Filter<GameObject> filter) {
        return getNearest(tile, filter, ALL);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param tile the {@link Tile} to look at
     * @param filter the {@link Filter} to use in the loading
     * @param mask the mask of the {@link GameObject} to get
     * @return a {@link GameObject} selected by a {@link Filter} at a specific {@link Tile} and a specific mask
     */
    public GameObject getNearest(final Tile tile, final Filter<GameObject> filter, final int mask) {
        final GameObject[] loaded = getLoaded(filter, mask);
        if (loaded == null || loaded.length < 1) return null;

        GameObject result = null;
        double dist = Float.MAX_VALUE;
        for (final GameObject object : loaded) {
            final double ddist = object.getLocation().distance(tile);
            if (ddist < dist) {
                dist = ddist;
                result = object;
            }
        }

        return result;
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param ids the ids of the {@link GameObject}s to get
     * @return an array with all loaded {@link GameObject}s with specific ids
     */
    public GameObject[] getLoaded(final int... ids) {
        return getLoaded(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                return context().calculations.arrayContains(ids, object.getId());
            }
        });
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param names the names of the {@link GameObject}s to get
     * @return an array with all loaded {@link GameObject}s with specific names
     */
    public GameObject[] getLoaded(final String... names) {
        return getLoaded(ALL, names);
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param mask the mask of the {@link GameObject}s to get
     * @param names the names of the {@link GameObject}s to get
     * @return an array with all loaded {@link GameObject}s with specific names and a specific mask
     */
    public GameObject[] getLoaded(final int mask, final String... names) {
        return getLoaded(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                return context().calculations.arrayContains(names, object.getName());
            }
        }, mask);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param locatable the {@link Locatable} to look at
     * @return a {@link GameObject} at a specific {@link Locatable}
     */
    public GameObject getAt(final Locatable locatable) {
        return getAt(locatable.getLocation());
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param tile the {@link Tile} to look at
     * @return a {@link GameObject} at a specific {@link Tile}
     */
    public GameObject getAt(final Tile tile) {
        return getAt(tile, ALL);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param locatable the {@link Locatable} to look at
     * @param mask the mask of the {@link GameObject} to get
     * @return a {@link GameObject} at a specific {@link Locatable} with a specific mask
     */
    public GameObject getAt(final Locatable locatable, final int mask) {
        return getAt(locatable.getLocation(), mask);
    }

    /**
     * This method is used to get a {@link GameObject}
     *
     * @param tile the {@link Tile} to look at
     * @param mask the mask of the {@link GameObject} to get
     * @return a {@link GameObject} at a specific {@link Tile} with a specific mask
     */
    public GameObject getAt(final Tile tile, final int mask) {
        return getAt(tile.getX(), tile.getY(), mask);
    }

    private GameObject getAt(final int x, final int y, final int mask) {
        final GameObject[] objects = getAllAt(x - context().game.getBaseX(), y - context().game.getBaseY(), mask);
        return objects != null && objects.length > 0 ? objects[0] : null;
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @return an array with all loaded {@link GameObject}s
     */
    public GameObject[] getLoaded() {
        return getLoaded(ALL);
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param mask the mask of the {@link GameObject}s to get
     * @return an array with all loaded {@link GameObject}s with a specific mask
     */
    public GameObject[] getLoaded(final int mask) {
        return getLoaded(ALL_FILTER, mask);
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @return an array with all the loaded {@link GameObject} that are accepted by the {@link Filter}
     */
    public GameObject[] getLoaded(final Filter<GameObject> filter) {
        return getLoaded(filter, ALL);
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @param mask the mask of the {@link GameObject}s to get
     * @return an array with all the loaded {@link GameObject} that are accepted by the {@link Filter} with a specific mask
     */
    public GameObject[] getLoaded(final Filter<GameObject> filter, final int mask) {
        final List<GameObject> result = new ArrayList<GameObject>();

        for (int x = 0; x < 104; x++)
            for (int y = 0; y < 104; y++)
                for (final GameObject object : getAllAt(x, y, mask))
                    if (object != null && filter.accept(object))
                        result.add(object);

        return result.toArray(new GameObject[result.size()]);
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param tile the {@link Tile} to look at
     * @return all loaded {@link GameObject}s at a specific {@link Tile}
     */
    public GameObject[] getAllAt(final Tile tile) {
        return getAllAt(tile, ALL);
    }

    /**
     * This method is used to get all loaded {@link GameObject}s
     *
     * @param tile the {@link Tile} to look at
     * @param mask the mask of the {@link GameObject}s to get
     * @return all loaded {@link GameObject}s at a specific {@link Tile} with a specific mask
     */
    public GameObject[] getAllAt(final Tile tile, final int mask) {
        return getAllAt(tile.getX() - context().game.getBaseX(), tile.getY() - context().game.getBaseY(), mask);
    }

    private GameObject[] getAllAt(final int xOffset, final int yOffset, final int mask) {
        if (xOffset < 0 || yOffset < 0 || xOffset > 103 || yOffset > 103) return null;

        final List<GameObject> result = new ArrayList<GameObject>();

        final int baseX = context().game.getBaseX();
        final int baseY = context().game.getBaseY();

        final RSSceneGraph graph = context().client.getSceneGraph();
        if (graph == null) return null;

        final RSGroundTile tile = graph.getGroundTiles()[context().game.getPlane()][xOffset][yOffset];
        if (tile != null) {
            final int x = xOffset + baseX;
            final int y = yOffset + baseY;

            if ((mask & WALL_OBJECT) == WALL_OBJECT) {
                final RSWallObject wallObject = tile.getWallObject();
                if (wallObject != null)
                    result.add(new WallObject(context(), wallObject, x, y));
            }

            if ((mask & WALL_DECORATION) == WALL_DECORATION) {
                final RSWallDecoration wallDecoration = tile.getWallDecoration();
                if (wallDecoration != null)
                    result.add(new WallDecoration(context(), wallDecoration, x, y));
            }

            if ((mask & GROUND_DECORATION) == GROUND_DECORATION) {
                final RSGroundDecoration groundDecoration = tile.getGroundDecoration();
                if (groundDecoration != null)
                    result.add(new GroundDecoration(context(), groundDecoration, x, y));
            }

            if ((mask & INTERACTABLE_OBJECT) == INTERACTABLE_OBJECT) {
                final RSInteractableObject[] interactiveObjects = tile.getInteractableObjects();
                if (interactiveObjects != null && interactiveObjects.length > 0)
                    for (final RSInteractableObject object : interactiveObjects)
                        if (object != null && (object.getHash() >> 29 & 0x3) == 2)
                            result.add(new InteractableObject(context(), object, x, y));
            }

        }

        return result.toArray(new GameObject[result.size()]);
    }

    /**
     * This method is used to create a new {@link GameObjectQuery}
     *
     * @return a new {@link GameObjectQuery}
     */
    public final GameObjectQuery find() {
        return new GameObjectQuery(context());
    }

}
