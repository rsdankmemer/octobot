package org.octobot.bot.handler;

import org.octobot.bot.Environment;
import org.octobot.bot.game.loader.Crawler;

import java.io.*;

/**
 * OptionsHandler
 *
 * @author Pat-ji
 */
public class OptionsHandler {

    public static void load(final String... args) {
        try {
            String dir = Environment.getDataDirectory();
            if (!Environment.checkFolder(dir)) return;

            dir += File.separator + "option.dat";
            if (!Environment.checkFile(dir)) return;

            final BufferedReader out = new BufferedReader(new FileReader(dir));
            String line;
            o: while ((line = out.readLine()) != null) {
                if (line.startsWith("[world]>")) {
                    for (final String arg : args)
                        if (arg.contains("world")) continue o;

                    Crawler.world = Integer.parseInt(line.substring(8));
                } else if (line.startsWith("[offline]>")) {
                    for (final String arg : args)
                        if (arg.contains("offline")) continue o;

                    if (!Environment.offline)
                        Environment.offline = Boolean.parseBoolean(line.substring(10));
                }
            }

            out.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(Environment.getDataDirectory() + File.separator + "option.dat"));

            /*default world*/
            out.newLine();
            if (Crawler.world > 300) {
                Crawler.world -= 300;
            } else if (Crawler.world < 1) {
                Crawler.world = 1;
            }

            out.write("[world]>" + Crawler.world);

            /*offline mode*/
            out.newLine();
            out.write("[offline]>" + Environment.offline);

            out.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
