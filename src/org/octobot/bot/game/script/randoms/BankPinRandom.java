package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.bot.handler.TextHandler;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.wrappers.Component;

/**
 * BankPinRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "BankPin",
        description = "Handles the BankPin random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class BankPinRandom extends RandomScript {
    private static final int[] COMPONENTS = { 16, 18, 20, 22, 24, 26, 28, 30, 32, 34 };

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && context().widgets.getComponent(213, 23) != null && definition.account != null &&
                !definition.account.name.equals("none") && definition.account.pin != null && !definition.account.pin.isEmpty() && !definition.account.pin.equals("none");
    }

    @Override
    public int execute() {
        int state = 0;
        for (int i = 3; i < 7; i++) {
            final Component component = context().widgets.getComponent(213, i);
            if (component != null && component.getText().contains("?"))
                state++;
        }

        state = 4 - state;

        final char next = TextHandler.decode(definition.account.pin).charAt(state);
        final String line = "" + next;
        for (final int index : COMPONENTS) {
            final Component component = context().widgets.getComponent(213, index).getChild(1);
            if (component != null && component.getText().contains(line))
                if (component.click(true)) return Random.nextInt(1000, 1200);
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return RandomEvent.Event.BANK_PIN;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
