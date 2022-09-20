package org.octobot.bot.internal.ui;

import org.octobot.Application;
import org.octobot.bot.handler.TextHandler;
import org.octobot.bot.handler.UserHandler;
import org.octobot.bot.internal.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * UserFrame
 *
 * @author Pat-ji
 */
public class UserFrame extends JFrame {
    public static UserFrame instance;

    private final Image logo;

    public UserFrame() {
        logo = TextureLoader.load("logo_small");

        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(388, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel() {
            @Override
            public void paintComponent(final Graphics g) {
                g.drawImage(logo, 0, 0, null);
            }
        };
        panel.setBounds(55, 5, 300, 300);
        add(panel);

        final JLabel usernameLabel = new JLabel("Account Name", JLabel.CENTER);
        usernameLabel.setBounds(55, 90, 278, 24);
        add(usernameLabel);

        final JTextField username = new JTextField();
        username.setBounds(55, 114, 278, 24);
        username.setHorizontalAlignment(JTextField.CENTER);
        add(username);

        final JLabel passwordLabel = new JLabel("Password", JLabel.CENTER);
        passwordLabel.setBounds(55, 140, 278, 24);
        add(passwordLabel);

        final JTextField password = new JPasswordField();
        password.setBounds(55, 164, 278, 24);
        password.setHorizontalAlignment(JTextField.CENTER);
        add(password);

        JButton button = new JButton("Login");
        button.setBounds(55, 194, 100, 24);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final User user = new User(TextHandler.encode(username.getText()), TextHandler.encode(password.getText()));
                if (UserHandler.loadUserData(user)) {
                    startMainFrame(user);
                    UserHandler.save();
                    System.out.println("[UserFrame] - " + TextHandler.decode(user.name) + " logged in.");
                }
            }
        });
        add(button);

        button = new JButton("Offline");
        button.setBounds(233, 194, 100, 24);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                startMainFrame(User.OFFLINE_USER);
            }
        });
        button.setEnabled(true);
        add(button);

        instance = this;

        new SplashScreen().setVisible(true);
    }

    private void startMainFrame(final User user) {
        User.user = user;
        Application.username = TextHandler.decode(user.name);
        Application.rank = user.rank;

        MainFrame.instance.setTitle(Application.TITLE + " - " + TextHandler.decode(user.name));
        MainFrame.instance.setVisible(true);
        dispose();
    }

    /**
     * SplashScreen
     *
     * @author Pat-ji
     */
    private final class SplashScreen extends JFrame implements Runnable {

        public SplashScreen() {
            setSize(361, 92);
            setLayout(null);
            setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop(true);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            new Thread(this).start();
        }

        @Override
        public void paint(final Graphics g) {
            g.drawImage(logo, 0, 0, null);
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserHandler.load();
                }
            }).start();

            final long startTime = System.currentTimeMillis();
            while (User.user == null && (System.currentTimeMillis() - startTime) < 5000) {
                try {
                    Thread.sleep(100);
                } catch (final Exception ignored) {}
            }

            if (User.user == null) {
                instance.setVisible(true);
            } else {
                while (MainFrame.instance == null) {
                    try {
                        Thread.sleep(100);
                    } catch (final Exception ignored) { }
                }

                MainFrame.instance.setVisible(true);
                MainFrame.instance.requestFocus();
            }

            dispose();
        }

    }

}
