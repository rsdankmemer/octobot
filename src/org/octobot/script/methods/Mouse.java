package org.octobot.script.methods;

import org.octobot.bot.handler.input.MouseForceGenerator;
import org.octobot.bot.handler.input.MouseHandler;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Model;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Mouse
 *
 * @author Pat-ji
 */
public class Mouse extends Input {
    public static final int FAST = 5;
    public static final int NORMAL = 10;
    public static final int SLOW = 15;
    public static final int VERY_SLOW = 20;

    private final org.octobot.bot.game.loader.internal.wrappers.input.Mouse mouse;
    private final MouseHandler executor;
    public volatile boolean active;

    private int speed = 6;

    public Mouse(final ScriptContext context, final Applet applet, final org.octobot.bot.game.loader.internal.wrappers.input.Mouse mouse) {
        super(context, applet);

        this.mouse = mouse;
        executor = new MouseHandler();
    }

    /**
     * This method is used to get the {@link Mouse} speed
     *
     * @return the {@link Mouse} speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * This method is used to set the {@link Mouse} speed
     *
     * @param speed the speed to set
     */
    public void setSpeed(final int speed) {
        this.speed = speed;
    }

    public org.octobot.bot.game.loader.internal.wrappers.input.Mouse getMouse() {
        return mouse;
    }

    /**
     * This method is used to send a {@link java.awt.event.MouseEvent}
     *
     * @param event the {@link java.awt.event.MouseEvent} to send
     */
    public void sendEvent(final MouseEvent event) {
        mouse.sendEvent(event);
    }

    /**
     * This method is used to get the time of the last click
     *
     * @return the time of the last click
     */
    public long getClickTime() {
        return mouse.getClickTime();
    }

    /**
     * This method is used to get the current x position of the {@link Mouse}
     *
     * @return the current x position of the {@link Mouse}
     */
    public int getX() {
        return mouse.getLocation().x;
    }

    /**
     * This method is used to get the current y position of the {@link Mouse}
     *
     * @return the current y position of the {@link Mouse}
     */
    public int getY() {
        return mouse.getLocation().y;
    }

    /**
     * This method is used to get the {@link Mouse} location
     *
     * @return the {@link Mouse} location
     */
    public Point getLocation() {
        return new Point(getX(), getY());
    }

    /**
     * This method is used to get the current crosshair of the {@link Mouse}
     *
     * @return the current crosshair of the {@link Mouse}
     */
    public int getCurrentCrosshair() {
        return context().client.getCurrentCrosshair();
    }

    /**
     * This method is used to hop the {@link Mouse} to a given position
     *
     * @param x the x position to hop the {@link Mouse} to
     * @param y the y position to hop the {@link Mouse} to
     * @return <code>true</code> if the {@link Mouse} has been successfully hopped
     */
    public boolean hop(final int x, final int y) {
        sendEvent(new MouseEvent(getApplet(), MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false));
        return getX() == x && getY() == y;
    }

    /**
     * This method is used to send a click event
     *
     * @param left <code>true</code> if the click event should be a left click
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean click(final boolean left) {
        return click(getX(), getY(), left);
    }

    /**
     * This method is used to send a click event
     *
     * @param point the {@link java.awt.Point} to click at
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean click(final Point point) {
        return click(point.x, point.y, true);
    }

    /**
     * This method is used to send a click event
     *
     * @param x the x position to click at
     * @param y the y position to click at
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean click(final int x, final int y) {
        return click(x, y, true);
    }

    /**
     * This method is used to send a click event
     *
     * @param point the {@link java.awt.Point} to click at
     * @param left <code>true</code> if the click event should be a left click
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean click(final Point point, final boolean left) {
        return click(point.x, point.y, left);
    }

    /**
     * This method is used to send a click event
     *
     * @param x the x position to click at
     * @param y the y position to click at
     * @param left <code>true</code> if the click event should be a left click
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean click(final int x, final int y, final boolean left) {
        if (move(x, y)) {
            Time.sleep(50, 80);
            if (press(x, y, left)) {
                Time.sleep(100, 200);
                return release(left);
            }
        }

        return false;
    }

    /**
     * This method is used to send a press event
     *
     * @param x the x position to press at
     * @param y the y position to press at
     * @param left  <code>true</code> if the press event should be a left press
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean press(final int x, final int y, final boolean left) {
        sendEvent(new MouseEvent(getApplet(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x, y, 1, false, left ? 1 : 3));
        return true;
    }

    /**
     * This method is used to send a release event
     *
     * @param left <code>true</code> if the release event should be from the left button
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean release(final boolean left) {
        return release(getX(), getY(), left);
    }

    /**
     * This method is used to send a release event
     *
     * @param x the x position to release at
     * @param y the y position to release at
     * @param left <code>true</code> if the release event should be from the left button
     * @return <code>true</code> if the event has successfully been send
     */
    public boolean release(final int x, final int y, final boolean left) {
        sendEvent(new MouseEvent(getApplet(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y, 1, false, left ? 1 : 3));
        return true;
    }

    /**
     * This method is used to hover a {@link Model}
     *
     * @param model the {@link Model} to hover
     * @return <code>true</code> if the {@link Model} has successfully been hovered
     */
    public boolean hover(final Model model) {
        final MouseForceGenerator generator = new MouseForceGenerator();
        active = true;
        while (generator.active && active)
            if (!generator.hover(this, model)) break;

        active = false;
        return generator.completed;
    }

    /**
     * This method is used to interrupt the current {@link Mouse} movement and should be called from another {@link Thread} to work properly
     */
    public void interrupt() {
        active = false;
    }

    /***
     * This method is used to move the {@link Mouse}
     *
     * @param point the {@link java.awt.Point} to move the {@link Mouse} to
     * @return <code>true</code> if the {@link Mouse} has been successfully moved
     */
    public boolean move(final Point point) {
        return point != null && move(point.x, point.y);
    }

    /**
     * This method is used to move the {@link Mouse}
     *
     * @param x the x position to move the {@link Mouse} to
     * @param y the y position to move the {@link Mouse} to
     * @return <code>true</code> if the {@link Mouse} has been successfully moved
     */
    public boolean move(final int x, final int y) {
        active = true;
        boolean moved = executor.move(this, x, y);
        active = false;
        return moved;
    }

    /**
     * This method is used to move the {@link Mouse}
     *
     * @param rectangle the {@link java.awt.Rectangle} to move the {@link Mouse} to
     * @return <code>true</code> if the {@link Mouse} has been successfully moved
     */
    public boolean move(final Rectangle rectangle) {
        return rectangle != null && move(Random.nextInt(0, rectangle.width) + rectangle.x, Random.nextInt(0, rectangle.height) + rectangle.y);
    }

}
