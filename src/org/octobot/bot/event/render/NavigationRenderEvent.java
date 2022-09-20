package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * Created by Joseph on 12/20/2014.
 */
public class NavigationRenderEvent extends RenderEvent {

    public NavigationRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(Graphics g) {
        context().navigation.render(g);
    }
}
