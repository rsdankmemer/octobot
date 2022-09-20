package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.methods.Tabs;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Actor;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.Item;
import org.octobot.script.wrappers.Widget;

/**
 * MysteriousBoxRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "MysteriousBox",
        description = "Handles the MysteriousBox random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class MysteriousBoxRandom extends RandomScript {
    private static final int[] CIRCLE = { 7005, 7020, 7035 };
    private static final int[] PENTAGON = { 7006, 7021, 7036 };
    private static final int[] SQUARE = { 7007, 7022, 7037 };
    private static final int[] STAR = { 7008, 7023, 7038 };
    private static final int[] TRIANGLE = { 7009, 7024, 7039 };
    private static final int[][] MODELS = {
            { 7010, 7025, 7040 },
            { 7011, 7026, 7041 },
            { 7012, 7027, 7042 },
            { 7013, 7028, 7043 },
            { 7014, 7029, 7044 },
            { 7015, 7030, 7045 },
            { 7016, 7031, 7046 },
            { 7017, 7032, 7047 },
            { 7018, 7033, 7048 },
            { 7019, 7034, 7049 }};

    private Item box;
    private long startTime;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (box = context().inventory.getItem(3062)) != null && !context().players.getLocal().isInteractedBy(Actor.ActorType.NPC);
    }

    private String getAnswer(final Widget widget) {
        String answer = null;
        final String question = getQuestion();
        System.out.println("[MysteriousBoxRandom] - Question: " + question);
        if (question != null) {
            final String[] shapes = getShapes(widget);
            final int[] numbers = getNumbers(widget);
            for (int i = 0; i < shapes.length; i++) {
                final String str = shapes[i] + numbers[i];
                if (str.contains(question))
                    answer = str.replace(question, "");
            }
        }

        return answer;
    }

    private String getQuestion() {
        final Component child = context().widgets.getComponent(190, 6);
        if (child != null) {
            final String text = child.getText().toLowerCase();
            if (text.contains("shape has")) {
                return text.substring(text.indexOf("number ") + 7, text.indexOf("?"));
            } else if (text.contains("number is")) {
                return text.substring(text.indexOf("the ") + 4, text.indexOf("?")).toLowerCase();
            }
        }

        return null;
    }

    private String[] getShapes(final Widget widget) {
        if (widget != null) {
            final String[] shapes = new String[3];
            for (int i = 0; i < 3; i++) {
                final Component child = widget.getChild(i);
                if (child != null) {
                    final int modelId = child.getModelId();
                    if (context().calculations.arrayContains(CIRCLE, modelId)) {
                        shapes[i] = "circle";
                    } else if (context().calculations.arrayContains(PENTAGON, modelId)) {
                        shapes[i] = "pentagon";
                    } else if (context().calculations.arrayContains(STAR, modelId)) {
                        shapes[i] = "star";
                    } else if (context().calculations.arrayContains(SQUARE, modelId)) {
                        shapes[i] = "square";
                    } else if (context().calculations.arrayContains(TRIANGLE, modelId)) {
                        shapes[i] = "triangle";
                    }
                }
            }

            return shapes;
        }

        return null;
    }

    private int[] getNumbers(final Widget widget) {
        if (widget != null) {
            final int[] numbers = new int[3];
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < MODELS.length; j++) {
                    final int modelId = widget.getChild(3 + i).getModelId();
                    if (context().calculations.arrayContains(MODELS[j], modelId))
                        numbers[i] = j;
                }

            return numbers;
        }

        return null;
    }

    @Override
    public int execute() {
        if (!context().tabs.isOpen(Tabs.Tab.INVENTORY)) {
            if (context().tabs.open(Tabs.Tab.INVENTORY)) return Random.nextInt(600, 800);

            return 100;
        }

        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - startTime > 8000) {
            final Component close = context().widgets.getComponent(190, 14);
            if (close != null && close.click(true))
                startTime = 0;
        }

        final Widget widget = context().widgets.get(190);
        if (widget != null) {
            final String answer = getAnswer(widget);
            if (answer != null) {
                System.out.println("[MysteriousBoxRandom] - Answer: " + answer);
                for (int i = 10; i < 13; i++) {
                    final Component comp = widget.getChild(i);
                    if (comp != null) {
                        final String text = comp.getText();
                        System.out.println("[MysteriousBoxRandom] - Text: " + text);
                        if (text != null && text.toLowerCase().contains(answer) && comp.interact("Ok")) {
                            Time.sleep(new Condition() {
                                @Override
                                public boolean validate() {
                                    return context().widgets.get(190) != null;
                                }
                            }, 1500);
                        }
                    }
                }
            }
        } else if (this.box.interact("Open")) {
            Time.sleep(new Condition() {
                @Override
                public boolean validate() {
                    return context().widgets.get(190) == null;
                }
            }, 2000);
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.MYSTERIOUS_BOX;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }
}
