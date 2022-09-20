package org.octobot.bot.internal.ui.component;

import org.octobot.bot.GameDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ContentPanel
 *
 * @author Pat-ji
 */
public class ContentPanel extends JPanel {
    public static ContentPanel instance;

    public GameDefinition definition;
    public Thread thread;

    private int clickX, clickY, mouseX, mouseY;

    public ContentPanel() {
        setBounds(0, 50, 765, 503);
        setLayout(null);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (thread.isAlive()) {
                    try {
                        if (TabBar.instance.index == -1) {
                            repaint();
                            Thread.sleep(20);
                        } else {
                            Thread.sleep(2000);
                        }
                    } catch (final Exception ignored) { }
                }
            }
        });
        thread.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(final MouseEvent e) {
                mouseX = -1;
                mouseY = -1;
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                clickX = e.getPoint().x;
                clickY = e.getPoint().y;
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        instance = this;
    }

    public void displayBot(final GameDefinition definition) {
        this.definition = definition;

        removeAll();
        if (definition != null)
            if (definition.applet != null) {
                definition.applet.setLayout(null);
                add(definition.applet);
            } else {
                final JPanel panel = new JPanel();
                panel.setBackground(Color.BLACK);
                panel.setSize(765, 503);
                add(panel);
            }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        if (TabBar.instance.index == -1) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            renderClients(g);
        }
    }

    private void renderClients(final Graphics g) {
        int width = 253;
        int height = 168;
        int row = 3;
        if (GameDefinition.definitions.size() < 5) {
            width = 380;
            height = 253;
            row = 2;
        }

        int bots = 0;
        for (final GameDefinition definition :  GameDefinition.definitions)
            if (definition != null && definition.canvas != null) {
                final int x = (bots % row) * width;
                final int y = (bots / row) * height;
                g.drawImage(definition.canvas.clientBuffer, x, y, width, height, this);
                g.setColor(Color.WHITE);
                g.drawString("Client " + definition.index, x + 10, y + 16);

                if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
                    g.setColor(new Color(15, 96, 172, 100));
                    g.fillRect(x, y, width, height);

                    if (clickX > x && clickX < x + width && clickY > y && clickY < y + height) {
                        clickX = clickY = -1;
                        TabBar.instance.setIndex(definition.index + 1);
                    }
                }

                bots++;
            }
    }

}
