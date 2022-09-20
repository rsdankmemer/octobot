package org.octobot.script.wrappers;

import org.octobot.bot.game.client.RSModel;
import org.octobot.bot.game.client.RSNode;
import org.octobot.bot.game.client.RSNodeList;
import org.octobot.bot.game.client.RSProjectile;
import org.octobot.script.ScriptContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Projectile
 *
 * @author Pat-ji
 */
public class Projectile extends Animable<RSProjectile> implements RSProjectile {

    public Projectile(final ScriptContext context, final RSProjectile accessor) {
        super(context, accessor);
    }

    /**
     * This method is used to check if a {@link Projectile} is moving
     *
     * @return <code>true</code> if the {@link Projectile} is moving
     */
    @Override
    public boolean isMoving() {
        return getAccessor() != null && getAccessor().isMoving();
    }

    /**
     * This method is used to get the {@link Projectile}s target Id
     *
     * @return the id of the {@link Projectile}s target
     */
    @Override
    public int getTargetId() {
        return getAccessor() != null ? getAccessor().getTargetId() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s x coordinate
     *
     * @return the x coordinate of the {@link Projectile}s location
     */
    @Override
    public double getX() {
        return getAccessor() != null ? getAccessor().getX() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s y coordinate
     *
     * @return the y coordinate of the {@link Projectile}s location
     */
    @Override
    public double getY() {
        return getAccessor() != null ? getAccessor().getY() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s height
     *
     * @return the height of the {@link Projectile}s location
     */
    @Override
    public double getCurrentHeight() {
        return getAccessor() != null ? getAccessor().getCurrentHeight() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s speed vector x
     *
     * @return the {@link Projectile}s speed vector x
     */
    @Override
    public double getSpeedVectorX() {
        return getAccessor() != null ? getAccessor().getSpeedVectorX() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s speed vector y
     *
     * @return the {@link Projectile}s speed vector y
     */
    @Override
    public double getSpeedVectorY() {
        return getAccessor() != null ? getAccessor().getSpeedVectorY() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s speed vector z
     *
     * @return the {@link Projectile}s speed vector z
     */
    @Override
    public double getSpeedVectorZ() {
        return getAccessor() != null ? getAccessor().getSpeedVectorZ() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s speed vector scalar
     *
     * @return the {@link Projectile}s speed vector scalar
     */
    @Override
    public double getSpeedVectorScalar() {
        return getAccessor() != null ? getAccessor().getSpeedVectorScalar() : -1;
    }
    /**
     * This method is used to get the {@link Projectile}s height offset
     *
     * @return the {@link Projectile}s height offset
     */
    @Override
    public double getHeightOffset() {
        return getAccessor() != null ? getAccessor().getHeightOffset() : -1;
    }

    /**
     * This method is used to get the {@link Projectile}s animated model
     *
     * @return the {@link Projectile}s animated model
     */
    @Override
    public RSModel getAnimatedModel() {
        return getAccessor() != null ? getAccessor().getAnimatedModel() : null;
    }

    /**
     * This method is used to get the {@link Projectile}s height
     *
     * @return the {@link Projectile}s height
     */
    @Override
    public int getHeight() {
        return getAccessor() != null ? getAccessor().getHeight() : -1;
    }
    /**
     * This method is used to get the {@link Projectile}s rotation in the x direction
     *
     * @return the {@link Projectile}s rotation in the x direction
     */
    public int getModelRotationX() {
        return (int) (Math.atan2(getSpeedVectorZ(), getSpeedVectorScalar()) * 325.949) & 0x7ff;
    }

    /**
     * This method is used to get the {@link Projectile}s rotation in the y direction
     *
     * @return the {@link Projectile}s rotation in the y direction
     */
    public int getModelRotationY() {
        return (int) (Math.atan2(getSpeedVectorX(), getSpeedVectorY()) * 325.949) + 1024 & 0x7ff;
    }

    /**
     * This method is used to get the {@link Projectile}s current location
     *
     * @return the {@link Projectile}s current location
     */
    @Override
    public Tile getLocation() {
        return new Tile(context.game.getBaseX() + ((int) getX() >> 7), context.game.getBaseY() + ((int) getY() >> 7), context.game.getPlane());
    }
    /**
     * This method is used to get the {@link Projectile}s model
     *
     * @return the {@link Projectile}s model
     */

    @Override
    public Model getModel() {
        if (getModelInstance() != null) {
            final Model model = new Model(getModelInstance().getAccessor()) {
                @Override
                public int getHeight() {
                    return (int) getCurrentHeight();
                }

                /**
                 * This method is used to get the {@link Projectile}s triangles
                 *
                 * @return the {@link Projectile}s triangles
                 */
                @Override
                public Polygon[] getTriangles() {
                    final List<Polygon> polygons = new ArrayList<Polygon>();

                    final int height = getHeight();
                    if (xTriangles == null || yTriangles == null || zTriangles == null) {
                        return null;
                    } else if (xTriangles.length != yTriangles.length || xTriangles.length != zTriangles.length) {
                        return null;
                    }

                    for (int i = 0; i < xTriangles.length; i++) {
                        final Point p1 = context.calculations.worldToScreen(x + getXVertices()[xTriangles[i]], y + getZVertices()[xTriangles[i]], height + getYVertices()[xTriangles[i]]);
                        if (p1 == null || !context.calculations.isOnGameScreen(p1.x, p1.y)) continue;

                        final Point p2 = context.calculations.worldToScreen(x + getXVertices()[yTriangles[i]], y + getZVertices()[yTriangles[i]], height + getYVertices()[yTriangles[i]]);
                        if (p2 == null || !context.calculations.isOnGameScreen(p2.x, p2.y)) continue;

                        final Point p3 = context.calculations.worldToScreen(x + getXVertices()[zTriangles[i]], y + getZVertices()[zTriangles[i]], height + getYVertices()[zTriangles[i]]);
                        if (p3 == null || !context.calculations.isOnGameScreen(p3.x, p3.y)) continue;

                        polygons.add(new Polygon(new int[]{p1.x, p2.x, p3.x}, new int[]{p1.y, p2.y, p3.y}, 3));
                    }

                    return polygons.toArray(new Polygon[polygons.size()]);
                }
            };
            model.setContext(context);
            model.setX((int) getX());
            model.setY((int) getY());
            return model;
        }

        return null;
    }

    /**
     * This method is to check if the {@link Projectile} is valid
     *
     * @return <code>true</code> if the {@link Projectile} is valid
     */
    @Override
    public boolean isValid() {
        final RSNodeList list = context.client.getProjectileList();
        RSNode head;
        if ((head = list.getHead()) != null) {
            RSNode next = null;
            while ((next = (next != null ? next.getNext() : head.getNext())) != null && next instanceof RSProjectile) {
                if (equals(next)) return true;
            }
        }

        return false;
    }

    /**
     * This method is used to render the {@link org.octobot.script.wrappers.Projectile}
     *
     * @param graphics the {@link java.awt.Graphics} to render with
     */
    public void render(final Graphics graphics) {
        final Model model = getModel();
        if (model != null) {
            final Point[] points = model.getPoints();
            if (points != null && points.length > 0)
                for (final Point point : points)
                    if (point != null)
                        graphics.fillRect(point.x, point.y, 2, 2);
        }
    }

    /**
     * This method is used to check if the {@link Projectile} is equal to this
     *
     * @param obj the object you want to compare to the {@link Projectile}
     * @return true if the {@link Projectile} and the object are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;

        if (obj instanceof Projectile) {
            final Projectile projectile = (Projectile) obj;
            return projectile.getAccessor() == getAccessor();
        } else if (obj instanceof RSProjectile) {
            return obj == getAccessor();
        }

        return false;
    }

}
