package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;
import org.octobot.script.methods.Tabs;
import org.octobot.script.wrappers.Item;

import java.awt.*;

/**
 * ItemRenderEvent
 *
 * @author Pat-ji
 */
public class ItemRenderEvent extends RenderEvent {

    public static final Color backgroundColor = new Color(96, 96, 150, 255);

    public ItemRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        if (context().game.isLoggedIn()) {
            g.setColor(Color.WHITE);
            if (context().bank.isOpen()) {
                for (final Item item : context().bank.getItems()) {
                    if (context().bank.isVisible(item)) {
                        drawItemId(g, item.getCentralPoint(), item.getId());
                    }
                }
            } else if (context().shop.isOpen()) {
                for (final Item item : context().shop.getItems()) {
                    drawItemId(g, item.getCentralPoint(), item.getId());
                }
            } else if (context().depositBox.isOpen()) {
                for (final Item item : context().depositBox.getItems()) {
                    drawItemId(g, item.getCentralPoint(), item.getId());
                }
            }

            if (context().tabs.isOpen(Tabs.Tab.INVENTORY)) {
                final Item[] items = context().inventory.getItems();
                if (items != null) {
                    for (final Item item : items) {
                        drawItemId(g, item.getCentralPoint(), item.getId());
                    }
                }
            } else if (context().tabs.isOpen(Tabs.Tab.EQUIPMENT)) {
                for (final Item item : context().equipment.getItems()) {
                    drawItemId(g, item.getCentralPoint(), item.getId());
                }
            }

            if (context().trading.isInTrade()) {
                Item[] items = context().trading.getOurOffer();
                if (items != null)
                    for (final Item item : items) {
                        drawItemId(g, item.getCentralPoint(), item.getId());
                    }

                items = context().trading.getTheirOffer();
                if (items != null)
                    for (final Item item : items) {
                        drawItemId(g, item.getCentralPoint(), item.getId());
                    }
            }
        }
    }

    private void drawItemId(final Graphics g, final Point centerPoint, final int id) {
        FontMetrics metrics = g.getFontMetrics();
        String text = String.valueOf(id);

        int width = metrics.stringWidth(text);
        int x = centerPoint.x - (width / 2);
        int y = centerPoint.y + 6;

        g.setColor(backgroundColor);
        g.fillRect(x, y - 12, width, 12);
        g.setColor(Color.black);
        g.drawString(text, x + 1, y + 1);
        g.setColor(Color.white);
        g.drawString(text, x, y);
    }

}
