package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * RegionRenderEvent
 *
 * @author Pat-ji
 */
public class RegionRenderEvent extends TextRenderEvent {

    public RegionRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("[Region] - [xBase=" + context().game.getBaseX() + ", yBase=" + context().game.getBaseY()
                + ", plane=" + context().game.getPlane() + "]", 10, getY());
    }

}
