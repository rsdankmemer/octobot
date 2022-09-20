package org.octobot.bot.internal.ui;

import org.octobot.Application;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * VersionFrame
 *
 * @author Pat-ji
 */
public class VersionFrame extends JFrame {
    public static VersionFrame instance;

    private static double latestVersion;

    private final JLabel versionLabel;

    public VersionFrame() {
        setTitle(Application.TITLE);
        setLayout(null);
        setResizable(false);
        setSize(450, 130);
        setLocationRelativeTo(MainFrame.instance);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        versionLabel = new JLabel("You are running OctoBot v" + Application.VERSION + ". The newest version is -1.");
        versionLabel.setBounds(45, 40, 500, 20);
        add(versionLabel);

        new UpdateChecker().start();

        instance = this;
    }

    /**
     * UpdateChecker
     *
     * @author Pat-ji
     */
    private final class UpdateChecker extends Thread implements Runnable {

        @Override
        public void run() {
            try {
                final URL url = new URL("http://www.novakscripts.net/bot/version.txt");

                InputStreamReader inputStreamReader;
                try {
                    URLConnection urlConnection = url.openConnection();
                    inputStreamReader = new InputStreamReader(urlConnection.getInputStream());//HttpClient.getHttpsInputStream(url));
                } catch (final Exception e) {
                    System.out.println("[UpdateChecker] - Failed to connect to the update server");
                    latestVersion = Application.VERSION;
                    return;
                }

                String read;
                final BufferedReader reader = new BufferedReader(inputStreamReader);
                final String prefix = "<version>";
                final String suffix = "</version>";
                while ((read = reader.readLine()) != null) {
                    if (read.contains(prefix)) {
                        latestVersion = Double.parseDouble(read.substring(read.indexOf(prefix) + prefix.length(), read.indexOf(suffix)));
                        break;
                    }
                }

                reader.close();

                if (latestVersion > Application.VERSION) {
                    System.out.println("[UpdateChecker] - A new version of OctoBot is available. Be sure to check it out.");
                } else {
                    System.out.println("[UpdateChecker] - You have the latest version of OctoBot!");
                }
                versionLabel.setText("You are running OctoBot v" + Application.VERSION + ", the newest version is " + latestVersion + ".");
                repaint();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

    }

}
