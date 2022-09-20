package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.methods.Movement;

import java.awt.*;

/**
 * CollisionRenderEvent
 *
 * @author Pat-ji
 */
public class CollisionRenderEvent extends RenderEvent {
    private final Point[][][] points;

    public CollisionRenderEvent(final ScriptContext context) {
        super(context);

        points = new Point[2][105][105]; // [0] = minimap, [1] = screen
    }

    @Override
    public void render(final Graphics g) {
        if (context().game.isLoggedIn()) {
            final int baseX = context().game.getBaseX();
            final int baseY = context().game.getBaseY();
            final int plane = context().game.getPlane();
            final int flags[][] = context().movement.getCollisionFlags(plane);
            for (int i = 4; i < 99; i++) {
                for (int j = 4; j < 99; j++) {
                    final int x = i + baseX;
                    final int y = j + baseY;
                    Point point = context().calculations.worldToMiniMap(x - 0.5, y - 0.5);
                    if (point == null || point.x == -1 || point.y == -1)
                        point = null;

                    points[0][i][j] = point;

                    point = context().calculations.groundToScreen((x - baseX) * 128, (y - baseY) * 128, plane, 0);
                    if (point == null || point.x == -1 || point.y == -1 || !context().calculations.isOnGameScreen(point))
                        point = null;

                    points[1][i][j] = point;
                }
            }

            for (int i = 4; i < 99; i++) {
                for (int j = 4; j < 99; j++) {
                    final int flag = flags[i][j];
                    Point point = points[0][i][j];
                    if (point == null) continue;

                    Point back = points[0][i][j + 1];
                    Point left = points[0][i + 1][j];
                    Point leftback = points[0][i + 1][j + 1];
                    if ((flag & Movement.Flags.WATER) != 0) {
                        g.setColor(Color.BLUE);
                        if (left != null && back != null && leftback != null)
                            g.fillPolygon(new int[]{point.x, back.x, leftback.x, left.x}, new int[]{point.y, back.y, leftback.y, left.y}, 4);
                    }

                    if ((flag & Movement.Flags.BLOCKED) != 0) {
                        g.setColor(Color.RED);
                        if (left != null && back != null && leftback != null)
                            g.fillPolygon(new int[]{point.x, back.x, leftback.x, left.x}, new int[]{point.y, back.y, leftback.y, left.y}, 4);
                    }

                    if ((flags[i][j - 1] & Movement.Flags.NORTH_WALL) != 0 || (flag & Movement.Flags.SOUTH_WALL) != 0) {
                        g.setColor(Color.BLACK);
                        if (left != null)
                            g.drawLine(point.x, point.y, left.x, left.y);
                    }

                    if ((flags[i - 1][j] & Movement.Flags.EAST_WALL) != 0 || (flag & Movement.Flags.WEST_WALL) != 0) {
                        g.setColor(Color.BLACK);
                        if (back != null)
                            g.drawLine(point.x, point.y, back.x, back.y);
                    }

                    point = points[1][i][j];
                    if (point == null) continue;

                    back = points[1][i][j + 1];
                    left = points[1][i + 1][j];
                    leftback = points[1][i + 1][j + 1];
                    if ((flag & Movement.Flags.WATER) != 0) {
                        g.setColor(Color.BLUE);
                        if (left != null && back != null && leftback != null)
                            g.fillPolygon(new int[]{point.x, back.x, leftback.x, left.x}, new int[]{point.y, back.y, leftback.y, left.y}, 4);
                    }

                    if ((flag & Movement.Flags.BLOCKED) != 0) {
                        g.setColor(Color.RED);
                        if (left != null && back != null && leftback != null)
                            g.fillPolygon(new int[]{point.x, back.x, leftback.x, left.x}, new int[]{point.y, back.y, leftback.y, left.y}, 4);
                    }

                    if ((flags[i][j - 1] & Movement.Flags.NORTH_WALL) != 0 || (flag & Movement.Flags.SOUTH_WALL) != 0) {
                        g.setColor(Color.BLACK);
                        if (left != null)
                            g.drawLine(point.x, point.y, left.x, left.y);
                    }

                    if ((flags[i - 1][j] & Movement.Flags.EAST_WALL) != 0 || (flag & Movement.Flags.WEST_WALL) != 0) {
                        g.setColor(Color.BLACK);
                        if (back != null)
                            g.drawLine(point.x, point.y, back.x, back.y);
                    }
                }
            }
        }
    }

}
