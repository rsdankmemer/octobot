package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.collection.Filter;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.NPC;
import org.octobot.script.wrappers.Widget;

/**
 * FrogCaveRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "FrogCave",
        description = "Handles the FrogCave random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class FrogCaveRandom extends RandomScript {
    private static final int[] HEIGHTS = { 70, 77, 79, 85, 86 };

    private NPC frog;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (frog = context().npcs.find().name("Frog").filter(new Filter<NPC>() {
            @Override
            public boolean accept(final NPC npc) {
                return context().calculations.arrayContains(HEIGHTS, npc.getHeight());
            }
        }).nearest().first()) != null && context().objects.getNearest("Roots") != null;
    }

    private Component getSorry(final Widget widget) {
        for (final Component component : widget.getChildren())
            if (component.getText().contains("sorry")) return component;

        return null;
    }

    @Override
    public int execute() {
        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        final Widget widget = context().widgets.get(228);
        if (widget != null) {
            final Component sorry = getSorry(widget);
            if (sorry != null && sorry.interact("Continue")) return Random.nextInt(700, 900);
        } else {
            if (frog.isOnGameScreen()) {
                if (frog.interact("Talk-to"))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return context().widgets.getContinue() == null && context().widgets.get(228) == null;
                        }
                    }, 4000);
            } else if (context().movement.walkTileOnMap(frog)) {
                return Random.nextInt(600, 800);
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.FROG_CAVE;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
