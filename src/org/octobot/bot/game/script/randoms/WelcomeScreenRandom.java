package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.wrappers.Component;

/**
 * WelcomeScreenRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "WelcomeScreen",
        description = "Handles the WelcomeScreen random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class WelcomeScreenRandom extends RandomScript {
    private Component component;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (component = context().widgets.getComponent(378, 6)) != null && component.isVisible();
    }

    @Override
    public int execute() {
        return component != null && component.click(true) ? Random.nextInt(600, 800) : 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return RandomEvent.Event.WELCOME_SCREEN;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
