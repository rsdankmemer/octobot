package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.bot.internal.Reward;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.methods.Tabs;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;

/**
 * ExpLampRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "ExpLamp",
        description = "Handles the ExpLamp random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class ExpLampRandom extends RandomScript {
    private Item item;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (item = context().inventory.getItem(2528, 11640)) != null && definition.account != null && definition.account.reward != null;
    }

    @Override
    public int execute() {
        if (!context().tabs.isOpen(Tabs.Tab.INVENTORY)) {
            if (context().tabs.open(Tabs.Tab.INVENTORY)) return Random.nextInt(600, 800);

            return 100;
        }

        final Component confirm = context().widgets.getComponent(134, 26);
        if (confirm != null) {
            final Reward reward = definition.account.reward;
            if (context().settings.get(261) == reward.getSetting()) {
                if (confirm.interact("Ok"))  return Random.nextInt(600, 800);
            } else {
                final Component skill = context().widgets.getComponent(134, reward.getWidget());
                if (skill != null)
                    if (skill.click(true))
                        Time.sleep(new Condition() {
                            @Override
                            public boolean validate() {
                                return context().settings.get(261) != reward.getSetting();
                            }
                        }, 1500);
            }
        } else if (item.interact("Rub") || item.interact("Read")) {
            return Random.nextInt(600, 800);
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return RandomEvent.Event.EXP_LAMP;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
