package org.octobot.bot.internal;

import org.octobot.bot.Environment;
import org.octobot.bot.handler.TextHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccountHandler
 *
 * @author Pat-ji
 */
public class AccountStore {
    public static final Map<String, Account> accounts;

    static {
        accounts = new HashMap<String, Account>();
    }

    public static void load() {
        try {
            final String dir = Environment.getDataDirectory() + File.separator + "accounts.dat";
            if (!Environment.checkFile(dir)) return;

            final BufferedReader out = new BufferedReader(new FileReader(dir));
            String line;
            while ((line = out.readLine()) != null) {
                final String[] split = line.split("\\|");
                if (accounts.get(split[0]) == null) {
                    final String name = split[0];
                    final String pass = split[1];
                    Account account;
                    if (split.length == 4) {
                        final String pin = split[2];
                        final String reward = TextHandler.decode(split[3]);
                        account = new Account(name, pass, pin);
                        account.reward = Reward.get(reward);
                    } else {
                        final String reward = TextHandler.decode(split[2]);
                        account = new Account(name, pass, "");
                        account.reward = Reward.get(reward);
                        if (account.reward == null)
                            account.reward = Reward.AGILITY;
                    }

                    accounts.put(name, account);
                }
            }

            out.close();
        } catch (final Exception ignored) { }
    }

    public static void save() {
        try {
            final String dir = Environment.getDataDirectory() + File.separator + "accounts.dat";
            final BufferedReader reader = new BufferedReader(new FileReader(dir));
            final List<String> names = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                names.add(line);
            }

            reader.close();

            final BufferedWriter writer = new BufferedWriter(new FileWriter(dir));
            for (final String name : accounts.keySet()) {
                if (names.contains(name)) continue;

                final Account account = accounts.get(name);
                if (account != null) {
                    writer.write(name + "|" + account.password + "|" + account.pin + "|" + TextHandler.encode(account.reward.getName()));
                    writer.newLine();
                }
            }

            writer.close();
        } catch (final Exception ignored) { }
    }

}
