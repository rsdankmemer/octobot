package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Model;
import org.octobot.script.wrappers.Player;

import java.awt.*;

/**
 * PlayerRenderEvent
 *
 * @author Pat-ji
 */
public class PlayerRenderEvent extends TextRenderEvent {

    public PlayerRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        if (context().game.isLoggedIn()) {
            final Player[] players = context().players.find().all();
            if (players != null && players.length > 0) {
                for (final Player player : players) {
                    final Model model = player.getModel();
                    if (model != null && model.contains(context().mouse.getLocation())) {
                        final Polygon[] triangles = model.getTriangles();
                        if (triangles != null && triangles.length > 0) {
                            g.setColor(new Color(0, 0, 255, 60));
                            for (final Polygon polygon : triangles)
                                g.drawPolygon(polygon);

                            final Point point = model.getCentralPoint();
                            if (point != null) {
                                point.y -= 10;
                                g.setColor(Color.WHITE);
                                g.drawString("[name=" + player.getName() + ", animation=" + player.getAnimation() + ", prayerIcon=" + player.getPrayerIcon() + "]", point.x - 130, point.y);
                                g.drawString("[interacting=" + player.getInteractingIndex() + ", health="+ player.getHealth() + "/" + player.getMaxHealth()
                                        +  ", location=" + player.getLocation().toString() + "]", point.x - 158, point.y + 20);
                            }
                        }
                    }
                }
            }

            final Player local = context().players.getLocal();
            if (local != null) {
                g.setColor(Color.WHITE);
                g.drawString("[Player] - [animation=" + local.getAnimation() + ", location[" + local.getLocation() + "]]", 10, getY());
            }
        }
    }

}
