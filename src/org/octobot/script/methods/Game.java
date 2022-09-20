package org.octobot.script.methods;

import org.octobot.bot.game.client.RSNodeIterable;
import org.octobot.bot.game.loader.internal.wrappers.input.Canvas;
import org.octobot.script.Condition;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.util.Time;
import org.octobot.script.wrappers.Component;

/**
 * Game
 *
 * @author Pat-ji
 */
public class Game extends ContextProvider {

    public Game(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to check is the game state is equal to {@link State}.LOGGED_IN
     *
     * @return <code>true</code> if the game state is equal to {@link State}.LOGGED_IN
     */
    public boolean isLoggedIn() {
        return getGameState() == State.LOGGED_IN;
    }

    /**
     * This method is used to get the {@link Canvas} of the game
     *
     * @return the {@link Canvas} of the game
     */
    public Canvas getCanvas() {
        return (Canvas) context().client.getCanvas();
    }

    /**
     * This method is used to get the color at a specific position on the screen
     *
     * @param x the x position to get the color at
     * @param y the y position to get the color at
     * @return the color at a specific position on the screen
     */
    public int getColorAt(final int x, final int y) {
        return getCanvas().getColorAt(x, y);
    }

    /**
     * This method is used to get the tile heights data
     *
     * @return the tile heights data
     */
    public int[][][] getTileHeights() {
        return context().client.getTileHeights();
    }

    /**
     * This method is used to get the tile settings data
     *
     * @return the tile settings data
     */
    public byte[][][] getTileSettings() {
        return context().client.getTileSettings();
    }

    /**
     * This method is used to get the login index
     *
     * @return the login index
     */
    public int getLoginIndex() {
        return context().client.getLoginIndex();
    }

    /**
     * This method is used to get the current game state
     *
     * @return the current game state
     */
    public int getGameState() {
        return context().client.getGameState();
    }

    /**
     * This method is used to get the current plane
     *
     * @return the current plane
     */
    public int getPlane() {
        return context().client.getPlane();
    }

    /**
     * This method is used to get the x base offset
     *
     * @return the x base offset
     */
    public int getBaseX() {
        return context().client.getBaseX();
    }

    /**
     * This method is used to get the y base offset
     *
     * @return the y base offset
     */
    public int getBaseY() {
        return context().client.getBaseY();
    }

    /**
     * This method is used to get the current game cycle
     *
     * @return the current game cycle
     */
    public int getCurrentCycle() {
        return context().client.getCurrentCycle();
    }

    /**
     * This method is used to get the {@link RSNodeIterable} containing the chat messages
     *
     * @return the {@link RSNodeIterable} containing the chat messages
     */
    public RSNodeIterable getMessages() {
        return context().client.getMessages();
    }

    /**
     * This method is used to log out the account
     *
     * @return <code>true</code> if the account has successfully been logged out
     */
    public boolean logout() {
        if (getGameState() == State.LOBBY) return true;

        if (context().tabs.isOpen(Tabs.Tab.LOGOUT)) {
            final Component component = context().widgets.getComponent(182, 6);
            if (component != null && component.click(true))
                Time.sleep(new Condition() {
                    @Override
                    public boolean validate() {
                        return isLoggedIn();
                    }
                }, 3000);
        } else if (context().tabs.open(Tabs.Tab.LOGOUT)) {
            Time.sleep(200, 400);
            return logout();
        }

        return !isLoggedIn();
    }

    /**
     * State
     *
     * @author Pat-ji
     */
    public static class State {
        public static final int LOBBY = 10;
        public static final int LOGGED_IN = 30;
        public static final int LOGGING_IN = 20;
        public static final int LOADING = 25;

    }

}
