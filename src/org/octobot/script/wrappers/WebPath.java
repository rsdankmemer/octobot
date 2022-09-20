package org.octobot.script.wrappers;

import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.methods.Game;
import org.octobot.script.methods.GameObjects;
import org.octobot.script.util.Time;

import java.util.Arrays;

/**
 * WebPath
 *
 * @author Pat-ji
 */
public class WebPath extends Path {
    private static final String[] WRONG_OBSTACLE_NAMES = { "Chest", "Coffin", "Tree", "Evergreen", "Oak", "Willow", "Teak", "Yew", "Maple tree", "Mahogany tree", "Magic tree" };
    private static final String[] OBSTACLE_ACTIONS = { "Climb", "Climb-up", "Climb-down", "Enter", "Open", "Pass-through",
            "Go-through", "Walk-down", "Walk-up", "Walk-through", "Chop-down", "Jump-from", "Walk-across", "Leave" };

    private final boolean[] traversed;

    private int index;
    private WebNode last;

    public WebPath(final ScriptContext context, final Tile[] tiles) {
        super(context, tiles);

        traversed = new boolean[tiles.length];
    }

    /**
     * this method is used to get the next {@link Tile} on the {@link WebPath}
     *
     * @return the next {@link Tile} on the {@link WebPath}
     */
    @Override
    public Tile getNext() {
        for (int i = 0; i < tiles.length; i++)
            if (!traversed[i]) {
                index = i;
                return (next = tiles[i]);
            }

        return null;
    }

    /**
     * This method is used to traverse the webpath
     *
     * @param range the maximum randomized distance to the next {@link Tile} on the {@link WebPath}
     * @return <code>true</code> if the client has successfully traversed to the next {@link Tile}
     */
    @Override
    public boolean traverse(final int range) {
        if (context.game.getGameState() != Game.State.LOGGED_IN)
            Time.sleep(1000, 1200);

        if (run && !context.movement.isRunEnabled() && context.movement.getEnergy() > 50 && context.widgets.getClosableWidget() == null) {
            if (context.movement.setRun(true))
                Time.sleep(400, 600);

            return traverse(range);
        }

        final Tile location = context.players.getLocal().getLocation();
        if (location == null) return false;

        if (last == null)
            last = (WebNode) tiles[index];

        final double distance = tiles[index].distance(location);
        if (distance < 8) {
            traversed[index] = true;
            last = (WebNode) tiles[index];
            return true;
        } else if (distance > 40 || tiles[index].getPlane() != last.getPlane()) {
            System.out.println("[WebPath] - Plane or object traversal detected.");

            if (last instanceof WebNode.EventNode && ((WebNode.EventNode) last).traversable()) return ((WebNode.EventNode) last).traverse();

            final GameObject[] obstacles = getObjects();
            if (obstacles != null) {
                for (final GameObject obstacle : obstacles) {
                    if (tiles[Math.max(0, index - 1)].distance(obstacle.getLocation()) < 3.0) {
                        if (obstacle.isOnGameScreen()) {
                            if (obstacle.interact(getAction(obstacle.getActions())))
                                Time.sleep(1000, 1200);

                            return true;
                        } else if (context.movement.walkTileOnMap(obstacle)) {
                            return true;
                        }
                    }
                }
            }

            return true;
        }

        final int[][] realFlags = context.client.getCollisionMaps()[next.getPlane()].getFlags();
        final int[][] changedFlags = new int[realFlags.length][realFlags[0].length];
        for (int i = 0; i < changedFlags.length; i++)
            changedFlags[i] = Arrays.copyOf(realFlags[i], realFlags[i].length);

        if (next instanceof WebNode.EventNode && ((WebNode.EventNode) next).traversable()) return ((WebNode.EventNode) next).traverse();

        final Path path = context.movement.findLocalPath(location, next, openFlags(changedFlags));
        if (path.validate()) {
            final GameObject obstacle = getNextObstacle(path.getTiles());
            if (obstacle != null && obstacle.isOnGameScreen()) {
                obstacle.interact(getAction(obstacle.getActions()));
                return true;
            } else {
                return path.traverse(range);
            }
        }

        return context.movement.walkTileOnMap(next);
    }

    private String getAction(final String[] actions) {
        if (actions == null || actions.length == 0) return "";

        final int plane = context.game.getPlane();
        final int nextPlane = tiles[index].getPlane();
        if (plane == nextPlane) return actions[0];

        if (nextPlane > plane) {
            for (final String action : actions)
                if (action.toLowerCase().contains("up")) return action;
        } else if (nextPlane < plane) {
            for (final String action : actions)
                if (action.toLowerCase().contains("down")) return action;
        }

        return actions[0];
    }

    /**
     * This method is used to give the next obstacle on the {@link WebPath}
     *
     * @param tiles the tiles that are checked for objects
     * @return the first obstacle found on the {@link WebPath}
     */
    public GameObject getNextObstacle(final Tile[] tiles) {
        final GameObject[] objects = getObjects();
        if (objects == null || objects.length <= 0) return null;
        for (int i = 0; i < tiles.length - 1; i++)
            for (final GameObject object : objects)
                if (tiles[i].equals(object.getLocation()) && !context.movement.canReach(tiles[i + 1])) return object;

        return null;
    }

    /**
     * This method is used to get all the possible obstacles on the {@link WebPath}
     *
     * @return all the possible obstacles on the {@link WebPath}
     */
    public GameObject[] getObjects() {
        return context.objects.getLoaded(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                final ObjectDefinition def = object.getDefinition();
                if (def == null) return false;

                final String[] actions = def.getActions();
                return !(actions == null || actions.length <= 0) && !context.calculations.arrayContains(WRONG_OBSTACLE_NAMES, def.getName())
                        && context.calculations.arrayContains(OBSTACLE_ACTIONS, actions[0]);
            }
        }, GameObjects.INTERACTABLE_OBJECT + GameObjects.WALL_OBJECT + GameObjects.GROUND_DECORATION);
    }

    private int[][] openFlags(final int[][] flags) {
        final int xBase = context.game.getBaseX();
        final int yBase = context.game.getBaseY();
        for (final GameObject object : getObjects())
            if (object != null) {
                int x = object.getX() - xBase; int y = object.getY() - yBase;
                if (x < 0 || x >= 103 || y < 0 || y >= 103) continue;

                if ((flags[x][y] & 0x2) == 2)
                    flags[x][(y + 1)] = 0;
                if ((flags[x][y] & 0x20) == 32)
                    flags[x][(y - 1)] = 0;
                if ((flags[x][y] & 0x8) == 8)
                    flags[(x + 1)][y] = 0;
                if ((flags[x][y] & 0x80) == 128)
                    flags[(x - 1)][y] = 0;

                flags[x][y] = 0;
            }

        return flags;
    }

    /**
     * This method is used to reserve all the {@link Tile}s on the {@link WebPath}
     *
     * @return the reversed {@link WebPath}
     */
    @Override
    public WebPath reverse() {
        super.reverse();
        for (int i = 0; i < traversed.length; i++)
            traversed[i] = false;

        return this;
    }

}
