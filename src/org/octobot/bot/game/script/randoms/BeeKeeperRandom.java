package org.octobot.bot.game.script.randoms;

import org.octobot.bot.game.script.ScriptManifest;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;
import org.octobot.script.wrappers.NPC;
import org.octobot.script.wrappers.Widget;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BeeKeeperRandom
 *
 * @author Pat-ji
 */
@ScriptManifest(
        name = "BeeKeeper",
        description = "Handles the BeeKeeper random.",
        version = 1.0,
        authors = "Pat-ji"
)
public class BeeKeeperRandom extends RandomScript {
    private NPC keeper;
    private Widget widget;

    @Override
    public boolean validate() {
        return context().game.isLoggedIn() && (keeper = context().npcs.find().name("Bee keeper").interacting(context().players.getLocal()).first()) != null || (widget = context().widgets.get(420)) != null;
    }

    private Rectangle getNonOverlappedRectangle(final Rectangle rectangle, final List<Rectangle> list) {
        if (list.isEmpty()) return rectangle;

        final List<Point> points = new ArrayList<Point>();
        for (int i = rectangle.x; i < rectangle.x + rectangle.getWidth(); i++)
            for (int j = rectangle.y; j < rectangle.y + rectangle.getHeight(); j++) {
                boolean add = true;
                for (final Rectangle test : list)
                    if (test.contains(i, j)) {
                        add = false;
                        break;
                    }

                if (add)
                    points.add(new Point(i, j));
            }

        return points.isEmpty() ? rectangle : new Rectangle(points.get(Random.nextInt(0, points.size())), new Dimension(3, 3));
    }

    private List<Rectangle> getOverlappingRectangles(final Rectangle rectangle) {
        final List<Rectangle> results = new ArrayList<Rectangle>();
        for (final Part check : Part.values) {
            final Component checkComponent = getPartComponent(check.modelId);
            if (checkComponent == null) continue;

            final Rectangle checkRect;
            if ((checkRect = checkComponent.getBounds()) != null && !checkRect.equals(rectangle) && rectangle.intersects(checkRect))
                results.add(checkRect);
        }

        return results;
    }

    private Component getPartComponent(final int modelId) {
        int idx = 0;
        final Component[] children;
        if ((children = widget.getChildren()) == null) return null;
        for (final Component component : children) {
            if (component != null && component.getModelId() == modelId)  return component;

            if (++idx > 20) break;
        }

        return null;
    }

    private void updateLocations() {
        for (final Part part : Part.values) {
            final Component component = getPartComponent(part.modelId);
            part.xx = component.getX();
            part.yy = component.getY();
        }
    }

    private boolean allInPlace() {
        for (final Part part : Part.values)
            if (!part.isOnPosition(getPartComponent(part.modelId))) return false;

        return true;
    }

    @Override
    public int execute() {
        if (context().widgets.getContinue() != null) {
            if (context().widgets.clickContinue()) return Random.nextInt(600, 800);

            return 100;
        }

        final Component talk = context().widgets.getComponent(228, 1);
        if (talk != null) {
            talk.interact("Continue");
            return Random.nextInt(600, 800);
        }

        Component control = widget.getChild(3);
        if (control != null) {
            if (allInPlace()) {
                if (control.click(true)) return Random.nextInt(600, 800);
            } else {
                Component current = null;
                parts: for (final Part part : Part.values) {
                    updateLocations();
                    Component component = getPartComponent(part.modelId);
                    while (component.isVisible() && !part.isOnPosition(component)) {
                        if (current == null || component.getModelId() != current.getModelId()) {

                            final Rectangle rectangle = component.getBounds();
                            final Rectangle free = getNonOverlappedRectangle(rectangle, getOverlappingRectangles(rectangle));
                            if (free != null && context().mouse.move(free) && context().mouse.click(true))
                                current = component;
                        }

                        final List<Integer> options = new ArrayList<Integer>();
                        if (component.getX() < part.x) {
                            options.add(5);
                        } else if (component.getX() > part.x) {
                            options.add(4);
                        }

                        if (component.getY() < part.y) {
                            options.add(2);
                        } else if (component.getY() > part.y) {
                            options.add(1);
                        }

                        if (options.isEmpty() || allInPlace()) break parts;

                        control = widget.getChild(options.get(0));
                        if (control != null && control.click(true)) {
                            Time.sleep(800, 1000);

                            for (final Part check : Part.values) {
                                final Component checkComponent = getPartComponent(check.modelId);
                                if (check.hasMoved(checkComponent)) {
                                    component = checkComponent;
                                    updateLocations();
                                    if (allInPlace()) break parts;

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else if (keeper != null && keeper.interact("Talk-To")) {
            return Random.nextInt(700, 900);
        }

        return 100;
    }

    @Override
    public RandomEvent.Event getEvent() {
        return null;//RandomEvent.Event.BEE_KEEPER;
    }

    @Override
    public boolean mayBreak() {
        return false;
    }

    /**
     * Part
     *
     * @author Pat-ji
     */
    private enum Part {
        TOP(16036, 199, 4),
        MID_UP(16025, 199, 64),
        MID_DOWN(16022, 199, 94),
        DOWN(16034, 199, 154);

        public static final Part[] values = values();

        public final int modelId, x, y;

        public int xx, yy;

        private Part(final int modelId, final int x, final int y) {
            this.modelId = modelId;
            this.x = x;
            this.y = y;
        }

        public boolean isOnPosition(final Component component) {
            return component.getX() == x && component.getY() == y;
        }

        public boolean hasMoved(final Component component) {
            return component.getX() != xx || component.getY() != yy;
        }

    }

}
