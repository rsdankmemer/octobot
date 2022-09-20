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
 * ScapeRuneRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "ScapeRune",
        description = "Handles the ScapeRune random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class ScapeRuneRandom extends RandomScript {
    public static final Tile[] STATUE_TILES = { new Tile(2528, 4790), new Tile(2542, 4778), new Tile(2526, 4765), new Tile(2511, 4776) };

    private NPC bob, servant;
    private int statueIndex = -1;
    private boolean canLeave;
    private Tile bobTile;

    private final List<Integer> droppedItems = new ArrayList<Integer>();

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (bobTile != null && bobTile.distance(context().players.getLocal()) <= 30)
                || (bob = context().npcs.getNearest("Evil Bob")) != null && (servant = context().npcs.getNearest("Servant")) != null;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    @Override
    public void onStop() {
        statueIndex = -1;
        canLeave = false;
        bobTile = null;
        droppedItems.clear();
    }

    @Override
    public int execute() {
        if (bobTile == null)
            bobTile = bob.getLocation();

        if (canLeave) {
            if (!droppedItems.isEmpty()) {
                final GroundItem item = context().groundItems.getNearest(droppedItems.get(0));
                if (item == null) {
                    droppedItems.remove(0);
                } else if (item.isOnGameScreen()) {
                    if (item.interact("Take")) return Random.nextInt(700, 900);
                } else if (context().movement.walkTileOnMap(item)) {
                    return Random.nextInt(700, 900);
                }
            } else {
                final GameObject portal = context().objects.getNearest("Portal");
                if (portal != null)
                    if (portal.isOnGameScreen()) {
                        if (portal.interact("Enter")) {
                            final Player local = context().players.getLocal();
                            Time.sleep(new Condition() {
                                @Override
                                public boolean validate() {
                                    return local.getAnimation() == -1;
                                }
                            }, 4000);

                            Time.sleep(new Condition() {
                                @Override
                                public boolean validate() {
                                    return (bob = context().npcs.getNearest("Evil Bob")) != null;
                                }
                            }, 10000);
                        }
                    } else if (context().movement.walkTileOnMap(portal)) {
                        return Random.nextInt(600, 800);
                    }
            }

            return 100;
        }

        if (statueIndex == -1) {
            final Component chat = context().widgets.getComponent(186, 0);
            if (chat != null && chat.isVisible())
                for (int i = 0; i < STATUE_TILES.length; i++)
                    if (STATUE_TILES[i].getMatrix(context()).isOnGameScreen()) {
                        statueIndex = i;
                        break;
                    }
        }

        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        if (statueIndex != -1) {
            if (!context().inventory.contains("Small fishing net")) {
                if (context().inventory.isFull()) {
                    final Item item = context().inventory.getItemAt(1);
                    if (item != null) {
                        final int id = item.getId();
                        if (!droppedItems.contains(id))
                            droppedItems.add(id);

                        if (item.interact("Drop")) return Random.nextInt(400, 600);
                    }
                } else {
                    final GroundItem net = context().groundItems.getNearest(STATUE_TILES[statueIndex], new Filter<GroundItem>() {
                        @Override
                        public boolean accept(final GroundItem groundItem) {
                            return "Small fishing net".equals(groundItem.getName());
                        }
                    });

                    if (net != null)
                        if (net.isOnGameScreen()) {
                            if (net.interact("Take")) return Random.nextInt(600, 800);
                        } else if (context().movement.walkTileOnMap(net)) {
                            return Random.nextInt(600, 800);
                        }
                }
            } else if (context().inventory.contains("Fishlike thing")) {
                final GameObject pot = context().objects.getNearest("Uncooking pot");
                if (pot != null)
                    if (pot.isOnGameScreen()) {
                        final Item fish = context().inventory.getItem("Fishlike thing");
                        if (fish != null && fish.useItemOn(pot)) return Random.nextInt(600, 800);
                    } else if (context().movement.walkTileOnMap(pot)) {
                        return Random.nextInt(600, 800);
                    }
            } else if (context().inventory.contains("Raw fishlike thing")) {
                if (bob.isOnGameScreen()) {
                    final Item fish = context().inventory.getItem("Raw fishlike thing");
                    if (fish != null && fish.useItemOn(bob)) {
                        Time.sleep(new Condition() {
                            @Override
                            public boolean validate() {
                                return context().inventory.contains("Raw fishlike thing");
                            }
                        }, 4000);

                        if (!context().inventory.contains("Raw fishlike thing"))
                            canLeave = true;
                    }
                } else if (context().movement.walkTileOnMap(bob)) {
                    return Random.nextInt(600, 800);
                }
            } else {
                if (context().inventory.isFull()) {
                    final Item item = context().inventory.getItemAt(2);
                    if (item != null) {
                        final int id = item.getId();
                        if (!droppedItems.contains(id))
                            droppedItems.add(id);

                        if (item.interact("Drop")) return Random.nextInt(400, 600);
                    }
                } else {
                    final GameObject spot = context().objects.getNearest(STATUE_TILES[statueIndex], new Filter<GameObject>() {
                        @Override
                        public boolean accept(final GameObject object) {
                            return "Fishing spot".equals(object.getName());
                        }
                    });

                    if (spot != null)
                        if (spot.isOnGameScreen()) {
                            if (spot.interact("Net")) {
                                final Player local = context().players.getLocal();
                                Time.sleep(new Condition() {
                                    @Override
                                    public boolean validate() {
                                        return local.getAnimation() == -1;
                                    }
                                }, 4000);

                                Time.sleep(new Condition() {
                                    @Override
                                    public boolean validate() {
                                        return local.getAnimation() != -1;
                                    }
                                }, 10000);
                            }
                        } else if (context().movement.walkTileOnMap(spot)) {
                            return Random.nextInt(600, 800);
                        }
                }
            }
        } else {
            if (servant.isOnGameScreen()) {
                if (servant.interact("Talk-to")) return Random.nextInt(600, 800);
            } else if (context().movement.walkTileOnMap(servant)) {
                return Random.nextInt(600, 800);
            }
        }
        
        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.SCAPE_RUNE;
    }

}
