package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.event.RandomEvent;

/**
 * WorldSwitchRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "WorldSwitch",
        description = "Handles the WorldSwitch random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class WorldSwitchRandom extends RandomScript {
    private int lastWorld;

    @Override
    public boolean validate() {
        if (lastWorld < 300) {
            lastWorld = context().worlds.getCurrentWorld();
            System.out.println("[WorldSwitchRandom] - Setting lastWorld to: " + lastWorld + ".");
        }

        return false;//(context().worlds.getCurrentWorld() == 385 || context().worlds.getCurrentWorld() == 386) && definition.account != null && !definition.account.name.equals("none");
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        lastWorld = 0;
    }

    @Override
    public int execute() {
        return context().worlds.hopTo(lastWorld) ? 10 : 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return RandomEvent.Event.WORLD_SWITCH;
    }

}
