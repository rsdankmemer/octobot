package org.octobot.bot.game.loader.internal.wrappers.input;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.event.render.TextRenderEvent;
import org.octobot.bot.game.script.GameScript;
import org.octobot.bot.game.script.Task;
import org.octobot.bot.game.script.TaskScript;
import org.octobot.bot.internal.ui.WidgetExplorerFrame;
import org.octobot.bot.internal.ui.component.Colors;
import org.octobot.script.event.listeners.PaintListener;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Canvas
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class Canvas extends java.awt.Canvas implements Input {
    private static final Font ARIAL;

    public final BufferedImage gameBuffer;
    public final BufferedImage clientBuffer;

    private final GameDefinition definition;

    public boolean renderingDisabled;

    static {
        ARIAL = new Font("Arial", 0, 12);
    }

    public Canvas() {
        gameBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);
        clientBuffer = new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

        definition = context.getBot(this);
        definition.canvas = this;
        definition.context.build(definition);
    }

    @Override
    public Graphics getGraphics() {
        final Graphics g = clientBuffer.getGraphics();
        g.drawImage(gameBuffer, 0, 0, null);

        if (renderingDisabled) {
            g.dispose();
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException ignored) { }
            return gameBuffer.getGraphics();
        }

        if (definition != null) {
            final GameScript script = definition.scriptHandler.getScript();
            if (script != null) {
                if (script instanceof PaintListener)
                    ((PaintListener) script).render(g);

                if (script instanceof TaskScript)
                    for (final Task task : ((TaskScript) script).getTasks())
                        if (task != null && task instanceof PaintListener)
                            ((PaintListener) task).render(g);

                g.setFont(ARIAL);
                if (definition.randomHandler.getActive() != null) {
                    g.setColor(Colors.DEEP_RED);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    if (definition.randomHandler.getActive() instanceof PaintListener)
                        ((PaintListener) definition.randomHandler.getActive()).render(g);
                }
            }

            g.setFont(ARIAL);
            int textIndex = 0;
            for (final PaintListener renderEvent : definition.renderEvents.values())
                if (renderEvent != null) {
                    if (renderEvent instanceof TextRenderEvent)
                        ((TextRenderEvent) renderEvent).index = textIndex++;

                    if (renderEvent instanceof WidgetExplorerFrame && !((WidgetExplorerFrame) renderEvent).isVisible()) {
                        definition.renderEvents.remove("widgets");
                        continue;
                    }

                    renderEvent.render(g);
                }
        }

        if (definition.disableInput) {
            g.setColor(Colors.DEEP_RED);
            g.fillRect(getWidth() - 50, 0, 50, getHeight());
        }

        g.dispose();
        final Graphics graphics = super.getGraphics();
        graphics.drawImage(clientBuffer, 0, 0, null);

        try {
            Thread.sleep(15);
        } catch (final InterruptedException ignored) { }
        return gameBuffer.getGraphics();
    }

    public int getColorAt(final int x, final int y) {
        return (x < 0 || y < 0 || x >= gameBuffer.getWidth() || y >= gameBuffer.getHeight()) ? -1 : gameBuffer.getRGB(x, y);
    }

}