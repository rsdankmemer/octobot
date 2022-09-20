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
 * PrisonPeteRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "PrisonPete",
        description = "Handles the PrisonPete random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class PrisonPeteRandom extends RandomScript {
    private final Filter<NPC> animalFilter = new Filter<NPC>() {
        @Override
        public boolean accept(final NPC npc) {
            final Model model = npc.getModel();
            final int[] triangles;
            return model != null && (triangles = model.getXTriangles()) != null && triangles.length > 0 && triangles[triangles.length - 1] == modelCount;
        }
    };

    private NPC pete;
    private GameObject lever;

    private int droppedItem = -1, setting = 0, modelCount = -1;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (pete = context().npcs.getNearest("Prison Pete")) != null && (lever = context().objects.getNearest("Lever")) != null;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        modelCount = droppedItem = -1;
        setting = 0;
    }

    @Override
    public int execute() {
        if (!context().tabs.isOpen(Tabs.Tab.INVENTORY)) {
            if (context().tabs.open(Tabs.Tab.INVENTORY)) return Random.nextInt(600, 800);

            return 100;
        }

        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        if (setting != context().settings.get(638))
            modelCount = -1;

        if ((setting = context().settings.get(638)) == 96) {
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
                final Tile walk = pete.getLocation();
                if (context().movement.walkTileOnMap(new Tile(walk.getX() + 10, walk.getY()))) return Random.nextInt(1300, 1600);
            }
        } else {
            final Component check = context().widgets.getComponent(273, 3);
            if (check != null && check.isVisible()) {
                switch (check.getModelId()) {
	                case 11028:
                    case 10752:
                        modelCount = 163;
                        break;
	                case 11034:
                    case 10749:
                        modelCount = 115;
                        break;
                    case 10750:
                        modelCount = 107;
                        break;
                    case 10751:
                        modelCount = 139;
                        break;
                }

                final Component close = context().widgets.getComponent(273, 4);
                if (close != null && close.isVisible() && close.click(true)) return Random.nextInt(600, 800);
            } else {
                if (context().inventory.getItem("Prison key") == null && context().inventory.isFull()) {
                    final Item item = context().inventory.getItemAt(1);
                    if (item != null) {
                        if (droppedItem == -1)
                            droppedItem = item.getId();

                        if (item.interact("Drop")) return Random.nextInt(400, 600);
                    }
                } else if (context().inventory.getItem("Prison key") != null) {
                    if (pete.isOnGameScreen()) {
                        if (pete.interact("Talk-to")) return Random.nextInt(800, 1000);
                    } else if (context().movement.walkTileOnMap(pete)) {
                        return Random.nextInt(600, 800);
                    }
                } else {
                    if (modelCount != -1) {
                        final NPC animal = context().npcs.getNearest(animalFilter);
                        if (animal != null && pete.getAnimation() == -1)
                            if (animal.isOnGameScreen()) {
                                if (animal.interact("Pop"))
                                    Time.sleep(new Condition() {
                                        @Override
                                        public boolean validate() {
                                            return context().inventory.getItem("Prison key") == null;
                                        }
                                    }, 2000);
                            } else if (context().movement.walkTileOnMap(animal)) {
                                return Random.nextInt(600, 800);
                            }
                    } else {
                        if (lever.isOnGameScreen()) {
                            if (lever.interact("Pull"))
                                Time.sleep(new Condition() {
                                    @Override
                                    public boolean validate() {
                                        return context().widgets.getComponent(273, 4) == null;
                                    }
                                }, 2000);
                        } else if (context().movement.walkTileOnMap(lever)) {
                            return Random.nextInt(600, 800);
                        }
                    }
                }
            }
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.PRISON_PETE;
    }

}
