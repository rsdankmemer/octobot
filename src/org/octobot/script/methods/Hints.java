package org.octobot.script.methods;

import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;

/**
 * Hints
 *
 * @author Pat-ji
 */
public class Hints extends ContextProvider {

    public Hints(final ScriptContext context) {
        super(context);
    }

    /**
     * This method is used to get the {@link Hints} icon type
     *
     * @return the {@link Hints} icon type
     */
    public int getIconType() {
        return context().client.getHintIconType();
    }

    /**
     * This method is used to get the {@link Hints} x location
     *
     * @return the {@link Hints} x location
     */
    public int getIconX() {
        return context().client.getHintIconX();
    }

    /**
     * This method is used to get the {@link Hints} y location
     *
     * @return the {@link Hints} y location
     */
    public int getIconY() {
        return context().client.getHintIconY();
    }

    /**
     * This method is used to get the {@link Hints} actor id
     *
     * @return the {@link Hints} actor id
     */
    public int getIconActorId() {
        return context().client.getHintIconActorId();
    }

    /**
     * This method is used to get the {@link Hints} icon id
     *
     * @return the {@link Hints} icon id
     */
    public int getIconId() {
        return context().client.getHintIconId();
    }

}
