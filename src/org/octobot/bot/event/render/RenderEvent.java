package org.octobot.bot.event.render;

import org.octobot.script.ContextProvider;
import org.octobot.script.ScriptContext;
import org.octobot.script.event.listeners.PaintListener;

/**
 * RenderEvent
 *
 * @author Pat-ji
 */
public abstract class RenderEvent extends ContextProvider implements PaintListener {

    public RenderEvent(final ScriptContext context) {
        super(context);
    }

}
