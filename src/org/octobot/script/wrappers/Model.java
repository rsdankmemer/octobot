package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSModel;
import org.octobot.script.Condition;
import org.octobot.script.ScriptContext;
import org.octobot.script.ViewPoint;
import org.octobot.script.methods.Calculations;
import org.octobot.script.util.Random;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model
 *
 * @author Pat-ji
 */
public class Model extends Wrapper<RSModel> implements RSModel, ViewPoint {
    protected final int[] xTriangles;
    protected final int[] yTriangles;
    protected final int[] zTriangles;
    protected final int[] xVertices;
    protected final int[] yVertices;
    protected final int[] zVertices;

    private ScriptContext context;

    private int heightOffset;
    public int x, y;

    public Model(final RSModel accessor) {
        super(accessor);

        xTriangles = accessor.getXTriangles().clone();
        yTriangles = accessor.getYTriangles().clone();
        zTriangles = accessor.getZTriangles().clone();
        xVertices = accessor.getXVertices().clone();
        yVertices = accessor.getYVertices().clone();
        zVertices = accessor.getZVertices().clone();
    }

    public Model getModelInstance() {
        return this;
    }

    void setX(final int x) {
        this.x = x;
    }

    void setY(final int y) {
        this.y = y;
    }

    void setContext(final ScriptContext context) {
        this.context = context;
    }

    /**
     * This method is used to get the {@link Model}s x vertices
     *
     * @return the {@link Model}s x vertices
     */
    @Override
    public int[] getXVertices() {
        return xVertices;
    }

    /**
     * This method is used to get the {@link Model}s y vertices
     *
     * @return the {@link Model}s y vertices
     */
    @Override
    public int[] getYVertices() {
        return yVertices;
    }

    /**
     * This method is used to get the {@link Model}s z vertices
     *
     * @return the {@link Model}s z vertices
     */
    @Override
    public int[] getZVertices() {
        return zVertices;
    }

    /**
     * This method is used to get the {@link Model}s x triangles
     *
     * @return the {@link Model}s x triangles
     */
    @Override
    public int[] getXTriangles() {
        return xTriangles;
    }

    /**
     * This method is used to get the {@link Model}s y triangles
     *
     * @return the {@link Model}s y triangles
     */
    @Override
    public int[] getYTriangles() {
        return yTriangles;
    }

    /**
     * This method is used to get the {@link Model}s z triangles
     *
     * @return the {@link Model}s z triangles
     */
    @Override
    public int[] getZTriangles() {
        return zTriangles;
    }

    /**
     * This method is used to get the {@link Model}s vertices count
     *
     * @return the {@link Model}s vertices count
     */
    @Override
    public int getVerticesCount() {
        return getAccessor() != null ? getAccessor().getVerticesCount() : -1;
    }

    /**
     * This method is used to get the {@link Model}s triangles count
     *
     * @return the {@link Model}s triangles count
     */
    @Override
    public int getTriangleCount() {
        return getAccessor() != null ? getAccessor().getTriangleCount() : -1;
    }

    /**
     * This method is used to get the {@link Model}s height
     *
     * @return the {@link Model}s height
     */
    @Override
    public int getHeight() {
        return getAccessor() != null ? getAccessor().getHeight() : -1;
    }

    /**
     * This method is used to get the {@link Model}s height offset
     *
     * @return the {@link Model}s height offset
     */
    public int getHeightOffset() {
        return heightOffset;
    }

    /**
     * This method is used to set the {@link Model}s height offset
     *
     * @param heightOffset the amount you want to set the height offset to
     */
    public void setHeightOffset(final int heightOffset) {
        this.heightOffset = heightOffset;
    }

    /**
     * This method is used to check if the {@link Model} contains a certain point
     *
     * @param point the point to check
     * @return {@code true} if the {@link Model} contains given point
     */
    @Override
    public boolean contains(final Point point) {
        return contains(point.x, point.y);
    }

