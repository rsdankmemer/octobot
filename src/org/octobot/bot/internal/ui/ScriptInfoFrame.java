package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.game.script.ScriptManifest;

import javax.swing.*;

/**
 * ScriptInfoFrame
 *
 * @author Pat-ji
 */
public class ScriptInfoFrame extends JFrame {

    public ScriptInfoFrame(final ScriptManifest manifest) {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(450, 130);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JLabel titleLabel = new JLabel("You are currently running " + manifest.name() + " by " + manifest.authors());
        titleLabel.setBounds(45, 30, 500, 20);
        add(titleLabel);

        final JLabel descriptionLabel = new JLabel("   - " + manifest.description());
        descriptionLabel.setBounds(45, 50, 500, 20);
        add(descriptionLabel);
    }

}
