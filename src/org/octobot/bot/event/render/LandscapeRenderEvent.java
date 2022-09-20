package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * LandscapeRenderEvent
 *
 * @author Pat-ji
 */
public class LandscapeRenderEvent extends RenderEvent {

    public LandscapeRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        context().landscape.render(g);
    }

}