    /**
     * This method is used to check if the {@link Model} contains a certain set of coordinates
     *
     * @param x the x coordinate of the point you wish to check.
     * @param y the y coordinate of the point you wish to check
     * @return {@code true} if the {@link Model} contains given coordinates
     */
    @Override
    public boolean contains(final int x, final int y) {
        final Polygon[] triangles = getTriangles();
        if (triangles == null || triangles.length <= 0) return false;

        for (final Polygon triangle : triangles)
            if (triangle.contains(x, y)) return true;

        return false;
    }

    /**
     * This method is used to check of the {@link Model} is on the screen, this  includes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link Model} is on the screen
     */
    @Override
    public boolean isOnScreen() {
        return isOnGameScreen();
    }

    /**
     * This method is used to check of the {@link Model} is on the game screen, this  excludes the chat box, inventory and mini map
     *
     * @return <code>true</code> if the {@link Model} is on the game screen
     */
    @Override
    public boolean isOnGameScreen() {
        final Point[] points = getPoints();
        return points != null && points.length > 0;
    }

    /**
     * This method is used to get the central {@link java.awt.Point} of the {@link Model}
     *
     * @return the central {@link java.awt.Point} of the {@link Model}
     */
    @Override
    public Point getCentralPoint() {
        try {
            int x = 0, y = 0, total = 0;
            final Polygon[] triangles = getTriangles();
            if (triangles == null) return null;

            for (final Polygon polygon : triangles)
                for (int i = 0; i < polygon.npoints; i++) {
                    x += polygon.xpoints[i];
                    y += polygon.ypoints[i];

                    total++;
                }

            final Point central = new Point(x / total, y / total);

            Point center = null;
            double dist = 1000.0D;
            for (final Polygon polygon : triangles)
                for (int i = 0; i < polygon.npoints; i++) {
                    final Point result = new Point(polygon.xpoints[i], polygon.ypoints[i]);
                    if (!context.calculations.isOnGameScreen(result)) continue;

                    final double dist2 = context.calculations.distance(central, result);
                    if (center == null || dist2 < dist) {
                        center = result;
                        dist = dist2;
                    }
                }

            return center;
        } catch (final Exception ignored) {
        ignored.printStackTrace(); }

        return null;
    }

    /**
     * This method is used to get a random {@link java.awt.Point} on the {@link Model}
     *
     * @return a random {@link java.awt.Point} on the {@link Model}
     */
    @Override
    public Point getRandomPoint() {
        final Polygon[] triangles = getTriangles();
        if (triangles != null && triangles.length > 0) {
            int i = 0;
            final int length = Math.min(50, triangles.length);
            do {
                final Polygon polygon = triangles[Random.nextInt(0, triangles.length)];
                final Point point = new Point((int) (0.34F * (polygon.xpoints[0] + polygon.xpoints[1] + polygon.xpoints[2])),
                        (int) (0.34F * (polygon.ypoints[0] + polygon.ypoints[1] + polygon.ypoints[2])));

                if (context.calculations.isOnGameScreen(point)) return point;
            } while (i++ < length);
        }

        return new Point(-1, -1);
    }

    /**
     * This method is used to rotate the model
     *
     * @param orientation the orientation you want to give the model
     */
    public void rotate(final int orientation) {
        if (orientation <= 0 || orientation > 2048) return;

        final int sin = Calculations.SIN_TABLE[orientation];
        final int cos = Calculations.COS_TABLE[orientation];
        final int[] x = getXVertices();
        final int[] z = getZVertices();
        if (x != null && z != null)
            for (int i = 0; i < x.length; i++) {
                final int xNew = z[i] * sin + x[i] * cos >> 16;
                getZVertices()[i] = z[i] * cos - x[i] * sin >> 16;
                getXVertices()[i] = xNew;
            }
    }

    /**
     * This method is used to get all the {@link java.awt.Point}s of a {@link Model}
     *
     * @return all the {@link Model}s {@link java.awt.Point}s
     */
    public Point[] getPoints() {
        final Polygon[] triangles = getTriangles();
        if (triangles != null && triangles.length > 0) {
            final List<Point> result = new ArrayList<Point>();
            for (final Polygon polygon : triangles) {
                final Point point  = new Point((int) (0.34F * (polygon.xpoints[0] + polygon.xpoints[1] + polygon.xpoints[2])),
                        (int) (0.34F * (polygon.ypoints[0] + polygon.ypoints[1] + polygon.ypoints[2])));

                if (context.calculations.isOnGameScreen(point))
                    result.add(point);
            }

            return result.toArray(new Point[triangles.length]);
        }

        return null;
    }

