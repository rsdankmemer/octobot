package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.GameDefinition;
import org.octobot.bot.internal.BreakProfile;
import org.octobot.script.event.BreakEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * LoadBreakFrame
 *
 * @author Pat-ji
 */
public class LoadBreakFrame extends JFrame {

    public LoadBreakFrame(final GameDefinition definition, final DefaultListModel<String> model) {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(190, 150);
        setAlwaysOnTop(true);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JLabel text = new JLabel("Select break profile");
        text.setBounds(46, 10, 400, 24);
        add(text);

        final String[] breaks = new String[BreakProfile.BREAK_PROFILES.size()];
        int index = 0;
        for (final BreakProfile profile : BreakProfile.BREAK_PROFILES)
            breaks[index++] = profile.name;

        final JComboBox<String> comboBox = new JComboBox<String>(breaks);
        comboBox.setBounds(12, 42, 160, 24);
        add(comboBox);

        final JButton doneButten = new JButton("Done");
        doneButten.setBounds(40, 75, 100, 24);
        doneButten.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                definition.breakHandler.breakEvents.clear();
                model.clear();

                for (final BreakProfile profile : BreakProfile.BREAK_PROFILES)
                    if (profile.name.equals(comboBox.getSelectedItem())) {
                        for (final BreakEvent event : profile.events) {
                            definition.breakHandler.breakEvents.add(event);
                            model.addElement(event.toString());
                        }

                        break;
                    }
                dispose();
            }
        });
        add(doneButten);
    }

}
