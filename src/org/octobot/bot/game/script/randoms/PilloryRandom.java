package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.GameObject;
import org.octobot.script.wrappers.Player;
import org.octobot.script.wrappers.Tile;

import java.util.HashMap;
import java.util.Map;

/**
 * PilloryRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Pillory",
        description = "Handles the Pillory random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class PilloryRandom extends RandomScript {
    private static final Map<Integer, String> KEY_MAP = new HashMap<Integer, String>();

    private static final Tile[] CAGE_TILES = {
            new Tile(2608, 3105, 0), new Tile(2606, 3105, 0), new Tile(2604, 3105, 0),
            new Tile(3226, 3407, 0), new Tile(3228, 3407, 0), new Tile(3230, 3407, 0),
            new Tile(2685, 3489, 0), new Tile(2683, 3489, 0), new Tile(2681, 3489, 0)
    };

    static {
        KEY_MAP.put(9753, "Diamond");
        KEY_MAP.put(9754, "Square");
        KEY_MAP.put(9755, "Circle");
        KEY_MAP.put(9756, "Triangle");
    }

    private Player local;

    @Override
    public boolean validate() {
        if (!context().game.isLoggedIn()) return false;

        local = context().players.getLocal();
        if (local != null) {
            final Tile location = local.getLocation();
            for (final Tile tile : CAGE_TILES)
                if (tile.equals(location)) return true;
        }

        return false;
    }

    private int getKey() {
        for (final Map.Entry<Integer, String> key : KEY_MAP.entrySet())
            if (context().widgets.getComponent(189, 2).getModelId() == key.getKey())
                for (int i = 3; i < 6; i++)
                    if (context().widgets.getComponent(189, i).getModelId() == key.getKey() - 4) return i;

        return -1;
    }

    @Override
    public int execute() {
        if (context().widgets.getComponent(189, 2) == null) {
            final GameObject location = context().objects.getAt(local.getLocation().add(0, 1));
            if (location != null && location.interact("Unlock"))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return context().widgets.getComponent(189, 2) == null;
                    }
                }, 5000);
        } else {
            final int key = getKey();
            if (key != -1) {
                if (context().widgets.getComponent(189, (key + 3)).interact("Ok")) return Random.nextInt(1300, 1500);
            } else if (context().widgets.getComponent(189, Random.nextInt(3, 6)).interact("Ok")) {
                return Random.nextInt(1300, 1500);
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.PILLORY;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
