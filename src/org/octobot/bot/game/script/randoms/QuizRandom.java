package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.wrappers.Component;

/**
 * QuizRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Quiz",
        description = "Handles the Quiz random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class QuizRandom extends RandomScript {
    private static final int[][] ITEMS = {
            { 6189, 6190 },
            { 6192, 6194 },
            { 6195, 6196 },
            { 6197, 6198 }
    };

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && context().npcs.getNearest("Quiz Master") != null;
    }

    private int getSlotId(final int slot) {
        final Component component = context().widgets.getComponent(191, 5 + slot);
        return component != null ? component.getModelId() : -1;
    }

    @Override
    public int execute() {
        final Component reward = context().widgets.getComponent(228, 2);
        if (reward != null) {
            if (reward.click(true)) return Random.nextInt(600, 800);

            return 100;
        }

        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);
        } else if (getSlotId(0) != -1) {
            int[] valid = null;
            final int[] slots = { getSlotId(0), getSlotId(1), getSlotId(2) };
            for (final int[] items : ITEMS) {
                int count = 0;
                for (final int item : items)
                    for (final int slot : slots)
                        if (slot == item)
                            count++;

                if (count == 2) {
                    valid = items;
                    break;
                }
            }

            if (valid != null) {
                for (int index = 0; index < slots.length; index++) {
                    int count = 0;
                    for (final int id : valid)
                        if (id == slots[index])
                            count++;

                    if (count == 0) {
                        final Component component = context().widgets.getComponent(191, 5 + index);
                        if (component != null && component.click(true)) return Random.nextInt(500, 700);
                    }
                }
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.QUIZ;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
