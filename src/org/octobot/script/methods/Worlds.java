package org.octobot.script.methods;

import org.octobot.bot.game.client.RSWorld;
import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.collection.Filter;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Worlds
 *
 * @author Pat-ji
 */
public class Worlds extends ContextProvider {
    private static final Rectangle WORLD_SELECT = new Rectangle(10, 465, 90, 35);
    public static final Filter<RSWorld> ALL_FILTER = new Filter<RSWorld>() {
        @Override
        public boolean accept(final RSWorld world) {
            return true;
        }
    };

    /*public static final int[][] WORLDS = {
            { 1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12 , 13, 14, 16, 17, 18 },
            { 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 33, 34, 35, 36, 37, 38 },
            { 41, 42, 43, 44, 45, 46, 49, 50, 51, 52, 53, 54, 57, 58, 59, 60 },
            { 61, 62, 65, 66, 67, 68, 69, 70, 73, 74, 75, 76, 77, 78 }
    };*/

    public static final int[][] WORLDS = {
            {  1,  2,  3,  4,  5,  6,  8,  9, 10, 11, 12, 13, 14, 16, 17, 18, 19 },
            { 20, 21, 22, 25, 26, 27, 28, 29, 30, 33, 34, 35, 36, 37, 38, 41, 42 },
            { 43, 44, 45, 46, 49, 50, 51, 52, 53, 54, 57, 58, 59, 60, 61, 62, 65 },
            { 66, 67, 68, 69, 70, 73, 74, 75, 76, 77, 78, 81, 82, 83, 84, 93, 94 }
    };

    public Worlds(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the current world
     *
     * @return the current world
     */
    public int getCurrentWorld() {
        return context().client.getCurrentWorld();
    }

    /**
     * This method is used to check if the world selector is open
     *
     * @return <code>true</code> if the world selector is open
     */
    public boolean isOnWorldSelector() {
        return context().client.isOnWorldSelector();
    }

    /**
     * This method is used to get all the loaded {@link World}s
     *
     * @return all the loaded {@link World}s
     */
    public World[] getAll () {
        return getAll(ALL_FILTER);
    }

    /**
     * This method is used to get all the loaded {@link World}s
     *
     * @param filter the {@link Filter} to use in the loading
     * @return an array with all the loaded {@link World} that are accepted by the {@link Filter}
     */
    public World[] getAll(final Filter<RSWorld> filter) {
        final List<World> result = new ArrayList<World>();
        for (final RSWorld world : context().client.getWorlds())
            if (world != null && filter.accept(world))
                result.add(new World(world));

        return result.toArray(new World[result.size()]);
    }

    /**
     * This method is used to get a {@link World}
     *
     * @param filter the {@link Filter} to use in the loading
     * @return a {@link World} selected by a {@link Filter}
     */
    public World get(final Filter<RSWorld> filter) {
        for (final RSWorld world : context().client.getWorlds())
            if (world != null && filter.accept(world))
                return new World(world);

        return null;
    }

    /**
     * This method is used to get the location of a {@link World}
     *
     * @param world the {@link World} to get the location from
     * @return the location of the {@link World}
     */
    public Point getWorldLocation(final int world) {
        for (int colom = 0; colom < WORLDS.length; colom++)
            for (int row = 0; row < WORLDS[colom].length; row++)
                if (WORLDS[colom][row] == world || WORLDS[colom][row] == world - 300) return new Point(215 + (colom * 93), 69 + (row * 24));//return new Point(210 + (colom * 93), 83 + (row * 24));

        return new Point(0, 0);
    }

    /**
     * This method is used to check if the {@link World}s are ordered
     *
     * @return <code>true</code> if the {@link World}s are ordered
     */
    public boolean hasWorldOrdered() {
        return context().game.getColorAt(302, 14) == -13663701;
    }

    /**
     * This method is used to hop to a {@link World} using the regular login screen.
     *
     * @param world the world to hop to
     * @return <code>true</code> if successfully hopped to the world
     */
    public boolean hopTo(final int world) {
        if (getCurrentWorld() == world || getCurrentWorld() == world - 300) return true;

        context().environment.setRandomEnabled(RandomEvent.Event.LOGIN, false);

        boolean hopped = false;
        while (!hopped) {
            if (context().game.isLoggedIn()) {
                context().game.logout();
            } else {
                if (isOnWorldSelector()) {
                    if (hasWorldOrdered()) {
                        final Point point = getWorldLocation(world);
                        if (point != null && context().mouse.click(point.x, point.y, true)) {
                            Time.sleep(new Condition() {
                                @Override
                                public boolean validate() {
                                    return isOnWorldSelector();
                                }
                            }, 2000);

                            if (!isOnWorldSelector())
                                hopped = true;
                        }
                    } else if (context().mouse.click(new Point(302, 14), true)) {
                        Time.sleep(600, 800);
                    }
                } else if (context().mouse.move(WORLD_SELECT) && context().mouse.click(true)) {
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return !isOnWorldSelector();
                        }
                    }, 1500);
                }
            }
        }

        context().environment.setRandomEnabled(RandomEvent.Event.LOGIN, true);

        return getCurrentWorld() == world || getCurrentWorld() == world - 300;
    }

    /**
     * This method is used to hop to a {@link World} almost instantly using disconnect hopping.
     *
     * @param world the world to hop to
     * @return <code>true</code> if successfully hopped to the world
     */
    public boolean quickHopTo(final int world) {
        return false;
    }
}
