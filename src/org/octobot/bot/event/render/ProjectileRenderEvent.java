package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.Model;
import org.octobot.script.wrappers.Projectile;

import java.awt.*;

/**
 * ProjectileRenderEvent
 * 
 * @author Pat-ji
 */
public class ProjectileRenderEvent extends RenderEvent {
    
    public ProjectileRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        if (context().game.isLoggedIn()) {
            final Projectile[] projectiles = context().projectiles.find().all();
            if (projectiles != null && projectiles.length > 0) {
                for (final Projectile projectile : projectiles) {
                    final Model model = projectile.getModel();
                    if (model != null && model.contains(context().mouse.getLocation())) {
                        final Polygon[] triangles = model.getTriangles();
                        if (triangles != null && triangles.length > 0) {
                            g.setColor(new Color(0, 0, 255, 60));
                            for (final Polygon polygon : triangles)
                                g.drawPolygon(polygon);
                        }
                    }
                }
            }
        }
    }
    
}
