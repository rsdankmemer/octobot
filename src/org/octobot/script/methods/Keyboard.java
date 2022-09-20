package org.octobot.script.methods;

import org.octobot.script.ScriptContext;
import org.octobot.script.util.Random;
import org.octobot.script.util.Time;

import java.applet.Applet;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Keyboard
 *
 * @author Pat-ji
 */
public class Keyboard extends Input {
    private final org.octobot.bot.game.loader.internal.wrappers.input.Keyboard keyboard;

    public Keyboard(final ScriptContext context, final Applet applet, final org.octobot.bot.game.loader.internal.wrappers.input.Keyboard keyboard) {
        super(context, applet);

        this.keyboard = keyboard;
    }

    private char charKey(final char c) {
        return c >= 36 && c <= 40 ? KeyEvent.VK_UNDEFINED : c;
    }

    private boolean sendChar(final char c, final int delay) {
        boolean shift = false;
        int code = c;
        if (c >= 'a' && c <= 'z') {
            code -= 32;
        } else if (c >= 'A' && c <= 'Z') {
            shift = true;
        }

        KeyEvent event;
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
            event = new KeyEvent(getApplet(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + delay, 0, code, charKey(c), KeyEvent.KEY_LOCATION_STANDARD);
            keyboard.keyPressed(event);

            event = new KeyEvent(getApplet(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + Random.nextInt(50, 120) + Random.nextInt(0, 100), 0, code, charKey(c), KeyEvent.KEY_LOCATION_STANDARD);
            keyboard.keyReleased(event);
        } else {
            if (!shift) {
                event = new KeyEvent(getApplet(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + delay, 0, code, charKey(c), KeyEvent.KEY_LOCATION_STANDARD);
                keyboard.keyPressed(event);

                event = new KeyEvent(getApplet(), KeyEvent.KEY_TYPED, System.currentTimeMillis() + 0, 0, 0, c, 0);
                keyboard.keyTyped(event);

                event = new KeyEvent(getApplet(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + Random.nextInt(50, 120) + Random.nextInt(0, 100), 0, code, charKey(c), KeyEvent.KEY_LOCATION_STANDARD);
                keyboard.keyReleased(event);
            } else {
                event = new KeyEvent(getApplet(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + Random.nextInt(25, 60) + Random.nextInt(0, 50), InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED, KeyEvent.KEY_LOCATION_LEFT);
                keyboard.keyPressed(event);

                event = new KeyEvent(getApplet(), KeyEvent.KEY_PRESSED, System.currentTimeMillis() + delay, InputEvent.SHIFT_DOWN_MASK, code, charKey(c), KeyEvent.KEY_LOCATION_STANDARD);
                keyboard.keyPressed(event);

                event = new KeyEvent(getApplet(), KeyEvent.KEY_TYPED, System.currentTimeMillis() + 0, InputEvent.SHIFT_DOWN_MASK, 0, c, 0);
                keyboard.keyTyped(event);

                event = new KeyEvent(getApplet(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + Random.nextInt(50, 120) + Random.nextInt(0, 100), InputEvent.SHIFT_DOWN_MASK, code, charKey(c), KeyEvent.KEY_LOCATION_STANDARD);
                keyboard.keyReleased(event);

                event = new KeyEvent(getApplet(), KeyEvent.KEY_RELEASED, System.currentTimeMillis() + Random.nextInt(25, 60) + Random.nextInt(0, 50), InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED, KeyEvent.KEY_LOCATION_LEFT);
                keyboard.keyReleased(event);
            }
        }

        return true;
    }

    /**
     * This method is used to send a {@link String} of text
     *
     * @param text the text to send
     * @param enter <code>true</code> if there should be an enter_event after typing the text
     * @return <code>true</code> if the text has been successfully typed
     */
    public boolean sendText(final String text, final boolean enter) {
        return sendText(text, enter, 50, 100);
    }

    /**
     * This method is used to send a {@link String} of text
     *
     * @param text the text to send
     * @param enter <code>true</code> if there should be an enter_event after typing the text
     * @param delay the delay in ms between every key press
     * @return <code>true</code> if the text has been successfully typed
     */
    public boolean sendText(final String text, final boolean enter, final int delay) {
        return sendText(text, enter, delay, delay);
    }

    /**
     * This method is used to send a {@link String} of text
     *
     * @param text the text to send
     * @param enter <code>true</code> if there should be an enter_event after typing the text
     * @param minDelay the minimum delay between every key press
     * @param maxDelay the maximum delay between every key press
     * @return <code>true</code> if the text has been successfully typed
     */
    public boolean sendText(final String text, final boolean enter, final int minDelay, final int maxDelay) {
        final char[] array = text.toCharArray();
        if (array == null || array.length == 0) return false;

        for (final char element : array) {
            sendChar(element, Random.nextInt(minDelay, maxDelay));
            Time.sleep(Random.nextInt(minDelay, maxDelay));
        }

        if (enter)
            sendChar((char) KeyEvent.VK_ENTER, Random.nextInt(minDelay, maxDelay));

        return true;
    }

    /**
     * This method is used to send a {@link java.awt.event.KeyEvent.KEY_RELEASED} event
     *
     * @param c the char to release
     * @return <code>true</code> after sending the event
     */
    public boolean releaseKey(final char c) {
        keyboard.keyReleased(new KeyEvent(getApplet(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), InputEvent.ALT_DOWN_MASK, c, charKey(c)));
        return true;
    }

    /**
     * This method is used to send a {@link java.awt.event.KeyEvent.KEY_PRESSED} event
     *
     * @param c the char to release
     * @return <code>true</code> after sending the event
     */
    public boolean pressKey(final char c) {
        keyboard.keyPressed(new KeyEvent(getApplet(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, c, charKey(c)));
        return true;
    }

}
