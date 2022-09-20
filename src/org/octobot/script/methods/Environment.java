package org.octobot.script.methods;

import org.octobot.Application;
import org.octobot.bot.game.script.randoms.RandomScript;
import org.octobot.bot.handler.RandomHandler;
import org.octobot.bot.internal.Rank;
import org.octobot.bot.internal.ui.component.UtilityBar;
import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.RandomEvent;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * Environment
 *
 * @author Pat-ji
 */
public class Environment extends ContextProvider {
    private RandomHandler randomHandler;

    public Environment(final ScriptContext context, final RandomHandler randomHandler) {
        super(context);

        this.randomHandler = randomHandler;
    }

    /**
     * This method is used to check if the {@link org.octobot.bot.game.script.GameScript} is running with the {@link org.octobot.bot.game.script.GameScript#running()} method
     *
     * @return <code>true</code> if the {@link org.octobot.bot.game.script.GameScript} is running
     */
    public boolean isScriptRunning() {
        try {
            return randomHandler.getScriptHandler().getScript().running();
        } catch (final Exception e) {
            return false;
        }
    }

    RandomScript getRandom(final String name) {
        return randomHandler.getRandom(name);
    }

    /**
     * This method is used to take a screen shot from the games {@link java.awt.Canvas}
     *
     * @param name the name to save the image as
     * @return <code>true</code> if an image has successfully been taken
     */
    public boolean takeScreenShot(final String name) {
        try {
            final String dir = org.octobot.bot.Environment.getScreenshotsDirectory();
            if (!org.octobot.bot.Environment.checkFolder(dir)) return false;

            ImageIO.write(context().game.getCanvas().clientBuffer, "JPEG", new File(dir + File.separator + name + ".jpeg"));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This method is used to enable of disable a given {@link RandomEvent.Event}
     *
     * @param event the {@link RandomEvent.Event} to enable or disable
     * @param enabled <code>true</code> for enabling, <code>false</code> for disabling
     */
    public void setRandomEnabled(final RandomEvent.Event event, final boolean enabled) {
        randomHandler.setEnabled(event, enabled);
    }

    /**
     * This method is used to set the status bar text
     *
     * @param text the text to set the status bar text to
     */
    public void setStatusBarText(final String text) {
        UtilityBar.instance.statusLabel.setText(text);
    }

    /**
     * This method is used to get the users username
     *
     * @return the users username
     */
    public String getUsername() {
        return Application.username;
    }

    /**
     * This method is used to check if the user has vip access
     *
     * @return <code>true</code> if the user has vip access
     */
    public boolean isUserVip() {
        return (Application.rank & Rank.STAFF.mask) == Rank.STAFF.mask || (Application.rank & Rank.VIP.mask) == Rank.VIP.mask;
    }

    /**
     * This method is used to check if the user has sponsor access
     *
     * @return <code>true</code> if the user has sponsor access
     */
    public boolean isUserSponsor() {
        return (Application.rank & Rank.STAFF.mask) == Rank.STAFF.mask || (Application.rank & Rank.SPONSOR.mask) == Rank.SPONSOR.mask;
    }

}
