package org.octobot.script;

import java.awt.*;

/**
 * ViewPoint
 */
public interface ViewPoint extends Interactable {

    /**
     * This method gets a random {@link java.awt.Point} of something on the screen
     *
     * @return a random {@link java.awt.Point} of something on the screen
     */
    public Point getRandomPoint();

    /**
     * This is used to check if the {@link ViewPoint} contains a certain {@link java.awt.Point}
     *
     * @param point the point to check
     * @return <code>true</code> if the {@link ViewPoint} contains given {@link java.awt.Point}
     */
    public boolean contains(final Point point);

    /**
     * This is used to check if the {@link ViewPoint} contains a certain {@link java.awt.Point} with given x and y
     *
     * @param x the x coordinate of the point you wish to check.
     * @param y the y coordinate of the point you wish to check
     * @return <code>true</code> if the {@link ViewPoint} contains the given point
     */
    public boolean contains(final int x, final int y);

}
