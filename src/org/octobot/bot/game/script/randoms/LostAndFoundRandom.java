package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.collection.Filter;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.wrappers.GameObject;
import org.octobot.script.wrappers.Model;

/**
 * LostAndFoundRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "LostAndFound",
        description = "Handles the LostAndFound random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class LostAndFoundRandom extends RandomScript {
    private GameObject[] objects;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (objects = context().objects.getLoaded("Appendage")) != null && objects.length > 0;
    }

    private GameObject getOddAppendage() {
        int lowest = Integer.MAX_VALUE;
        for (final GameObject object : objects) {
            final Model model = object.getModel();
            if (model != null) {
                final int count = model.getVerticesCount();
                if (count < lowest)
                    lowest = count;
            }
        }

        final int value = lowest;
        return context().objects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject object) {
                final Model model = object.getModel();
                return model != null && value == model.getVerticesCount();
            }
        });
    }

    @Override
    public int execute() {
        final GameObject appendage = getOddAppendage();
        if (appendage != null)
            if (appendage.isOnGameScreen()) {
                if (appendage.interact("Operate"))
                    return Random.nextInt(800, 1000);
            } else if (context().movement.walkTileOnMap(appendage)) {
                return Random.nextInt(600, 800);
            }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.LOST_AND_FOUND;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
