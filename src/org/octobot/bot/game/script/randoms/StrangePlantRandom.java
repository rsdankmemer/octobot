package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.event.MessageEvent;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.event.listeners.MessageListener;
import org.octobot.script.methods.Magic;
import org.octobot.script.methods.Tabs;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;
import org.octobot.script.wrappers.NPC;
import org.octobot.script.wrappers.Tile;

/**
 * StrangePlantRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "StrangePlant",
        description = "Handles the StrangePlant random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class StrangePlantRandom extends RandomScript implements MessageListener {
    private NPC plant;
    private Tile wrongLocation, plantLocation;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (plant = context().npcs.find().name("Strange plant").height(172, 173).animation(-1).range(context().players.getLocal(), 8).nearest().first()) != null
                && (wrongLocation == null || wrongLocation.distance(plantLocation) >= 1);
    }

    @Override
    public int execute() {
        plantLocation = plant.getLocation();

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

        if (plant.isOnGameScreen()) {
            if (plant.interact("Pick")) return Random.nextInt(700, 900);
        } else if (context().movement.walkTileOnMap(plant)) {
            return Random.nextInt(600, 800);
        }

        return 100;
    }

    @Override
    public void messageReceived(final MessageEvent event) {
        final String message = event.getMessage();
        if (message.contains("you pick the fruit")) {
            plant = null;
        } else if (message.contains("unable to pick"))  {
            wrongLocation = plantLocation;
        }
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.STRANGE_PLANT;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
