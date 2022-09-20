package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.internal.ui.component.*;

import javax.swing.*;

/**
 * MainFrame
 *
 * @author Pat-ji
 */
public class MainFrame extends JFrame {
    public static MainFrame instance;

    public boolean consoleDisplayed;

    public MainFrame() {
        setTitle(Application.TITLE);
        setSize(771, 605);
        setLayout(null);
        setResizable(false);
        setIconImage(TextureLoader.load("menu_bar_icon"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        add(new MenuBar());
        add(new TabBar());
        add(new ContentPanel());
        add(UtilityBar.instance);

        instance = this;
    }

    public void displayConsole(final boolean display) {
        if (consoleDisplayed) {
            remove(ConsolePanel.instance);
            setSize(getWidth(), getHeight() - 105);
        } else {
            add(ConsolePanel.instance);
            setSize(getWidth(), getHeight() + 105);
        }

        consoleDisplayed = display;
    }

}
