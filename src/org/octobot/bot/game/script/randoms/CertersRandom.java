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
import org.octobot.script.wrappers.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * CertersRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Certers",
        description = "Handles the Certers random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class CertersRandom extends RandomScript {
    private static final Map<Integer, String> ITEM_MAP = new HashMap<Integer, String>();
    private static final int[] WIDGET_IDS = { 183, 184 };
    private static final String[] NAMES = { "Niles", "Miles", "Giles" };

    private NPC certer;

    static {
        ITEM_MAP.put(8829, "fish");
        ITEM_MAP.put(8834, "ring");
        ITEM_MAP.put(8836, "sword");
        ITEM_MAP.put(8837, "spade");
        ITEM_MAP.put(8832, "shield");
        ITEM_MAP.put(8828, "axe");
        ITEM_MAP.put(8835, "shears");
        ITEM_MAP.put(8833, "helmet");
    }

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (certer = context().npcs.find().name(NAMES).interacting(context().players.getLocal()).nearest().first()) != null;
    }

    private Widget getWidget() {
        for (final int id : WIDGET_IDS) {
            final Widget widget = context().widgets.get(id);
            if (widget != null) return widget;
        }

        return null;
    }

    @Override
    public int execute() {
        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        final Widget widget = getWidget();
        if (widget != null) {
            final Component check = widget.getChild(7);
            Component solution = null;
            if (check != null) {
                final String name = ITEM_MAP.get(check.getModelId());
                final Component[] components;
                if (name != null && (components = widget.getChildren()) != null)
                    for (int i = 0; i < components.length; i++) {
                        final Component child = components[i];
                        if (child != null && child.getText().toLowerCase().contains(name)) {
                            solution = components[i + 7];
                            break;
                        }
                    }
            }

            if (solution != null && solution.click(true))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return getWidget() != null;
                    }
                }, 2000);
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
                    }, 1500);

                return 100;
            } else if (certer.interact("Talk-to", certer.getName()) || certer.interact("Talk-To", certer.getName())) {
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return getWidget() == null;
                    }
                }, 3000);
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.CERTERS;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
