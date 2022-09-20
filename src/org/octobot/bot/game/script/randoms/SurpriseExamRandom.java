package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.*;

/**
 * SurpriseExamRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "SurpriseExam",
        description = "Handles the SurpriseExam random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class SurpriseExamRandom extends RandomScript {
    private static final Tile BLUE_DOOR_TILE = new Tile(1881, 5030);
    private static final Tile PURPLE_DOOR_TILE = new Tile(1886, 5033);
    private static final Tile GREEN_DOOR_TILE = new Tile(1891, 5019);
    private static final Tile RED_DOOR_TILE = new Tile(1881, 5022);

    private static final int[] RANGE = { 11539, 11540, 11541, 11614, 11615, 11633 };
    private static final int[] COOKING = { 11526, 11529, 11545, 11549, 11550, 11555, 11560,
            11563, 11564, 11607, 11608, 11616, 11620, 11621, 11622, 11623,
            11628, 11629, 11634, 11639, 11641, 11649, 11624 };
    private static final int[] FISHING = { 11527, 11574, 11578, 11580, 11599, 11600, 11601,
            11602, 11603,
            11604, 11605, 11606, 11625 };
    private static final int[] COMBAT = { 11528, 11531, 11536, 11537, 11579, 11591, 11592,
            11593, 11597, 11627, 11631, 11635, 11636, 11638, 11642, 11648,
            11617};
    private static final int[] FARMING = { 11530, 11532, 11547, 11548, 11554, 11556, 11571,
            11581, 11586, 11645 };
    private static final int[] MAGIC = { 11533, 11534, 11538, 11562, 11567, 11582 };
    private static final int[] FIREMAKING = { 11535, 11551, 11552, 11559, 11646 };
    private static final int[] HATS = { 11540, 11557, 11558, 11560, 11570, 11619, 11626,
            11630, 11632, 11637, 11654 };
    private static final int[] PIRATE = { 11570, 11626, 11558, 11626 };
    private static final int[] DRINKS = { 11542, 11543, 11544, 11644, 11647 };
    private static final int[] WOODCUTTING = { 11573, 11595 };
    private static final int[] BOOTS = { 11561, 11618, 11650, 11651 };
    private static final int[] CRAFTING = { 11546, 11553, 11565, 11566, 11568, 11569, 11572,
            11575, 11576, 11577, 11581, 11583, 11584, 11585, 11643, 11652,
            11653 };
    private static final int[] MINING = { 11587, 11588, 11594, 11596, 11598, 11609, 11610, 11612, 11613 };
    private static final int[] SMITHING = { 11611, 11612, 11613 };
    private static final int[][] ITEMS = { RANGE, COOKING, FISHING, COMBAT, FARMING, MAGIC,
            FIREMAKING, HATS, DRINKS, WOODCUTTING, BOOTS, CRAFTING, MINING,
            SMITHING };

    private static final SimilarObjectQuestion[] QUESTIONS = {
            new SimilarObjectQuestion("I never leave the house without some sort of jewellery.", new int[] { 11572, 11576, 11652 }),
            new SimilarObjectQuestion("There is no better feeling than", new int[] { 11572, 11576, 11652 }),
            new SimilarObjectQuestion("I'm feeling dehydrated", DRINKS),
            new SimilarObjectQuestion("All this work is making me thirsty", DRINKS),
            new SimilarObjectQuestion("quenched my thirst", DRINKS),
            new SimilarObjectQuestion("light my fire", FIREMAKING),
            new SimilarObjectQuestion("fishy", FISHING),
            new SimilarObjectQuestion("fishing for answers", FISHING),
            new SimilarObjectQuestion("fish out of water", FISHING),
            new SimilarObjectQuestion("strange headgear", HATS),
            new SimilarObjectQuestion("tip my hat", HATS),
            new SimilarObjectQuestion("thinking cap", HATS),
            new SimilarObjectQuestion("Piracy is a crime,", PIRATE),
            new SimilarObjectQuestion("wizardry here", MAGIC),
            new SimilarObjectQuestion("rather mystical", MAGIC),
            new SimilarObjectQuestion("abracada", MAGIC),
            new SimilarObjectQuestion("hide one's face", HATS),
            new SimilarObjectQuestion("shall unmask", HATS),
            new SimilarObjectQuestion("hand-to-hand", COMBAT),
            new SimilarObjectQuestion("melee weapon", COMBAT),
            new SimilarObjectQuestion("prefers melee", COMBAT),
            new SimilarObjectQuestion("me hearties", PIRATE),
            new SimilarObjectQuestion("puzzle for landlubbers", PIRATE),
            new SimilarObjectQuestion("mighty pirate", PIRATE),
            new SimilarObjectQuestion("mighty archer", RANGE),
            new SimilarObjectQuestion("as an arrow", RANGE),
            new SimilarObjectQuestion("Ranged attack", RANGE),
            new SimilarObjectQuestion("shiny things", CRAFTING),
            new SimilarObjectQuestion("igniting", FIREMAKING),
            new SimilarObjectQuestion("sparks from my synapses.", FIREMAKING),
            new SimilarObjectQuestion("fire.", FIREMAKING),
            new SimilarObjectQuestion("world burn", FIREMAKING),
            new SimilarObjectQuestion("disguised", HATS),
            new SimilarObjectQuestion("garments", HATS),
            new SimilarObjectQuestion("Sea food", FISHING),

            new SimilarObjectQuestion("range", RANGE),
            new SimilarObjectQuestion("arrow", RANGE),
            new SimilarObjectQuestion("drink", DRINKS),
            new SimilarObjectQuestion("logs", FIREMAKING),
            new SimilarObjectQuestion("light", FIREMAKING),
            new SimilarObjectQuestion("headgear", HATS),
            new SimilarObjectQuestion("hat", HATS),
            new SimilarObjectQuestion("cap", HATS),
            new SimilarObjectQuestion("mine", MINING),
            new SimilarObjectQuestion("mining", MINING),
            new SimilarObjectQuestion("ore", MINING),
            new SimilarObjectQuestion("fish", FISHING),
            new SimilarObjectQuestion("fishing", FISHING),
            new SimilarObjectQuestion("thinking cap", HATS),
            new SimilarObjectQuestion("cooking", COOKING),
            new SimilarObjectQuestion("cook", COOKING),
            new SimilarObjectQuestion("bake", COOKING),
            new SimilarObjectQuestion("farm", FARMING),
            new SimilarObjectQuestion("farming", FARMING),
            new SimilarObjectQuestion("cast", MAGIC),
            new SimilarObjectQuestion("magic", MAGIC),
            new SimilarObjectQuestion("craft", CRAFTING),
            new SimilarObjectQuestion("boot", BOOTS),
            new SimilarObjectQuestion("chop", WOODCUTTING),
            new SimilarObjectQuestion("cut", WOODCUTTING),
            new SimilarObjectQuestion("tree", WOODCUTTING),
    };

    private GameObject door;
    private NPC mordraut;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (mordraut = context().npcs.getNearest("Mr. Mordaut")) != null;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        door = null;
    }

    private int[] getNextAnswer() {
        final int[] count = new int[ITEMS.length];
        for (int i = 0; i < ITEMS.length; i++)
            for (int j = 0; j < ITEMS[i].length; j++) {
                if (ITEMS[i][j] == context().widgets.getComponent(103, 8).getModelId())
                    count[i]++;

                if (ITEMS[i][j] == context().widgets.getComponent(103, 9).getModelId())
                    count[i]++;

                if (ITEMS[i][j] == context().widgets.getComponent(103, 10).getModelId())
                    count[i]++;

                if (count[i] >= 2) return ITEMS[i];
            }

        return null;
    }

    @Override
    public int execute() {
        final Player player = context().players.getLocal();
        if (player == null) return 100;

        if (door != null) {
            if (door.isOnGameScreen()) {
                if (door.interact("Open")) return Random.nextInt(1500, 1800);
            } else if (context().movement.walkTileOnMap(door)) {
                return Random.nextInt(500, 800);
            }
        } else {
            final Component doorComponent = context().widgets.getComponent("to exit,");
            if (doorComponent != null) {
                final String text = doorComponent.getText().toLowerCase();
                if (text.contains("red")) {
                    door = context().objects.getAt(RED_DOOR_TILE);
                } else if (text.contains("green")) {
                    door = context().objects.getAt(GREEN_DOOR_TILE);
                } else if (text.contains("blue")) {
                    door = context().objects.getAt(BLUE_DOOR_TILE);
                } else if (text.contains("purple")) {
                    door = context().objects.getAt(PURPLE_DOOR_TILE);
                }

                return Random.nextInt(500, 1000);
            } else {
                final Widget questions = context().widgets.get(103);
                if (questions != null) {
                    final int[] answers = getNextAnswer();
                    for (int i = 12; i <= 15; i++) {
                        final Component component;
                        if (context().calculations.arrayContains(answers, (component = questions.getChild(i)).getModelId()) && component.click(true)) return Random.nextInt(600, 800);
                    }
                } else {
                    final Widget cards = context().widgets.get(559);
                    if (cards != null) {
                        final Component component = cards.getChild(72);
                        for (final SimilarObjectQuestion question : QUESTIONS) {
                            if (question.isValid(component))
                                System.out.println("[SurpriseExamRandom] - " + question.question);

                            if (question.isValid(component) && question.click(context())) {
                                final Component accept = cards.getChild(70);
                                if (accept != null && accept.click(true)) return Random.nextInt(500, 700);
                            }
                        }
                    } else if (context().widgets.getContinue() != null) {
                        if (context().widgets.clickContinue()) return Random.nextInt(600, 800);
                    } else if (mordraut.isOnGameScreen()) {
                        if (mordraut.interact("Talk-to")) {
                            return Random.nextInt(800, 1000);
                        }
                    } else if (context().movement.walkTileOnMap(mordraut)) {
                        return Random.nextInt(500, 700);
                    }
                }
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.SURPRISE_EXAM;
    }

    /**
     * SimilarObjectQuestion
     *
     * @author Pat-ji
     */
    private static class SimilarObjectQuestion {
        public final String question;
        public final int[] answers;

        public SimilarObjectQuestion(final String question, final int[] answers) {
            this.question = question;
            this.answers = answers;
        }

        public boolean isValid(final Component component) {
            return component.getText().toLowerCase().contains(question.toLowerCase());
        }

        public boolean click(final ScriptContext context) {
            int count = 0;
            for (final int answer : answers) {
                if (count >= 3) return true;

                for (int i = 24; i <= 38; i++) {
                    final Component component = context.widgets.getComponent(559, i);
                    if (component != null && component.getModelId() == answer)
                        if (component.click(true)) {
                            Time.sleep(500, 700);
                            count++;
                            break;
                        }
                }
            }

            return true;
        }

    }

}
