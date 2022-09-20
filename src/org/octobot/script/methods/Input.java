package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;

import java.applet.Applet;

/**
 * Input
 *
 * @author Pat-ji
 */
public class Input extends ContextProvider {
    private final Applet applet;

    public Input(final ScriptContext context, final Applet applet) {
        super(context);

        this.applet = applet;
    }

    /**
     * This method is used by the classes extending {@link Input} to create input events on
     *
     * @return the games {@link java.applet.Applet}
     */
    public Applet getApplet() {
        return applet;
    }

}
