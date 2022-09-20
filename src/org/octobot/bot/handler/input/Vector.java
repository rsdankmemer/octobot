package org.octobot.bot.handler.input;

/**
 * Vector
 *
 * @author Pat-ji
 */
public class Vector {
    public double xUnits;
    public double yUnits;

    public void add(final Vector vector) {
        xUnits += vector.xUnits;
        yUnits += vector.yUnits;
    }

    public Vector multiply(final double factor) {
        final Vector out = new Vector();
        out.xUnits = xUnits * factor;
        out.yUnits = yUnits * factor;
        return out;
    }

    public double getMagnitude() {
        return Math.sqrt(xUnits * xUnits + yUnits * yUnits);
    }

    public double getAngle() {
        return Math.atan2(yUnits, xUnits);
    }

}
