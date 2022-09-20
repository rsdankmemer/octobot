package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.GroundItem;
import org.octobot.script.wrappers.Model;
import org.octobot.script.wrappers.Tile;

import java.awt.*;

/**
 * GroundItemRenderEvent
 *
 * @author Pat-ji
 */
public class GroundItemRenderEvent extends RenderEvent {

    public GroundItemRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        if (context().game.isLoggedIn()) {
            final FontMetrics metrics = g.getFontMetrics();

            final Tile location = context().players.getLocal().getLocation();
            final int xBase = context().game.getBaseX();
            final int yBase = context().game.getBaseY();
            for (int x = location.getX() - 20; x < location.getX() + 20; x++) {
                for (int y = location.getY() - 20; y < location.getY() + 20; y++) {
                    final GroundItem[] items = context().groundItems.getAllAt(x - xBase, y - yBase);
                    if (items == null || items.length < 1) continue;

                    int i = 0;
                    for (final GroundItem item : items) {
                        final Model model = item.getModel();
                        if (model != null && model.contains(context().mouse.getLocation())) {
                            final Polygon[] triangles = model.getTriangles();
                            if (triangles != null && triangles.length > 0) {
                                g.setColor(new Color(0, 0, 255, 60));
                                for (final Polygon polygon : triangles)
                                    g.drawPolygon(polygon);

                                final Point point = item.getLocation().getMatrix(context()).getCentralPoint();
                                if (point == null) continue;

                                g.setColor(Color.WHITE);
                                final String text = "[name=" + item.getName() + ", id=" + item.getId() + ", location=" + item.getLocation().toString() + "]";
                                g.drawString(text, point.x - metrics.stringWidth(text) / 2, point.y - metrics.getHeight() / 2 - i++ * 15);
                            }
                        }
                    }
                }
            }
        }
    }

}
