package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.wrappers.*;

import java.awt.*;

/**
 * GameObjectRenderEvent
 *
 * @author Pat-ji
 */
public class GameObjectRenderEvent extends RenderEvent {

    public GameObjectRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        if (context().game.isLoggedIn()) {
            final FontMetrics metrics = g.getFontMetrics();

            final Tile location = context().players.getLocal().getLocation();
            for (int x = location.getX() - 15; x < location.getX() + 15; x++) {
                for (int y = location.getY() - 15; y < location.getY() + 15; y++) {
                    final GameObject[] objects = context().objects.getAllAt(new Tile(x, y));
                    if (objects == null || objects.length < 1) continue;

                    int i = 0;
                    for (final GameObject object : objects) {
                        final Model model = object.getModel();
                        if (model != null && model.contains(context().mouse.getLocation())) {
                            final Polygon[] triangles = model.getTriangles();
                            if (triangles != null && triangles.length > 0) {
                                g.setColor(new Color(0, 0, 255, 60));
                                for (final Polygon polygon : triangles)
                                    g.drawPolygon(polygon);

                                final Point point = model.getCentralPoint();
                                if (point == null) continue;

                                final ObjectDefinition def = object.getDefinition();
                                final String text = "[" + (def != null && !"null".equals(def.getName()) ? "name=" +  def.getName() + ", " : "")
                                        + "id=" + object.getId() + ", x=" + object.getX() + ", y=" + object.getY() + "]";
                                g.setColor(object instanceof WallDecoration ? Color.GREEN : object instanceof WallObject ? Color.BLACK : object instanceof GroundDecoration ? Color.YELLOW : Color.WHITE);
                                g.drawString(text, point.x - metrics.stringWidth(text) / 2, point.y - metrics.getHeight() / 2 - i++ * 15);
                            }
                        }
                    }
                }
            }

//            GameObject[] test = context().objects.getAllAt(new Tile(3091, 3243));
            GameObject[] test = context().objects.getAllAt(new Tile(3093, 3244));
            if (test != null && test.length > 0) {
                GameObject test2 = test[0];
                Model testModel = test2.getModel();

                if (testModel != null) {
                    g.drawString("VALID", 100, 100);
//                    testModel.getCentralPoint();
                    g.drawString("x verts: " + testModel.getXVertices().length, 100, 120);
                    g.drawString("y verts: " + testModel.getYVertices().length, 100, 130);
                    g.drawString("z verts: " + testModel.getZVertices().length, 100, 140);
                    g.drawString("x triangles: " + testModel.getXTriangles().length, 100, 150);
                    g.drawString("y triangles: " + testModel.getYTriangles().length, 100, 160);
                    g.drawString("z triangles: " + testModel.getZTriangles().length, 100, 170);
                    g.drawString("tri count: " + testModel.getTriangles().length, 100, 190);
                    g.drawString("x: " + testModel.x, 100, 210);
                    g.drawString("y: " + testModel.y, 100, 220);
                } else {
                    g.drawString("NULL", 100, 100);
                }
            }
        }
    }

}
