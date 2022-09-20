package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * ColorPickerRenderEvent
 *
 * @author Pat-ji
 */
public class ColorPickerRenderEvent extends RenderEvent {

    public ColorPickerRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        final int x = context().mouse.getX();
        final int y = context().mouse.getY();
        final Color color = new Color(context().game.getCanvas().getColorAt(x, y));
        g.setColor(Color.BLACK);
        g.drawString("Color: r=" + color.getRed() + ", g=" + color.getGreen() + ", b=" + color.getBlue(), x - 60, y + 30);
        g.setColor(color);
        g.fillRect(x - 20, y + 40, 40, 40);
    }
}
