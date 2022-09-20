package org.octobot.bot.internal.ui.component;

import org.octobot.Application;
import org.octobot.bot.GameDefinition;
import org.octobot.bot.internal.ui.MainFrame;
import org.octobot.bot.internal.ui.ScriptSelectorFrame;
import org.octobot.bot.internal.ui.TextureLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * UtilityBar
 *
 * @author Pat-ji
 */
public class UtilityBar extends JPanel {
    public static UtilityBar instance;

    private final Textures textures;

    public volatile JLabel statusLabel;

    public UtilityBar() {
        textures = new Textures();

        setLayout(null);
        setBounds(0, 552, 765, 25);

        add(new Button(this, ButtonDefinition.START_PAUSE));
        add(new Button(this, ButtonDefinition.STOP));
        add(new Button(this, ButtonDefinition.INPUT));
        add(new Button(this, ButtonDefinition.CONSOLE));

        statusLabel = new JLabel("Welcome to " + Application.TITLE);
        statusLabel.setFont(new Font("Cambria Math", 0, 13));
        statusLabel.setBounds(112, 2, 500, 25);
        add(statusLabel);

        instance = this;
    }

    public void updateTextures() {
        for (final Component menu : getComponents())
            if (menu instanceof Button) {
                final Button button = (Button) menu;
                if (button.definition.equals(ButtonDefinition.START_PAUSE)) {
                    final GameDefinition definition = ContentPanel.instance.definition;
                    if (definition != null)
                        button.down = definition.scriptHandler.getScript() != null && !definition.scriptHandler.paused;
                } else if (button.definition.equals(ButtonDefinition.INPUT)) {
                    final GameDefinition definition = ContentPanel.instance.definition;
                    if (definition != null)
                        button.down = definition.disableInput;
                } else if (button.definition.equals(ButtonDefinition.CONSOLE)) {
                    button.down = MainFrame.instance.consoleDisplayed;
                }
            }

        repaint();
    }

    /**
     * Textures
     *
     * @author Pat-ji
     */
    private final class Textures {
        public final Image mouse, mouseHover;
        public final Image console, consoleHover;
        public final Image start, pause, stop;

        public Textures() {
            mouse = TextureLoader.load("mouse");
            mouseHover = TextureLoader.load("mouse_hover");
            console = TextureLoader.load("console");
            consoleHover = TextureLoader.load("console_hover");
            start = TextureLoader.load("start");
            pause = TextureLoader.load("pause");
            stop = TextureLoader.load("stop");
        }

    }

    /**
     * ButtonDefinition
     *
     * @author Pat-ji
     */
    private enum ButtonDefinition {
        START_PAUSE, STOP, INPUT, CONSOLE

    }

    /**
     * Button
     *
     * @author Pat-ji
     */
    private final class Button extends JPanel {
        private final ButtonDefinition definition;

        private boolean hovered, down;

        public Button(final JPanel container, final ButtonDefinition definition) {
            super(new BorderLayout());

            this.definition = definition;

            setBounds(5 + container.getComponentCount() * 25, 2, 24, 24);
            setFocusable(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent e) {
                    if (definition.equals(ButtonDefinition.START_PAUSE)) {
                        final GameDefinition definition = ContentPanel.instance.definition;
                        if (definition != null)
                            if (down) {
                                definition.scriptHandler.paused = true;
                                definition.disableInput = false;
                            } else {
                                definition.scriptHandler.paused = false;
                                if (definition.scriptHandler.getScript() == null) {
                                    new ScriptSelectorFrame(definition);
                                } else {
                                    definition.disableInput = true;
                                }
                            }
                    } else if (definition.equals(ButtonDefinition.STOP)) {
                        final GameDefinition definition = ContentPanel.instance.definition;
                        if (definition != null)
                            definition.scriptHandler.stopScript();
                    } else if (definition.equals(ButtonDefinition.INPUT)) {
                        final GameDefinition definition = ContentPanel.instance.definition;
                        if (definition != null)
                            definition.disableInput = !definition.disableInput;
                    } else {
                        MainFrame.instance.displayConsole(!MainFrame.instance.consoleDisplayed);
                    }

                    updateTextures();
                    repaint();
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

            if (hovered) {
                g.setColor(Colors.DEEP_WHITE);
                g.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
                g.setColor(Colors.DEEP_GRAY);
                g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 1, 4, 4);
            }

            if (down) {
                g.drawImage(definition.equals(ButtonDefinition.START_PAUSE) ? textures.pause :
                        definition.equals(ButtonDefinition.INPUT) ? textures.mouseHover : textures.consoleHover, 3, 3, null);
            } else {
                g.drawImage(definition.equals(ButtonDefinition.START_PAUSE) ? textures.start :
                        definition.equals(ButtonDefinition.STOP) ? textures.stop :
                        definition.equals(ButtonDefinition.INPUT) ? textures.mouse : textures.console, 3, 3, null);
            }
        }

    }

}
