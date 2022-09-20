package org.octobot;

import org.octobot.bot.Console;
import org.octobot.bot.Controller;
import org.octobot.bot.Environment;
import org.octobot.bot.game.loader.Crawler;
import org.octobot.bot.handler.OptionsHandler;
import org.octobot.bot.internal.AccountStore;
import org.octobot.bot.internal.BreakProfile;
import org.octobot.bot.internal.ProxyStore;
import org.octobot.bot.internal.ui.MainFrame;
import org.octobot.bot.internal.ui.UserFrame;
import org.octobot.bot.internal.ui.VersionFrame;
import org.octobot.bot.internal.ui.component.ConsolePanel;
import org.octobot.bot.internal.ui.component.UtilityBar;
import org.octobot.script.methods.Landscape;
import org.octobot.script.methods.Navigation;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.TimerTask;

/**
 * Created by Joseph on 11/2/2014.
 */
public class Application {
    public static final double VERSION = 1.00;
    public static final String TITLE = "Octobot v" + VERSION + "";

    public static String username;
    public static int rank;

    private static void boot(final String... args) {
        if (Controller.registerInstance()) {
            System.out.println("[Application] - Controller started.");
        } else {
            System.out.println("[Application] - Client started.");
        }

        Environment.checkFolder(Environment.getBotDirectory());
        Environment.checkFolder(Environment.getScriptsDirectory());
        Environment.checkFolder(Environment.getLibraryDirectory());
        Environment.checkFolder(Environment.getDataDirectory());

        System.setOut(new Console.OutStream());
        System.setErr(new Console.ErrStream());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    JFrame.setDefaultLookAndFeelDecorated(true);

//                    Properties props = new Properties();
//                    props.put("logoString", " ");
//                    HiFiLookAndFeel.setCurrentTheme(props);
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    new UtilityBar(); // load the instance
                    new ConsolePanel(); // load the instance

                    System.out.println("[Application] - Welcome to " + TITLE + ".");

                    new UserFrame(); // start the loading sequence
                    new MainFrame(); // load the instance
                    new VersionFrame(); // load the instance

                    OptionsHandler.load(args);
                    AccountStore.load();
                    ProxyStore.load();
                    BreakProfile.load();
                    Landscape.load();
                    Navigation.loadData();
                } catch (final Exception e) {
                    System.out.println("5");
                    e.printStackTrace();
                }
            }
        });

        new java.util.Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                System.gc();
            }
        }, 1000 * 60 * 10, 1000 * 60 * 10);
    }

    public static void main(final String[] args) throws Exception {
        boolean restarted = false;

        for (final String arg : args)
            if (arg.contains("-forked")) {
                restarted = true;
            } else if (arg.contains("-offline")) {
                Environment.offline = true;
            } else if (arg.contains("-world=")) {
                try {
                    Crawler.world = Integer.parseInt(arg.trim().substring(7));
                } catch (final Exception ignored) { }
            }

        if (restarted) {
            boot(args);
        } else {
            fork(args);
        }
    }

    private static void fork(final String... args) {
        System.out.println("FORKING");
        try {
            String location = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            location = URLDecoder.decode(location, "UTF-8").replaceAll("\\\\", "/");

            final String flags = "-noverify -Xmx256m -Xss2m -Dsun.java2d.noddraw=true -XX:CompileThreshold=1500 -Xincgc -XX:+UseConcMarkSweepGC -XX:+UseParNewGC";
            final char quote = '"', space = ' ';
            final StringBuilder parameters = new StringBuilder(64);

            boolean windows = false;
            switch (Environment.SYSTEM) {
                case WINDOWS:
                    windows = true;
                    parameters.append("javaw");
                    parameters.append(space);
                    parameters.append(flags);
                    break;
                case MAC:
                    parameters.append("java");
                    parameters.append(space);
                    parameters.append(flags);
                    parameters.append(space);
                    parameters.append("-Xdock:name=");
                    parameters.append(quote);
                    parameters.append("OctoBot");
                    parameters.append(quote);
                    parameters.append(space);
                    parameters.append("-Xdock:icon=");
                    parameters.append(quote);
                    parameters.append("textures/menu_bar_icon.png");
                    parameters.append(quote);
                    break;
                case LINUX:
                    parameters.append("java");
                    parameters.append(space);
                    parameters.append(flags);
                    break;
            }

            parameters.append(space);
            parameters.append("-classpath");
            parameters.append(space);
            parameters.append(quote);
            parameters.append(location);
            parameters.append(quote);
            parameters.append(space);
            parameters.append(Application.class.getCanonicalName());

            for (final String arg : args) {
                parameters.append(space);
                parameters.append(arg);
            }

            parameters.append(space);
            parameters.append("-forked");

            final Process process;
            if (windows) {
                process = Runtime.getRuntime().exec(parameters.toString());
            } else {
                process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", parameters.toString()});
            }

            final BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = out.readLine()) != null) {
                System.out.println(line);
            }

            out.close();
        } catch (final Exception ignored) { }
    }

}