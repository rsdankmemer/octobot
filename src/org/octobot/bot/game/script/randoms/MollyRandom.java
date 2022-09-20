package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.collection.Filter;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.*;

import java.util.ArrayList;
import java.util.List;

/**
 * MollyRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "Molly",
        description = "Handles the Molly random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class MollyRandom extends RandomScript {
    private NPC molly;
    private GameObject panel;

    private boolean caught;
    private int modelCount = -1;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && ((molly = context().npcs.getNearest("Molly")) != null
                || modelCount != -1) && (panel = context().objects.getNearest("Control panel")) != null;
    }

    private boolean inControlRoom(final Player player) {
        final GameObject door = context().objects.getNearest("Door");
        return door != null && player.getLocation().getX() > door.getX();
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        modelCount = -1;
        caught = false;
    }

    @Override
    public int execute() {
        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        final Player local = context().players.getLocal();
        if (local == null) return 100;

        final Component component = context().widgets.getComponent(228, 1);
        if (component != null) {
            component.interact("Continue");
            return Random.nextInt(600, 800);
        }

        if (modelCount == -1) {
            final Model model = molly.getModel();
            if (model != null)
                modelCount = model.getXTriangles()[model.getXTriangles().length - 1];
        }

        if (caught) {
            if (inControlRoom(local)) {
                final GameObject door = context().objects.getNearest("Door");
                if (door != null)
                    if (door.isOnGameScreen()) {
                        if (door.interact("Open"))
                            Time.sleep(new Condition() {
                                @Override
                                public boolean validate() {
                                    return inControlRoom(local);
                                }
                            }, 4000);
                    } else {
                        context().camera.turnTo(door);
                    }
            } else if (molly != null) {
                if (molly.isOnGameScreen()) {
                    if (molly.interact("Talk-to")) return Random.nextInt(700, 800);
                } else if (context().movement.walkTileOnMap(molly)) {
                    return Random.nextInt(500, 700);
                }
            }
        } else {
            if (inControlRoom(local)) {
                if ((molly = context().npcs.getNearest("Molly")) != null) {
                    Time.sleep(1000, 1200);
                    final NPC[] suspects;
                    if (((suspects = context().npcs.getLoaded("Suspect")) != null && suspects.length < 2) || suspects == null) {
                        caught = true;
                        return 100;
                    }
                }

                final Component clawControl = context().widgets.getComponent(240, 28);
                if (clawControl != null) {
                    final GameObject claw = context().objects.getNearest("Evil claw");
                    if (claw != null) {
                        final NPC suspect = context().npcs.getNearest(new Filter<NPC>() {
                            @Override
                            public boolean accept(final NPC npc) {
                                final Model model = npc.getModel();
                                final int[] triangles;
                                return model != null && (triangles = model.getXTriangles()) != null && triangles.length > 0 && triangles[triangles.length - 1] == modelCount;
                            }
                        });

                        if (suspect != null) {
                            final List<Integer> options = new ArrayList<Integer>();
                            if (suspect.getX() > claw.getX())
                                options.add(31);

                            if (suspect.getX() < claw.getX())
                                options.add(32);

                            if (suspect.getY() > claw.getY())
                                options.add(30);

                            if (suspect.getY() < claw.getY())
                                options.add(29);

                            if (options.isEmpty())
                                options.add(28);

                            final Component control = context().widgets.getComponent(240, options.get(Random.nextInt(0, options.size())));
                            if (control != null && control.click(true)) return Random.nextInt(500, 700);
                        }
                    }
                } else {
                    context().camera.setPitch(100);
                    if (panel.isOnGameScreen()) {
                        if (panel.interact("Use"))
                            Time.sleep(new Condition() {
                                @Override
                                public boolean validate() {
                                    return context().widgets.getComponent(240, 7) == null;
                                }
                            }, 3000);
                    } else {
                        context().camera.turnTo(panel);
                    }
                }
            } else {
                final GameObject door = context().objects.getNearest("Door");
                if (door != null && door.interact("Open"))
                    Time.sleep(new Condition() {
                        @Override
                        public boolean validate() {
                            return inControlRoom(local);
                        }
                    }, 3000);
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.MOLLY;
    }

}
