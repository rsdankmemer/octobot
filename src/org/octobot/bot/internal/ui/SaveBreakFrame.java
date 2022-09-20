package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.GameDefinition;
import org.octobot.bot.internal.BreakProfile;
import org.octobot.script.event.BreakEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * SaveBreakFrame
 *
 * @author Pat-ji
 */
public class SaveBreakFrame extends JFrame {

    public SaveBreakFrame(final GameDefinition definition) {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(190, 150);
        setAlwaysOnTop(true);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JLabel text = new JLabel("Enter name for break profile");
        text.setBounds(22, 10, 400, 24);
        add(text);

        final JTextField nameField = new JTextField();
        nameField.setBounds(12, 42, 160, 24);
        add(nameField);

        final JButton doneButten = new JButton("Done");
        doneButten.setBounds(40, 75, 100, 24);
        doneButten.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (nameField.getText().isEmpty()) return;

                for (final BreakProfile profile : BreakProfile.BREAK_PROFILES)
                    if (profile.name.equals(nameField.getText())) {
                        System.out.println("This name is already in use. Please choose another one.");
                        return;
                    }

                final BreakProfile profile = new BreakProfile(nameField.getText());
                for (final BreakEvent event : definition.breakHandler.breakEvents)
                    profile.events.add(event);

                BreakProfile.BREAK_PROFILES.add(profile);
                BreakProfile.save();
                dispose();
            }
        });
        add(doneButten);
    }

}
