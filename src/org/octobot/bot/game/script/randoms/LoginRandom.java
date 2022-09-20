package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.bot.handler.TextHandler;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.methods.Game;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;

import java.awt.*;

/**
 * LoginRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Login",
        description = "Handles the Login random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class LoginRandom extends RandomScript {
    private static final Rectangle LOGIN = new Rectangle(230, 310, 140, 20);
    private static final Rectangle CANCEL = new Rectangle(400, 310, 140, 20);
    private static final Rectangle EXISTING_USER = new Rectangle(400, 280, 120, 30);
    private static final Rectangle USERNAME = new Rectangle(320, 250, 100, 9);
    private static final Rectangle PASSWORD = new Rectangle(350, 265, 100, 9);

    private boolean hasUsername, hasPassword;
    private int fails;

    @Override
    public boolean validate() {
        return context().game.getGameState() == Game.State.LOBBY && definition.account != null && !definition.account.name.equals("none");
    }

    private boolean atHighRiskScreen() {
        return context().game.getColorAt(361, 202) == -256 && context().game.getGameState() == Game.State.LOBBY;
    }

    private boolean atLoginScreen() {
        return context().game.getColorAt(480, 290) != -1 && context().game.getGameState() == Game.State.LOBBY;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        fails = 0;
        hasUsername = hasPassword = false;
    }

    @Override
    public int execute() {
        if (fails > 5) {
            System.out.println("[LoginRandom] - Failed more then 5 times to login, stopping script");
            stop();
            return 100;
        }

        if (atHighRiskScreen()) {
            if (context().mouse.move(LOGIN) && context().mouse.click(true))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return atHighRiskScreen();
                    }
                }, 2000);
        } else if (atLoginScreen()) {
            if (context().mouse.move(USERNAME) && context().mouse.click(true)) {
                if (!hasUsername) {
                    Time.sleep(500, 800);
                    if (context().keyboard.sendText(TextHandler.decode(definition.account.name), false))
                        hasUsername = true;
                }

                Time.sleep(300, 500);
                if (context().mouse.move(PASSWORD) && context().mouse.click(true)) {
                    if (!hasPassword) {
                        Time.sleep(300, 500);
                        if (context().keyboard.sendText(TextHandler.decode(definition.account.password), false))
                            hasPassword = true;
                    }

                    if (context().mouse.move(LOGIN) && context().mouse.click(true)) {
                        final Condition condition = new Condition() {
                            @Override
                            public boolean validate() {
                                return context().game.getGameState() != Game.State.LOGGED_IN;
                            }
                        };

                        Time.sleep(condition, 15000);
                        if (condition.validate()) {
                            System.out.println("[LoginRandom] - Failed to login, retry.");
                            do {
                                if (context().mouse.move(CANCEL) && context().mouse.click(true))
                                    Time.sleep(800, 1000);
                            } while (atLoginScreen());

                            fails++;
                        }
                    }
                }
            }
        } else if (context().mouse.move(EXISTING_USER) && context().mouse.click(true)) {
            hasUsername = false;
            hasPassword = false;
            return Random.nextInt(600, 800);
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return RandomEvent.Event.LOGIN;
    }

}
