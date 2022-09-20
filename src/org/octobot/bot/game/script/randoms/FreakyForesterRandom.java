package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.Condition;
import org.octobot.script.collection.Filter;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.methods.Tabs;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.*;

/**
 * FreakyForesterRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "FreakyForester",
        description = "Handles the FreakyForester random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class FreakyForesterRandom extends RandomScript {
    private boolean leave = false;
    private int tails = 0;
    private int droppedItem = -1;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && context().npcs.getNearest("Freaky Forester") != null && context().objects.getNearest("Exit portal") != null;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        leave = false;
        tails = 0;
        droppedItem = -1;
    }

    @Override
    public int execute() {
        if (!context().tabs.isOpen(Tabs.Tab.INVENTORY)) {
            if (context().tabs.open(Tabs.Tab.INVENTORY)) return Random.nextInt(600, 800);

            return 100;
        }

        Item selectedItem = context().inventory.getSelectedItem();
        if (selectedItem != null) {
            if (selectedItem.interact("Cancel")) return Random.nextInt(600, 800);

            return 100;
        }

        if (leave) {
            if (droppedItem != -1) {
                final GroundItem item = context().groundItems.getNearest(droppedItem);
                if (item == null) {
                    droppedItem = -1;
                } else if (item.isOnGameScreen()) {
                    if (item.interact("Take")) return Random.nextInt(700, 900);
                } else if (context().movement.walkTileOnMap(item)) {
                    return Random.nextInt(700, 900);
                }
            } else {
                final GameObject portal = context().objects.getNearest("Exit portal");
                if (portal != null)
                    if (portal.isOnGameScreen()) {
                        if (portal.interact("Use")) return Random.nextInt(1700, 1900);
                    } else if (context().movement.walkTileOnMap(portal)) {
                        return Random.nextInt(700, 900);
                    }
            }
        } else {
            final Component leave = context().widgets.getComponent("you can leave");
            if (leave != null) {
                this.leave = true;
                return 10;
            } else {
                if (context().inventory.isFull() && !context().inventory.contains("Raw pheasant")) {
                    final Item item = context().inventory.getItemAt(1);
                    if (item != null) {
                        if (droppedItem == -1)
                            droppedItem = item.getId();

                        if (item.interact("Drop")) return Random.nextInt(400, 600);
                    }
                } else if (tails == 0) {
                    int widget = 242;
                    Component check = context().widgets.getComponent(widget, 2);
                    if (check == null)
                        check = context().widgets.getComponent((widget = 243), 2);

                    if (check != null) {
                        final String face = check.getText().toLowerCase() + " " + context().widgets.getComponent(widget, 3).getText().toLowerCase();
                        if (face.contains("one") || face.contains("1")) {
                            tails = 174;
                        } else if (face.contains("two") || face.contains("2")) {
                            tails = 163;
                        } else if (face.contains("three") || face.contains("3")) {
                            tails = 212;
                        } else if (face.contains("four") || face.contains("4")) {
                            tails = 160;
                        }

                        System.out.println("[FreakyForesterRandom] - Detected tails: " + tails);

                        if (tails != 0) return 10;
                    } else {
                        final NPC forester = context().npcs.getNearest("Freaky Forester");
                        if (forester != null)
                            if (forester.isOnGameScreen()) {
                                if (forester.interact("Talk-to") || forester.interact("Talk-To")) return Random.nextInt(700, 900);
                            } else if (context().movement.walkTileOnMap(forester)) {
                                return Random.nextInt(600, 800);
                            }
                    }
                } else {
                    if (context().inventory.contains("Raw pheasant")) {
                        final NPC forester = context().npcs.getNearest("Freaky Forester");
                        if (forester != null)
                            if (forester.isOnGameScreen()) {
                                if (forester.interact("Talk-to") || forester.interact("Talk-To")) {
                                    Time.sleep(new Condition() {
                                        @Override
                                        public boolean validate() {
                                            return context().inventory.contains("Raw pheasant");
                                        }
                                    }, 5000);

                                    if (!context().inventory.contains("Raw pheasant"))
                                        this.leave = true;

                                    return Random.nextInt(700, 900);
                                }
                            } else if (context().movement.walkTileOnMap(forester)) {
                                return Random.nextInt(600, 800);
                            }
                    } else {
                        final GroundItem item = context().groundItems.getNearest("Raw pheasant");
                        if (item != null) {
                            if (item.isOnGameScreen()) {
                                if (item.interact("Take")) return Random.nextInt(600, 800);
                            } else if (context().movement.walkTileOnMap(item)) {
                                return Random.nextInt(600, 800);
                            }
                        } else if (context().players.getLocal().getAnimation() == -1) {
                            final NPC pheasant = context().npcs.getNearest(new Filter<NPC>() {
                                @Override
                                public boolean accept(final NPC npc) {
                                    final Model model = npc.getModel();
                                    final int[] triangles;
                                    return model != null && (triangles = model.getXTriangles()) != null && triangles.length > 0 && triangles[triangles.length - 1] == tails;
                                }
                            });

                            if (pheasant != null)
                                if (pheasant.isOnGameScreen()) {
                                    if (pheasant.interact("Attack")) return Random.nextInt(600, 800);
                                } else if (context().movement.walkTileOnMap(pheasant)) {
                                    return Random.nextInt(600, 800);
                                }
                        }
                    }
                }
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.FREAKY_FORESTER;
    }

}
