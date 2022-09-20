package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.Environment;
import org.octobot.bot.internal.ui.component.WebPanel;
import org.octobot.script.ScriptContext;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joseph on 12/19/2014.
 */
public class WebExplorerFrame extends JFrame {

    private Point mapAbsolutePosition;
    private double mapZoom;

    private WebPanel panel;

    public WebExplorerFrame(final ScriptContext context) {
        setTitle(Application.TITLE + " - Web Explorer");
        panel = new WebPanel();
        add(panel);
        setSize(725, 475);
        setLocationRelativeTo(MainFrame.instance);
    }

}
