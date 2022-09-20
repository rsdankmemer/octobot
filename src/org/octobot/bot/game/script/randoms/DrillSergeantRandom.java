package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.*;

/**
 * DrillSergeantRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "DrillSergeant",
        description = "Handles the DrillSergeant random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class DrillSergeantRandom extends RandomScript {
    private static final Area AREA = new Area(3158, 4823, 3167, 4818);

    private static final Tile MAT_1_TILE = new Tile(3160, 4820);
    private static final Tile MAT_2_TILE = new Tile(3162, 4820);
    private static final Tile MAT_3_TILE = new Tile(3164, 4820);
    private static final Tile MAT_4_TILE = new Tile(3166, 4820);

    private int sign1, sign2, sign3;
    private NPC sergeant;
    private Player local;

    @Override
    public boolean validate() {
        if (!context().game.isLoggedIn()) return false;

        local = context().players.getLocal();
        return local != null && AREA.contains(local) && (sergeant = context().npcs.getNearest("Sergeant Damien")) != null;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        sign1 = sign2 = sign3 = 0;
    }

    @Override
    public int execute() {
        if (local.isMoving() || local.getAnimation() != -1) return Random.nextInt(800, 1000);

        context().camera.setPitch(100);
        context().camera.setAngle(0);

        Component component = context().widgets.getComponent(241, 2);
        if (component == null)
            component = context().widgets.getComponent(242, 2);
        if (component != null) {
            switch (context().settings.get(531)) {
                case 668:
                    sign1 = 1;
                    sign2 = 2;
                    sign3 = 3;
                    break;
                case 675:
                    sign1 = 2;
                    sign2 = 1;
                    sign3 = 3;
                    break;
                case 724:
                    sign1 = 1;
                    sign2 = 3;
                    sign3 = 2;
                    break;
                case 738:
                    sign1 = 3;
                    sign2 = 1;
                    sign3 = 2;
                    break;
                case 787:
                    sign1 = 2;
                    sign2 = 3;
                    sign3 = 1;
                    break;
                case 794:
                    sign1 = 3;
                    sign2 = 2;
                    sign3 = 1;
                    break;
                case 1116:
                    sign1 = 1;
                    sign2 = 2;
                    sign3 = 4;
                    break;
                case 1123:
                    sign1 = 2;
                    sign2 = 1;
                    sign3 = 4;
                    break;
                case 1228:
                    sign1 = 1;
                    sign2 = 4;
                    sign3 = 2;
                    break;
                case 1249:
                    sign1 = 4;
                    sign2 = 1;
                    sign3 = 2;
                    break;
                case 1291:
                    sign1 = 2;
                    sign2 = 4;
                    sign3 = 1;
                    break;
                case 1305:
                    sign1 = 4;
                    sign2 = 2;
                    sign3 = 1;
                    break;
                case 1620:
                    sign1 = 1;
                    sign2 = 3;
                    sign3 = 4;
                    break;
                case 1634:
                    sign1 = 3;
                    sign2 = 1;
                    sign3 = 4;
                    break;
                case 1676:
                    sign1 = 1;
                    sign2 = 4;
                    sign3 = 3;
                    break;
                case 1697:
                    sign1 = 4;
                    sign2 = 1;
                    sign3 = 3;
                    break;
                case 1802:
                    sign1 = 3;
                    sign2 = 4;
                    sign3 = 1;
                    break;
                case 1809:
                    sign1 = 4;
                    sign2 = 3;
                    sign3 = 1;
                    break;
                case 2131:
                    sign1 = 2;
                    sign2 = 3;
                    sign3 = 4;
                    break;
                case 2138:
                    sign1 = 3;
                    sign2 = 2;
                    sign3 = 4;
                    break;
                case 2187:
                    sign1 = 2;
                    sign2 = 4;
                    sign3 = 3;
                    break;
                case 2201:
                    sign1 = 4;
                    sign2 = 2;
                    sign3 = 3;
                    break;
                case 2250:
                    sign1 = 3;
                    sign2 = 4;
                    sign3 = 2;
                    break;
                case 2257:
                    sign1 = 4;
                    sign2 = 3;
                    sign3 = 2;
                    break;
            }

            final String text = component.getText();
            if (text.contains("star")) {
                if (sign1 == 1) {
                    useMat(context().objects.getAt(MAT_1_TILE));
                } else if (sign2 == 1) {
                    useMat(context().objects.getAt(MAT_2_TILE));
                } else if (sign3 == 1) {
                    useMat(context().objects.getAt(MAT_3_TILE));
                } else {
                    useMat(context().objects.getAt(MAT_4_TILE));
                }
            } else if (text.contains("push ups")) {
                if (sign1 == 2) {
                    useMat(context().objects.getAt(MAT_1_TILE));
                } else if (sign2 == 2) {
                    useMat(context().objects.getAt(MAT_2_TILE));
                } else if (sign3 == 2) {
                    useMat(context().objects.getAt(MAT_3_TILE));
                } else {
                    useMat(context().objects.getAt(MAT_4_TILE));
                }
            } else if (text.contains("sit ups")) {
                if (sign1 == 3) {
                    useMat(context().objects.getAt(MAT_1_TILE));
                } else if (sign2 == 3) {
                    useMat(context().objects.getAt(MAT_2_TILE));
                } else if (sign3 == 3) {
                    useMat(context().objects.getAt(MAT_3_TILE));
                } else {
                    useMat(context().objects.getAt(MAT_4_TILE));
                }
            } else if (text.contains("jog on")) {
                if (sign1 == 4) {
                    useMat(context().objects.getAt(MAT_1_TILE));
                } else if (sign2 == 4) {
                    useMat(context().objects.getAt(MAT_2_TILE));
                } else if (sign3 == 4) {
                    useMat(context().objects.getAt(MAT_3_TILE));
                } else {
                    useMat(context().objects.getAt(MAT_4_TILE));
                }
            } else if (clickContinue()) {
                return Random.nextInt(500, 700);
            }
        } else if (sergeant != null) {
            if (sergeant.isOnGameScreen()) {
                if (sergeant.interact("Talk-to")) return Random.nextInt(500, 700);
            } else if (context().movement.walkTileOnMap(sergeant)) {
                return Random.nextInt(500, 700);
            }
        }

        return 100;
    }

    private void useMat(final GameObject mat) {
        if (mat != null)
            if (mat.isOnGameScreen()) {
                if (mat.interact("Use", "Exercise mat"))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return local.getAnimation() == -1;
                        }
                    }, 1500);
            } else if (context().movement.walkTileOnMap(mat)) {
                Time.sleep(Random.nextInt(500, 700));
            }
    }

    private boolean clickContinue() {
        return context().widgets.getContinue() != null && context().widgets.clickContinue();
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.DRILL_SERGEANT;
    }

}
