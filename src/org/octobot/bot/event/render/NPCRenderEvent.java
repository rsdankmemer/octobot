package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Model;
import org.octobot.script.wrappers.NPC;

import java.awt.*;

/**
 * NPCRenderEvent
 *
 * @author Pat-ji
 */
public class NPCRenderEvent extends RenderEvent {

    public NPCRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        if (context().game.isLoggedIn()) {
            final NPC[] npcs = context().npcs.find().all();
            if (npcs != null && npcs.length > 0) {
                for (final NPC npc : npcs) {
                    final Model model = npc.getModel();
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
                                g.drawString("[name=" + npc.getName() + ", id=" + npc.getId() + ", animation=" + npc.getAnimation() + " , height=" + npc.getHeight() + "]", point.x - 130, point.y);
                                g.drawString("[level=" + npc.getLevel() + ", interacting=" + npc.getInteractingIndex() + ", health=" + npc.getHealth() + "/" + npc.getMaxHealth()
                                        + ", location=" + npc.getLocation().toString() + "]", point.x - 165, point.y + 20);
                            }
                        }
                    }
                }
            }
        }
    }

}
