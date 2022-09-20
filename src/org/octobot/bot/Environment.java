package org.octobot.bot;

import org.octobot.bot.internal.Proxy;

import java.io.File;

/**
 * Environment
 *
 * @author Pat-ji
 */
public class Environment {
    public static final OS SYSTEM;

    public static boolean offline;
    public static Proxy proxy;

    static {
        final String system = System.getProperty("os.name").toUpperCase();
        if (system.contains("WIN")) {
            SYSTEM = OS.WINDOWS;
        } else if (system.contains("MAC")) {
            SYSTEM = OS.MAC;
        } else {
            SYSTEM = OS.LINUX;
        }
    }

    /**
     * OS
     *
     * @author Pat-ji
     */
    public static enum OS {
        WINDOWS, MAC, LINUX

    }

    public static String getSystemDirectory() {
        switch (SYSTEM) {
            case WINDOWS:
                return System.getProperty("user.home");
            case MAC:
                return System.getProperty("user.home") + "/Library/Application Support";
            case LINUX:
                return System.getProperty("user.home");
            default:
                return System.getProperty("user.dir");
        }
    }

    public static String getBotDirectory() {
        return getSystemDirectory() + File.separator + "OctoBot";
    }

    public static String getScriptsDirectory() {
        return getBotDirectory() + File.separator + "Scripts";
    }

    public static String getLibraryDirectory() {
        return getBotDirectory() + File.separator + "Library";
    }

    public static String getDataDirectory() {
        return getBotDirectory() + File.separator + "Data";
    }

    public static String getMapsDirectory() {
        return getDataDirectory() + File.separator + "Maps";
    }

    public static String getScreenshotsDirectory() {
        return getBotDirectory() + File.separator + "Screenshots";
    }

    public static boolean checkFolder(final String folder) {
        final File file = new File(folder);
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("[Environment] - Failed to make OctoBot directories. Please restart.");

        return file.exists();
    }

    public static boolean checkFile(final String folder) {
        final File file = new File(folder);
        try {
            if (!file.exists() && !file.createNewFile())
                throw new RuntimeException("[Environment] - Failed to make OctoBot files. Please restart.");
        } catch (final Exception ignored) { }

        return file.exists();
    }

}
