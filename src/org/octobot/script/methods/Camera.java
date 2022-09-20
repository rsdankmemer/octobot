package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.Locatable;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Time;
import org.octobot.script.util.Timer;
import org.octobot.script.wrappers.Tile;

import java.awt.event.KeyEvent;

/**
 * Camera
 *
 * @author Pat-ji
 */
public class Camera extends ContextProvider {

    public Camera(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the {@link Camera}s x location
     *
     * @return the {@link Camera}s x location
     */
    public int getX() {
        return context().client.getCameraX();
    }

    /**
     * This method is used to get the {@link Camera}s z location
     *
     * @return the {@link Camera}s x location
     */
    public int getY() {
        return context().client.getCameraY();
    }

    /**
     * This method is used to get the {@link Camera}s z location
     *
     * @return the {@link Camera}s z location
     */
    public int getZ() {
        return context().client.getCameraZ();
    }

    /**
     * This method is used to get the {@link Camera}s x curve
     *
     * @return the {@link Camera}s x curve
     */
    public int getCurveX() {
        return context().client.getCameraCurveX();
    }

    /**
     * This method is used to get the {@link Camera}s y curve
     *
     * @return the {@link Camera}s y curve
     */
    public int getCurveY() {
        return context().client.getCameraCurveY();
    }

    /**
     * This method is used to get the {@link Camera}s view rotation
     *
     * @return the {@link Camera}s view rotation
     */
    public int getViewRotation() {
        return context().client.getViewRotation();
    }

    /**
     * This method is used to get the {@link Camera}s mini map rotation
     *
     * @return the {@link Camera}s mini map rotation
     */
    public int getMiniMapRotation() {
        return context().client.getMinimapRotation();
    }

    /**
     * This method is used to get the {@link Camera}s mini map zoom
     *
     * @return the {@link Camera}s mini map zoom
     */
    public int getMiniMapZoom() {
        return context().client.getMinimapZoom();
    }

    /**
     * This method is used to get the {@link Camera}s angle
     *
     * @return the {@link Camera}s angle
     */
    public int getAngle() {
        return (int) ((getCurveX() / 2050.0) * 360);
    }

    /**
     * This method is used to get the {@link Camera}s pitch
     *
     * @return the {@link Camera}s pitch
     */
    public int getPitch() {
        return (int) ((getCurveY() - 128.0) / 255 * 100);
    }

    /**
     * This method is used to rotate the {@link Camera} to a {@link Locatable}
     *
     * @param locatable the {@link Locatable} to rotate to
     */
    public void turnTo(final Locatable locatable) {
        setAngle(getAngleTo(locatable));
    }

    /**
     * This method is used to get the angle to a {@link Locatable}
     *
     * @param locatable the {@link Locatable} to get the angle to
     * @return the angle to a {@link Locatable}
     */
    public int getAngleTo(final Locatable locatable) {
        return getAngleTo(locatable.getLocation());
    }

    /**
     * This method is used to get the angle to a {@link Tile}
     *
     * @param tile the {@link Tile} to get the angle to
     * @return the angle to a {@link Tile}
     */
    public int getAngleTo(final Tile tile) {
        return getAngle(context().players.getLocal(), tile);
    }

    private int getAngle(final Locatable base, final Locatable dest) {
        return getAngle(base.getLocation(), dest.getLocation());
    }

    private int getAngle(final Tile base, final Tile dest) {
        final int angle = (int) Math.toDegrees(Math.atan2(dest.getY() - base.getY(), dest.getX() - base.getX()));
        return angle >= 0 ? angle : 360 + angle;
    }

    /**
     * This method is used to get the angle to a degree
     *
     * @param degrees the degrees to get the angle to
     * @return the angle to a degree
     */
    public int getAngleTo(final int degrees) {
        int angle = getAngle();
        if (angle < degrees)
            angle += 360;

        final int result = angle - degrees;
        return result > 180 ? result - 360 : result;
    }

    /**
     * This method is used to set the pitch in another thread, thereby not interrupting the script
     *
     * @param pitch the pitch to set the {@link Camera}s pitch to
     */
    public void setPitchThreaded(final int pitch) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setPitch(pitch);
            }
        }).start();
    }

    /**
     * This method is used to set the pitch to a given pitch
     *
     * @param pitch the pitch to set the {@link Camera}s pitch to
     */
    public void setPitch(int pitch) {
        if (pitch > 100)
            pitch = 100;
        if (pitch < 0)
            pitch = 0;

        final Timer timer = new Timer(5000);
        if (pitch > getPitch()) {
            context().keyboard.pressKey((char) KeyEvent.VK_UP);
            while (context().game.isLoggedIn() && pitch - 10 > getPitch() && timer.isRunning()) {
                Time.sleep(10);
            }

            context().keyboard.releaseKey((char) KeyEvent.VK_UP);
        } else {
            context().keyboard.pressKey((char) KeyEvent.VK_DOWN);
            while (context().game.isLoggedIn() && pitch + 10 < getPitch() && timer.isRunning()) {
                Time.sleep(10);
            }

            context().keyboard.releaseKey((char) KeyEvent.VK_DOWN);
        }
    }

    /**
     * This method is used to set the angle in another thread, thereby not interrupting the script
     *
     * @param angle the angle to set the {@link Camera}s angle to
     */
    public void setAngleThreaded(final int angle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setAngle(angle);
            }
        }).start();
    }

    /**
     * This method is used to set the angle to a given angle
     *
     * @param angle the angle to set the {@link Camera}s angle to
     */
    public void setAngle(int angle) {
        if (angle < 0)
            angle = 0;
        if (getAngle() + angle >= 360)
            angle -= 360;

        final Timer timer = new Timer(5000);
        final int difference = getAngleTo(angle);
        if (difference > 5) {
            context().keyboard.pressKey((char) KeyEvent.VK_LEFT);
            while (context().game.isLoggedIn() && getAngleTo(angle) > 5 && timer.isRunning()) {
                Time.sleep(10);
            }

            context().keyboard.releaseKey((char) KeyEvent.VK_LEFT);
        } else if (difference < -5) {
            context().keyboard.pressKey((char) KeyEvent.VK_RIGHT);
            while (context().game.isLoggedIn() && getAngleTo(angle) < -5 && timer.isRunning()) {
                Time.sleep(10);
            }

            context().keyboard.releaseKey((char) KeyEvent.VK_RIGHT);
        }
    }

}
