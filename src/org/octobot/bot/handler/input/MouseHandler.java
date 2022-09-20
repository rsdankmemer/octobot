package org.octobot.bot.handler.input;

import org.octobot.script.methods.Mouse;
import org.octobot.script.util.Random;

import java.awt.*;

/**
 * SplineExecutor
 *
 * @author Pat-ji
 */
public class MouseHandler {

    public boolean move(final Mouse mouse, final int xDest, final int yDest) {
        final int x = mouse.getX();
        final int y = mouse.getY();
        if ((xDest == -1 && yDest == -1) || (xDest == x && yDest == y)) return true;

        try {
            final Point[] path = applyDynamism(generateSpline(generateControls(x, y, xDest, yDest, Random.nextInt(25, 50), Random.nextInt(10, 30))),
                    (int) fittsLaw(Math.sqrt(Math.pow(xDest - x, 2) + Math.pow(yDest - y, 2)), 10), 12);
            for (final Point point : path) {
                if (!mouse.active) break;

                mouse.hop(point.x, point.y);
                Thread.sleep(mouse.getSpeed());
            }

            return mouse.getX() == xDest && mouse.getY() == yDest;
        } catch (final Exception ignored) { }

        return false;
    }

    private void adaptiveMidpoints(final java.util.Vector<Point> points) {
        int i = 0;
        while (i < points.size() - 1) {
            final Point next = points.get(i++);
            final Point current = points.get(i);
            if (Math.abs(next.x - current.x) > 1 || Math.abs(next.y - current.y) > 1)
                if (Math.abs(next.x - current.x) != 0) {
                    final double slope = (double) (next.y - current.y) / (double) (next.x - current.x);
                    final double intercept = next.y - slope * next.x;
                    for (int c = next.x < current.x ? next.x + 1 : current.x - 1; next.x < current.x ? c < current.x : c > next.x; c += next.x < current.x ? 1 : -1)
                        points.add(i++, new Point(c, (int) Math.round(intercept + slope * c)));
                } else {
                    for (int c = next.y < current.y ? next.y + 1 : current.y - 1; next.y < current.y ? c < current.y : c > next.y; c += next.y < current.y ? 1 : -1)
                        points.add(i++, new Point(next.x, c));
                }
        }
    }

    private Point[] applyDynamism(final Point[] spline, final int msForMove, final int msPerMove) {
        final int numPoints = spline.length;
        final int steps = (int) Math.floor(numPoints / (msPerMove / ((double) msForMove / (double) numPoints)));
        final Point[] result = new Point[steps];
        final double[] gaussValues = gaussTable(result.length);

        double currentPercent = 0;
        for (int i = 0; i < steps; i++) {
            currentPercent += gaussValues[i];
            final int nextIndex = (int) Math.floor(numPoints * currentPercent);
            if (nextIndex < numPoints) {
                result[i] = spline[nextIndex];
            } else {
                result[i] = spline[numPoints - 1];
            }
        }

        if (currentPercent < 1)
            result[steps - 1] = spline[numPoints - 1];

        return result;
    }

    private double nCk(final int n, final int k) {
        return factor(n) / (factor(k) * factor(n - k));
    }

    private double factor(final int value) {
        double result = 1;
        for (int i = 1; i <= value; i++)
            result *= i;

        return result;
    }

    private long fittsLaw(final double targetDist, final double targetSize) {
        return (long) (105 * Math.log10(targetDist / targetSize + 1) / Math.log10(2));
    }

    private double gaussian(double t) {
        t = 10 * t - 5;
        return 1 / (Math.sqrt(5) * Math.sqrt(2 * Math.PI)) * Math.exp(-t * t / 20);
    }

    private double[] gaussTable(final int steps) {
        final double step = 1 / steps;
        double sum = 0;
        for (int i = 0; i < steps; i++)
            sum += gaussian(i * step);

        final double[] table = new double[steps];
        for (int i = 0; i < steps; i++)
            table[i] = gaussian(i * step) / sum;

        return table;
    }

    private Point[] generateControls(final int sx, final int sy, final int ex, final int ey, int ctrlSpacing, int ctrlVariance) {
        final double dist = Math.sqrt((sx - ex) * (sx - ex) + (sy - ey) * (sy - ey));
        final double angle = Math.atan2(ey - sy, ex - sx);
        int ctrlPoints = (int) Math.floor(dist / ctrlSpacing);
        ctrlPoints = ctrlPoints * ctrlSpacing == dist ? ctrlPoints - 1 : ctrlPoints;
        if (ctrlPoints <= 1) {
            ctrlPoints = 2;
            ctrlSpacing = (int) dist / 3;
            ctrlVariance = Random.nextInt(0, 3);
        }

        final Point[] result = new Point[ctrlPoints + 2];
        result[0] = new Point(sx, sy);
        for (int i = 1; i < ctrlPoints + 1; i++) {
            final double radius = ctrlSpacing * i;
            final Point cur = new Point((int) (sx + radius * Math.cos(angle)), (int) (sy + radius * Math.sin(angle)));
            double percent = 1 - (double) (i - 1) / (double) ctrlPoints;
            percent = percent > 0.5 ? percent - 0.5 : percent;
            percent += 0.25;
            final int curVariance = (int) (ctrlVariance * percent);

            cur.x = cur.x + curVariance * 2 - curVariance;
            cur.y = cur.y + curVariance * 2 - curVariance;
            result[i] = cur;
        }

        result[ctrlPoints + 1] = new Point(ex, ey);
        return result;
    }

    private Point[] generateSpline(final Point[] controls) {
        final double degree = controls.length - 1;
        final java.util.Vector<Point> spline = new java.util.Vector<Point>();
        boolean lastFlag = false;

        for (double theta = 0; theta <= 1; theta += 0.01) {
            double x = 0, y = 0;
            for (double index = 0; index <= degree; index++) {
                final double probPoly = nCk((int) degree, (int) index) * Math.pow(theta, index) * Math.pow(1.0 - theta, degree - index);
                x += probPoly * controls[(int) index].x;
                y += probPoly * controls[(int) index].y;
            }

            final Point temp = new Point((int) x, (int) y);
            try {
                if (!temp.equals(spline.lastElement()))
                    spline.add(temp);
            } catch (final Exception e) {
                spline.add(temp);
            }

            lastFlag = theta != 1.0;
        }

        if (lastFlag)
            spline.add(new Point(controls[(int) degree].x, controls[(int) degree].y));

        adaptiveMidpoints(spline);
        return spline.toArray(new Point[spline.size()]);
    }

}
