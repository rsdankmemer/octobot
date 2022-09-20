package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

/**
 * TextRenderEvent
 *
 * @author Pat-ji
 */
public abstract class TextRenderEvent extends RenderEvent {
    public int index;

    public TextRenderEvent(final ScriptContext context) {
        super(context);
    }

    protected int getY() {
        return 35 + index * 25;
    }

}
