package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.methods.Magic;
import org.octobot.script.methods.Tabs;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;
import org.octobot.script.wrappers.NPC;

/**
 * JekyllAndHydeRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "JekyllAndHyde",
        description = "Handles the JekyllAndHyde random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class JekyllAndHydeRandom extends RandomScript {
    private static final String[] NAMES = { "Dr Jekyll", "Mr Hyde" };

    private NPC npc;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (npc = context().npcs.find().name(NAMES).interacting(context().players.getLocal()).nearest().first()) != null;
    }

    @Override
    public int execute() {
        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        final Item item = context().inventory.getSelectedItem();
        if (item != null) {
            if (item.interact("Cancel")) return Random.nextInt(600, 800);

            return 100;
        }

        final Magic.Spell spell = context().magic.getSelectedSpell();
        if (spell != null) {
            if (context().tabs.isOpen(Tabs.Tab.MAGIC)) {
                final Component component = context().magic.getComponent(spell);
                if (component != null && component.interact("Cancel")) return Random.nextInt(600, 800);
            } else if (context().tabs.open(Tabs.Tab.MAGIC)) {
                return Random.nextInt(200, 400);
            }

            return 100;
        }

        final Component closable = context().widgets.getClosableWidget();
        if (closable != null) {
            if (closable.click(true))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return context().widgets.getClosableWidget() != null;
                    }
                }, 1200);

            return 100;
        }

        if (npc != null) {
            final String[] actions = npc.getActions();
            if (actions != null && context().calculations.arrayContains(actions, "Attack")
                    && !context().calculations.arrayContains(actions, "Talk-To") && !context().calculations.arrayContains(actions, "Talk-to")) {
                return handler().getRandom("Run from combat").execute();
            } else if (npc.isOnGameScreen()) {
                if (npc.interact("Talk-to") || npc.interact("Talk-To")) return Random.nextInt(600, 800);
            } else if (context().movement.walkTileOnMap(npc)) {
                return Random.nextInt(600, 800);
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.JEKYLL_AND_HYDE;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
