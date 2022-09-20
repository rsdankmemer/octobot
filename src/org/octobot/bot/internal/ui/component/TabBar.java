package org.octobot.bot.internal.ui.component;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.internal.User;
import org.octobot.bot.internal.ui.RenameTabFrame;
import org.octobot.bot.internal.ui.TextureLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * TabBar
 *
 * @author Pat-ji
 */
public class TabBar extends JPanel {
    public static TabBar instance;

    private final Textures textures;

    public int index;
    private long lastClickTime;

    public TabBar() {
        textures = new Textures();

        setLayout(null);
        setBounds(0, 25, 765, 25);

        add(new HomeButton());
        add(new AddButton(this));
        add(Box.createHorizontalGlue());

        index = -1;
        lastClickTime = System.currentTimeMillis();

        instance = this;
    }

    public void setIndex(final int index) {
        this.index = index;

        ContentPanel.instance.displayBot(index > -1 ? GameDefinition.definitions.get(index - 2) : null);
        MenuBar.instance.updateTextures();
        UtilityBar.instance.updateTextures();
        ContentPanel.instance.thread.interrupt();

        repaint();
    }

    public void removeBot(final int index) {
        remove(index);
        GameDefinition.definitions.get(index - 3).stop();

        for (int i = index; i < getComponentCount(); i++) {
            final Component component = getComponent(i);
            if (component != null) {
                component.setLocation(component.getX() - 115, component.getY());
                if (component instanceof BotButton) {
                    ((BotButton) component).idx--;
                    GameDefinition.definitions.get(index - 3).index--;
                    ((BotButton) component).updateText();
                }
            }
        }

        final Component add = getComponent(1);
        add.setLocation(add.getX() - 115, add.getY());

        setIndex(-1);
    }

    public void addBot() {
        if (getComponentCount() - 3 >= User.user.getMaxClients()) {
            System.out.println("[TabBar] - You have reached your max amount of clients.");
            return;
        }

        add(new BotButton(this));

        final Component add = getComponent(1);
        add.setLocation(add.getX() + 115, add.getY());

        repaint();
    }

    /**
     * Textures
     *
     * @author Pat-ji
     */
    private final class Textures {
        public final Image homeButton;
        public final Image close, closeHover;
        public final Image add, addHover;

        public Textures() {
            homeButton = TextureLoader.load("home_button");
            close = TextureLoader.load("close_button");
            closeHover = TextureLoader.load("close_button_hover");
            add = TextureLoader.load("add_button");
            addHover = TextureLoader.load("add_button_hover");
        }

    }

    /**
     * HomeButton
     *
     * @author Pat-ji
     */
    private final class HomeButton extends JPanel {
        private boolean hovered;

        public HomeButton() {
            super(new BorderLayout());

            setBounds(5, 0, 24, 22);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent e) {
                    setIndex(-1);
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (index == -1) {
                g.setColor(Colors.DEEP_WHITE);
                g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
                g.setColor(Colors.DEEP_GRAY);
                g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
            } else if (hovered) {
                g.setColor(Colors.LIGHT_WHITE);
                g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
                g.setColor(Colors.LIGHT_GRAY);
                g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
            }

            g.drawImage(textures.homeButton, 3, 3, null);
        }

    }

    /**
     * BotButton
     *
     * @author Pat-ji
     */
    private final class BotButton extends JPanel {
        private final JLabel nameLabel;

        public int idx;

        private boolean hovered;
        private boolean close;

        public BotButton(final JComponent parent) {
            super(new BorderLayout());

            idx = parent.getComponentCount() - 1;
            setBounds((idx - 1) * 115 - 82, 0, 110, 22);
            setBorder(new EmptyBorder(3, 6, 2, 3));

            nameLabel = new JLabel("OSRS Client " + (idx - 1));
            nameLabel.setBounds(8, 0, 85, 22);
            add(nameLabel);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent e) {
                    if (System.currentTimeMillis() - lastClickTime < 500)
                        new RenameTabFrame(nameLabel).setVisible(true);

                    lastClickTime = System.currentTimeMillis();
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                    if (hovered && e.getX() > 90) {
                        removeBot(idx + 1);
                    } else {
                        setIndex(idx);
                    }
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(final MouseEvent e) {
                    close = e.getX() > 90;
                    repaint();
                }
            });

            new GameDefinition(idx - 1);
        }

        public void updateText() {
            if (nameLabel.getText().startsWith("OSRS Client "))
                nameLabel.setText("OSRS Client " + (idx - 1));
        }

        @Override
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            final int color = idx == index ? 255 : hovered ? 230 : 215;
            g.setColor(new Color(color, color, color, 200));
            g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
            g.setColor(Colors.DEEP_GRAY);
            g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);

            g.drawImage(hovered && close ? textures.closeHover : textures.close, 90, 3, null);
        }

    }

    /**
     * AddButton
     *
     * @author Pat-ji
     */
    private final class AddButton extends JPanel {
        private boolean hovered;

        public AddButton(final JComponent parent) {
            super(new BorderLayout());

            setBounds((parent.getComponentCount() - 1) * 115 + 30, 0, 20, 20);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(final MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                    hovered = false;
                    repaint();
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                    addBot();
                }
            });
        }

        @Override
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);

            g.drawImage(hovered ? textures.addHover : textures.add, 2, 3, null);
        }

    }

}
