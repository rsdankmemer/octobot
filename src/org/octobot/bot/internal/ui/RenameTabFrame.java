package org.octobot.bot.internal.ui;

import org.octobot.Application;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * RenameTabFrame
 *
 * @author Pat-ji
 */
public class RenameTabFrame extends JFrame {

    public RenameTabFrame(final JLabel label) {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(260, 150);
        setAlwaysOnTop(true);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final JLabel text = new JLabel("Enter the name for tab " + label.getText());
        text.setBounds(22, 10, 400, 24);
        add(text);

        final JTextField nameField = new JTextField();
        nameField.setBounds(22, 42, 200, 24);
        add(nameField);

        final JButton doneButten = new JButton("Done");
        doneButten.setBounds(70, 75, 100, 24);
        doneButten.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!nameField.getText().isEmpty())
                    label.setText(nameField.getText());

                dispose();
            }
        });
        add(doneButten);
    }

}
