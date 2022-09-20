package org.octobot.bot.internal;

import org.octobot.script.event.BreakEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * BreakProfile
 *
 * @author Pat-ji
 */
public class BreakProfile {
    public static final List<BreakProfile> BREAK_PROFILES;

    public final String name;
    public final List<BreakEvent> events;

    public BreakProfile(final String name) {
        this.name = name;

        events = new ArrayList<BreakEvent>();
    }

    static {
        BREAK_PROFILES = new ArrayList<BreakProfile>();
    }

    public static void load() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/OctoBot/breaks.dat"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("~")) {
                    final BreakProfile profile = new BreakProfile(line.substring(8));
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("%")) break;

                        if (!line.isEmpty()) {
                            final String[] info = line.split("\\|");
                            profile.events.add(new BreakEvent(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[0]), Integer.parseInt(info[1])));
                        }
                    }

                    BREAK_PROFILES.add(profile);
                }
            }

            reader.close();
        } catch (final Exception ignored) { }
    }

    public static void save() {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "/OctoBot/breaks.dat"));
            for (final BreakProfile profile : BREAK_PROFILES) {
                out.write("~profile" + profile.name);
                out.newLine();

                for (final BreakEvent event : profile.events) {
                    out.write(event.getScriptTime() + "|" + event.getScriptDeviation() + "|" + event.getBreakTime() + "|" + event.getScriptDeviation());
                    out.newLine();
                }
                out.write("%");
                out.newLine();
            }

            out.close();
        } catch (final Exception ignored) { }
    }

}
