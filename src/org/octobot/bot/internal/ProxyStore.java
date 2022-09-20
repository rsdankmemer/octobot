package org.octobot.bot.internal;

import org.octobot.bot.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProxyStore
 *
 * @author Pat-ji
 */
public class ProxyStore {
    public static final Map<String, Proxy> proxies;

    static {
        proxies = new HashMap<String, Proxy>();
    }

    public static void load() {
        try {
            final String folder = Environment.getSystemDirectory() + "/OctoBot";
            if (!Environment.checkFolder(folder)) return;

            final String dir = Environment.getSystemDirectory() + "/OctoBot/proxies.dat";
            if (!Environment.checkFile(dir)) return;

            final BufferedReader out = new BufferedReader(new FileReader(dir));
            String line;
            while ((line = out.readLine()) != null) {
                final String[] split = line.split("\\|");
                if (proxies.get(split[0]) == null) {
                    final String name = split[0];
                    final Proxy proxy = new Proxy(name, split[1], split[2]);
                    if (split.length == 5) {
                        proxy.username = split[3];
                        proxy.password = split[4];
                    } else if (split.length == 4) {
                        proxy.username = split[3];
                    }

                    proxies.put(name, proxy);
                }
            }

            out.close();
        } catch (final Exception ignored) { }
    }

    public static void save() {
        try {
            final String dir = Environment.getSystemDirectory() + "/OctoBot/proxies.dat";
            final BufferedReader reader = new BufferedReader(new FileReader(dir));
            final List<String> names = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                names.add(line);
            }

            reader.close();

            final BufferedWriter writer = new BufferedWriter(new FileWriter(dir));
            for (final String name : proxies.keySet()) {
                if (names.contains(name)) continue;

                final Proxy proxy = proxies.get(name);
                if (proxy != null) {
                    if (proxy.username != null) {
                        if (proxy.password != null) {
                            writer.write(name + "|" + proxy.host + "|" + proxy.port + "|" + proxy.username + "|" + proxy.password);
                        } else {
                            writer.write(name + "|" + proxy.host + "|" + proxy.port + "|" + proxy.username);
                        }
                    } else {
                        writer.write(name + "|" + proxy.host + "|" + proxy.port);
                    }

                    writer.newLine();
                }
            }

            writer.close();
        } catch (final Exception ignored) { }
    }

}
