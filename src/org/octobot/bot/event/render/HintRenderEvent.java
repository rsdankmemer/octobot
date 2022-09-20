package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * HintRenderEvent
 *
 * @author Pat-ji
 */
public class HintRenderEvent extends TextRenderEvent {

    public HintRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("[Hint] - [x=" + context().hints.getIconX() + ", y=" + context().hints.getIconY()
                + ", id=" + context().hints.getIconId() + ", actor_id=" + context().hints.getIconActorId() + ", type=" + context().hints.getIconType() + "]", 10, getY());
    }

}
