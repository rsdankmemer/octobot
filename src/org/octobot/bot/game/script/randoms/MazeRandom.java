package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.collection.Filter;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.event.listeners.PaintListener;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.GameObject;
import org.octobot.script.wrappers.Path;
import org.octobot.script.wrappers.Player;
import org.octobot.script.wrappers.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MazeRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Maze",
        description = "Handles the Maze random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class MazeRandom extends RandomScript implements PaintListener {
    private final List<Tile> doors;
    private final List<Tile> invalidDoors;

    private GameObject shrine;

    public MazeRandom() {
        doors = new ArrayList<Tile>();
        invalidDoors = new ArrayList<Tile>();
    }

    @Override
    public boolean validate() {
        return (shrine = context().objects.getNearest("Strange shrine")) != null;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        doors.clear();
        invalidDoors.clear();
    }

    @Override
    public int execute() {
        final Player local = context().players.getLocal();
        if (local == null) return 100;

        if (local.getLocation().distance(shrine) < 2) {
            if (shrine.isOnGameScreen()) {
                if (shrine.interact("Leave"))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return shrine.isValid();
                        }
                    }, 4000);
            } else {
                context().camera.turnTo(shrine);
            }

            return 100;
        }

        if (doors.isEmpty()) {
            System.out.println("[MazeRandom] - Doors are empty, trying to fill.");
            loadDoors();
            return 100;
        } else {
            final Tile next = doors.get(0);
            System.out.println("[MazeRandom] - Next is " + next + ".");

            final GameObject door = context().objects.getAt(next);
            if (door == null) {
                System.out.println("[MazeRandom] - Door is null.");
                return 100;
            }

            if (door.isOnGameScreen()) {
                System.out.println("[MazeRandom] - Opening the next door.");
                if (door.interact("Open")) {
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return !local.isMoving();
                        }
                    }, 2000);

                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return local.isMoving();
                        }
                    }, 2000);

                    return Random.nextInt(1200, 1400);
                }
            } else if (context().movement.walkTileOnMap(door)) {
                return Random.nextInt(600, 800);
            }
        }

        return 100;
    }

    private void loadDoors() {
        final GameObject[] doors = context().objects.getLoaded(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                final int[] triangles;
                return object.getModel() != null && (triangles = object.getModel().getXTriangles()) != null && triangles.length > 0 && triangles[triangles.length - 1] == 192;
            }
        });

        if (doors == null || doors.length < 0) {
            System.out.println("[MazeRandom] - No doors found.");
            return;
        }

        final int xBase = context().game.getBaseX();
        final int yBase = context().game.getBaseY();

        final int[][] flags = context().movement.getCollisionFlags(context().game.getPlane());
        final List<Tile> doorTiles = new ArrayList<Tile>();
        for (final GameObject door : doors) {
            final Tile doorLocation = door.getLocation();
            if (invalidDoors.contains(doorLocation)) continue;

            System.out.println("[MazeRandom] - Removing flag from " + doorLocation + ".");
            //flags[doorLocation.getX() - xBase][doorLocation.getY() - yBase] = 0;
            //flags[doorLocation.getX() - xBase - 1][doorLocation.getY() - yBase - 1] = 0;
            //flags[doorLocation.getX() - xBase - 1][doorLocation.getY() - yBase + 1] = 0;
            //flags[doorLocation.getX() - xBase + 1][doorLocation.getY() - yBase - 1] = 0;
            //flags[doorLocation.getX() - xBase + 1][doorLocation.getY() - yBase + 1] = 0;
            doorTiles.add(doorLocation);
        }

        final Path path = context().movement.findLocalPath(context().players.getLocal(), shrine.getLocation(), flags, true);
        if (path == null) {
            System.out.println("[MazeRandom] - Path is null.");
            return;
        } else if (path.getTiles() == null || path.getTiles().length < 1) {
            System.out.println("[MazeRandom] - Not enough tiles found.");
            return;
        }

        for (final Tile tile : path.getTiles())
            if (doorTiles.contains(tile)) {
                System.out.println("[MazeRandom] - Door found at " + tile + ", adding to the doors.");
                this.doors.add(tile);
            }
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.MAZE;
    }

    @Override
    public void render(final Graphics g) {
        for (final Tile door : doors) {
            final Polygon bounds = door.getMatrix(context()).getBounds();
            if (bounds != null)
                g.fillPolygon(bounds);
        }
    }

}
