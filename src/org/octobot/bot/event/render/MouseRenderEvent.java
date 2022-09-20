package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Tile;

import java.awt.*;

/**
 * MouseRenderEvent
 *
 * @author Pat-ji
 */
public class MouseRenderEvent extends TextRenderEvent {

    public MouseRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        final int x = context().mouse.getX();
        final int y = context().mouse.getY();

        g.setColor(Color.WHITE);
        final Tile tile = context().calculations.getTileUnderMouse();
        if (tile != null) {
            g.drawString("[Mouse] - [x=" + x + ", y=" + y + ", tileX=" + tile.getX() + ", tileY=" + tile.getY() + ", crosshair=" + context().mouse.getCurrentCrosshair() + "]", 10, getY());
            g.setColor(new Color(255, 0, 0, 100));
            tile.getMatrix(context()).render(g);
        } else {
            g.drawString("[Mouse] - [x=" + x + ", y=" + y + ", crosshair=" + context().mouse.getCurrentCrosshair() + "]", 10, getY());
        }

        g.setColor(Color.GREEN);
        g.drawLine(x - 5, y - 5, x + 5, y + 5);
        g.drawLine(x + 5, y - 5, x - 5, y + 5);
    }

}
