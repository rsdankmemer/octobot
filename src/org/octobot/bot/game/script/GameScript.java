package org.octobot.bot.game.script;

import org.octobot.bot.handler.ScriptHandler;
import org.octobot.script.Executable;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.RandomEvent;
import org.octobot.script.event.listeners.GameEventListener;
import org.octobot.script.util.Timer;

/**
 * GameScript
 *
 * @author Pat-ji
 */
public abstract class GameScript implements GameEventListener, Executable {
    private ScriptHandler scriptHandler;
    private Timer runTime;

    /**
     * This method is used for validating the start of the {@link GameScript}
     *
     * @return <code>true</code> for the {@link GameScript} to start executing
     */
    public abstract boolean start();

    /**
     * This method is used for validating whether or not the script is in an acceptable state for taking a break.
     * This method will be polled whenever the bot wishes to take a break.
     *
     * @return <code>true</code> to allow the bot to take a break at this moment.
     */
    public abstract boolean mayBreak();


    /**
     * This method is called just before the {@link GameScript} stops executing
     */
    public void onStop() {
    }

    /**
     * This method is used as a shortcut to get the {@link ScriptContext}
     *
     * @return the {@link GameScript}s parent {@link ScriptContext}
     */
    public ScriptContext context() {
        return scriptHandler.context();
    }

    /**
     * This method is used to stop the {@link GameScript}
     */
    public final void stop() {
        scriptHandler.stopScript();
    }

    /**
     * This method is used to pause the {@link GameScript}
     */
    public final void pause() {
        scriptHandler.paused = true;
    }

    /**
     * This method is used to resume the {@link GameScript} and should be called from another {@link Thread} to properly work
     */
    public final void resume() {
        scriptHandler.paused = false;
    }

    /**
     * This method is used to set the {@link ScriptHandler} for the {@link GameScript}
     * This is automatically done by the client, and should not be used by users
     *
     * @param handler the {@link ScriptHandler} to set
     */
    public final void setScriptHandler(final ScriptHandler handler) {
        scriptHandler = handler;
    }

    /**
     * This method is used to add a {@link GameEventListener} to the event queue
     *
     * @param listener the {@link GameEventListener} to add
     */
    public final void addGameEventListener(final GameEventListener listener) {
        scriptHandler.addGameEventListner(listener);
    }

    /**
     * This method is used to check if the {@link GameScript} is executing and should be used in every while loop to prevent infinite loops
     *
     * @return <code>true</code> if the {@link GameScript} is executing
     */
    public final boolean running() {
        return !scriptHandler.paused && scriptHandler.getRandomHandler().getActive() == null && scriptHandler.getScript() != null;
    }

    /**
     * This method is used to claim the execution over a {@link RandomEvent.Event}
     *
     * @param event the {@link RandomEvent.Event} to claim the execution over
     * @param executable the new {@link Executable} for this {@link RandomEvent.Event}
     */
    public final void claim(final RandomEvent.Event event, final Executable executable) {
        scriptHandler.getRandomHandler().claims.put(event, executable);
    }

    /**
     * This method is used to get the {@link GameScript}s runtime
     *
     * @return the {@link GameScript}s runtime
     */
    public final Timer getRunTime() {
        return runTime;
    }

    /**
     * This method is used to set the {@link GameScript}s runtime {@link Timer}
     *
     * @param runTime the {@link Timer} to set the {@link GameScript}s runtime to
     */
    public final void setRunTime(final Timer runTime) {
        this.runTime = runTime;
    }

    /**
     * This method is used to set the {@link ScriptHandler}s {@link ScriptContext}
     *
     * @param context the {@link ScriptContext} to use
     */
    protected final void setScriptContext(final ScriptContext context) {
        scriptHandler.setScriptContext(context);
    }

    /**
     * This method is used to get the {@link GameScript}s {@link ScriptManifest}
     *
     * @return the {@link GameScript}s {@link ScriptManifest}
     */
    public final ScriptManifest manifest() {
        final ScriptManifest manifest = getClass().getAnnotation(ScriptManifest.class);
        if (manifest == null)
            throw new NullPointerException("[GameScript] - A ScriptManifest has not been added to the script.");

        return manifest;
    }

}
