package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.GameObject;
import org.octobot.script.wrappers.Model;
import org.octobot.script.wrappers.Tile;

/**
 * PinballRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Pinball",
        description = "Handles the Pinball random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class PinballRandom extends RandomScript {

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && context().objects.getNearest("Pinball Post") != null;
    }

    private int getScore() {
        final Component component = context().widgets.getComponent(263, 1);
        return component != null ? Integer.parseInt(component.getText().split(" ")[1]) : -1;
    }

    private int getVertices() {
        switch (context().settings.get(727)) {
            case 0:
            case 512:
            case 1024:
            case 1536:
            case 2048:
            case 2560:
            case 3072:
            case 3584:
            case 4096:
            case 4608:
                return 278;
            case 34:
            case 546:
            case 1058:
            case 1570:
            case 2082:
            case 2594:
            case 3106:
            case 3618:
            case 4130:
            case 4642:
                return 277;
            case 68:
            case 580:
            case 1092:
            case 1604:
            case 2116:
            case 2628:
            case 3140:
            case 3652:
            case 4164:
            case 4676:
                return 261;
            case 102:
            case 614:
            case 1126:
            case 1638:
            case 2150:
            case 2662:
            case 3174:
            case 3686:
            case 4198:
            case 4710:
                return 266;
            case 136:
            case 648:
            case 1160:
            case 1672:
            case 2184:
            case 2696:
            case 3208:
            case 3720:
            case 4232:
            case 4744:
                return 240;
        }

        return -1;
    }

    private GameObject getPole(final int vertices) {
        final GameObject[] loaded = context().objects.getLoaded("Pinball Post");
        if (loaded != null && loaded.length > 0) {
            Model model;
            int[] triangles;
            for (final GameObject test : loaded) {
                if (test != null && (model = test.getModel()) != null && (triangles = model.getXTriangles()) != null
                        && triangles.length > 0 && triangles[triangles.length - 1] == vertices) return test;
            }
        }

        return null;
    }

    @Override
    public int execute() {
        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        if (getScore() >= 10) {
            final GameObject exit = context().objects.getNearest("Cave Exit");
            if (exit != null)
                if (exit.isOnGameScreen()) {
                    if (exit.interact("Exit")) return Random.nextInt(1200, 1400);
                } else {
                    final Tile location = exit.getLocation();
                    if (location != null) {
                        context().camera.setAngle(180);
                        context().camera.setPitch(0);
                        if (location.getMatrix(context()).interact("Walk here")) return Random.nextInt(500, 700);
                    }
                }
        } else {
            final GameObject pole = getPole(getVertices());
            if (pole != null) {
                if (pole.isOnGameScreen()) {
                    if (pole.interact("Tag"))
                        Time.sleep(new Condition() {
                            @Override
                            public boolean validate() {
                                final GameObject next;
                                return (next = getPole(getVertices())) != null && next.equals(pole);
                            }
                        }, 5000);
                } else {
                    final Tile location = pole.getLocation();
                    if (location != null) {
                        context().camera.turnTo(location);
                        if (location.getMatrix(context()).interact("Walk here")) return Random.nextInt(500, 700);
                    }
                }
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.PINBALL;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
