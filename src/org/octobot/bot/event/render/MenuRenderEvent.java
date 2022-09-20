package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * MenuRenderEvent
 *
 * @author Pat-ji
 */
public class MenuRenderEvent extends TextRenderEvent {

    public MenuRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("[Menu] - [x=" + context().menu.getX() + ", y=" + context().menu.getY()
                + ", width=" + context().menu.getWidth() + ", height=" + context().menu.getHeight() + ", open=" + context().menu.isOpen() + "]", 10, getY());
    }

}
