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

import java.util.HashMap;
import java.util.Map;

/**
 * SandwichLadyRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "SandwichLady",
        description = "Handles the SandwichLady random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class SandwichLadyRandom extends RandomScript {
    private static final Map<String, Integer> FOOD_MAP = new HashMap<String, Integer>();

    static {
        FOOD_MAP.put("square", 10731);
        FOOD_MAP.put("roll", 10727);
        FOOD_MAP.put("chocolate", 10728);
        FOOD_MAP.put("baguette", 10726);
        FOOD_MAP.put("triangle", 10732);
        FOOD_MAP.put("kebab", 10729);
        FOOD_MAP.put("pie", 10730);
    }

    private NPC lady;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (lady = context().npcs.find().name("Sandwich lady").interacting(context().players.getLocal()).nearest().first()) != null;
    }

    private Component getAnswer(final int id) {
        for (int i = 1; i < 8; i++) {
            final Component check = context().widgets.getComponent(297, i);
            if (check != null && check.getModelId() == id) return check;
        }

        return null;
    }

    @Override
    public int execute() {
        final Component foodStock = context().widgets.getComponent(297, 8);
        if (foodStock != null) {
            final String text = foodStock.getText();
            if (text != null)
                for (final String foodName : FOOD_MAP.keySet())
                    if (text.contains(foodName)) {
                        final Component answer = getAnswer(FOOD_MAP.get(foodName));
                        if (answer != null && answer.click(true)) return Random.nextInt(1600, 1800);
                    }
        } else if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(500, 700);
        } else {
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
            } else if (lady != null && lady.interact("Talk-to")) {
                Time.sleep(new Condition() {
                    public boolean validate() {
                        return context().widgets.getContinue() == null;
                    }
                }, 2000);
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.SANDWICH_LADY;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