     /**
     * This method is used to get all the triangles of a {@link Model}
     *
     * @return all the {@link Model}s triangles
     */
    public Polygon[] getTriangles() {
        final List<Polygon> polygons = new ArrayList<Polygon>();

        final int height = context.calculations.tileHeight(x, y, context.game.getPlane()) + heightOffset;
        if (xTriangles == null || yTriangles == null || zTriangles == null) {
            return null;
        } else if (xTriangles.length != yTriangles.length || xTriangles.length != zTriangles.length) {
            return null;
        }

        for (int i = 0; i < xTriangles.length; i++) {
            final Point p1 = context.calculations.worldToScreen(x + xVertices[xTriangles[i]], y + zVertices[xTriangles[i]], height + yVertices[xTriangles[i]]);
            if (p1 == null || !context.calculations.isOnGameScreen(p1)) continue;

            final Point p2 = context.calculations.worldToScreen(x + xVertices[yTriangles[i]], y + zVertices[yTriangles[i]], height + yVertices[yTriangles[i]]);
            if (p2 == null || !context.calculations.isOnGameScreen(p2)) continue;

            final Point p3 = context.calculations.worldToScreen(x + xVertices[zTriangles[i]], y + zVertices[zTriangles[i]], height + yVertices[zTriangles[i]]);
            if (p3 == null || !context.calculations.isOnGameScreen(p3)) continue;

            polygons.add(new Polygon(new int[] { p1.x, p2.x, p3.x }, new int[] { p1.y, p2.y, p3.y }, 3));
        }

        return polygons.toArray(new Polygon[polygons.size()]);
    }

    /**
     * This method is used to hover the mouse over the {@link Model}
     *
     * @return <code>true</code> if the {@link Model} has been successfully hovered
     */
    @Override
    public boolean hover() {
        return context.mouse.hover(this);
    }

    /**
     * This method is used to click the {@link Model}
     *
     * @param left true if the click should be a left, false if it should be a right click
     * @return <code>true</code> if the {@link Model} has been successfully clicked
     */
    @Override
    public boolean click(final boolean left) {
        final Condition condition = new Condition() {
            @Override
            public boolean validate() {
                return contains(context.mouse.getLocation());
            }
        };

        if (!condition.validate())
            hover();

        if (condition.validate()) {
            return context.mouse.click(left) && context.mouse.getCurrentCrosshair() == 2;
        } else {
            return hover() && context.mouse.click(left) && context.mouse.getCurrentCrosshair() == 2;
        }
    }

    /**
     * This method is used to interact with the {@link Model}
     *
     * @param action what action the client should use to interact with the {@link Model} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link Model}
     */
    @Override
    public boolean interact(final String action) {
        return interact(action, "");
    }

    /**
     * This method is used to interact with the {@link Model}
     *
     * @param action what action the client should use to interact with the {@link Model} as a {@link String}
     * @param option what option the client should use when interacting with the {@link Model} as a {@link String}
     * @return <code>true</code> if the client has successfully interacted with the {@link Model}
     */
    @Override
    public boolean interact(final String action, final String option) {
        final Condition condition = new Condition() {
            @Override
            public boolean validate() {
                return contains(context.mouse.getLocation()) && context.menu.contains(action, option);
            }
        };

        if (!condition.validate())
            context.mouse.move(this.getRandomPoint()); //hover();

        if (condition.validate()) {
            // TODO FIX
            if (context.menu.interact(action, option)) {
                return true;
            } else {
                hover();
                return false;
            }
        } else {
            return hover() && context.menu.interact(action, option);
        }
    }

    /**
     * This method is used to check if the {@link Model} is equal to this
     *
     * @param obj the object you want to compare to the {@link Model}
     * @return true if the {@link Model} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Model) {
            final Model model = (Model) obj;
            return model.getAccessor() == getAccessor();
        } else if (obj instanceof RSModel) {
            return obj == getAccessor();
        }

        return false;
    }

}
