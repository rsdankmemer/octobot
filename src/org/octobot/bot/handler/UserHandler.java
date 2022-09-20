package org.octobot.bot.handler;

import org.octobot.Application;
import org.octobot.bot.Environment;
import org.octobot.bot.game.loader.HttpClient;
import org.octobot.bot.internal.Rank;
import org.octobot.bot.internal.User;
import org.octobot.bot.internal.ui.MainFrame;
import org.octobot.bot.internal.ui.UserFrame;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

/**
 * UserHandler
 *
 * @author Pat-ji
 */
public class UserHandler {

    public static boolean loadUserData(final User user) {
        try {
            final String name = TextHandler.decode(user.name);
            final String pass = TextHandler.decode(user.password);
            if (name == null || name.isEmpty() || pass == null || pass.isEmpty()) return false;

            final URL url = new URL("https://octobot.org/panel/bot/verifyuser.php?u="
                    + name.replace(" ", "%20")
                    + "&p="
                    + URLEncoder.encode(pass, "UTF-8"));

            InputStreamReader inputStreamReader;
            try {
                inputStreamReader = new InputStreamReader(HttpClient.getHttpsInputStream(url));
            } catch (final Exception e) {
                System.out.println("[UserHandler] - Failed to connect to login server");
                return false;
            }


            final BufferedReader reader = new BufferedReader(inputStreamReader);

            String read;
            while ((read = reader.readLine()) != null) {
                String output = read.trim().replace(" ", "");
                if (!output.isEmpty()) {
                    String errorPrefix = "#denied#";
                    String successPrefix = "#success#";
                    if (output.contains(errorPrefix)) {
                        output = reader.readLine();
                        final int reason = Integer.parseInt(output.substring(output.length() - 1));
                        switch (reason) {
                            case 1:
                                System.out.println("[UserHandler] - Your username or password is incorrect.");
                                break;
                            case 2:
                                System.out.println("[UserHandler] - You've been banned for violating the rules. Check the forum.");
                                break;
                            case 3:
                                System.out.println("[UserHandler] - You've made too many login attempts recently. Try again later.");
                                break;
                            case 4:
                                System.out.println("[UserHandler] - The login server is down for maintenance. We apologize.");
                                break;
                            case 5:
                                System.out.println("[UserHandler] - You must verify your email address before using the bot.");
                                break;
                            case 6:
                                System.out.println("[UserHandler] - Your account does not have access to the bot. Contact an admin.");
                                break;
                            default:
                                System.out.println("[UserHandler] - An unexpected error (" + reason + ") has occurred.");
                                break;
                        }

                        inputStreamReader.close();
                        reader.close();
                        return false;
                    } else if (output.contains(successPrefix)) {
//                        output = reader.readLine();
//                        System.out.println("output:" + output);
//                        final int primary = Integer.parseInt(output);
//                        System.out.println(primary);
//                        checkGroup(user, -1, primary);
//
//                        output = reader.readLine();
//                        final String[] secondary = output.split(",");
//                        final String index = TextHandler.decode("Idpyw2CXkeXUEWd4VI784w==");
//                        for (String group : secondary) {
//                            if (group.contains(index))
//                                group = group.substring(group.indexOf(index) + 15);
//
//                            if (!group.isEmpty())
//                                checkGroup(user, primary, Integer.parseInt(group));
//                        }
//
                        user.rank += Rank.SCRIPTER.mask;
                        inputStreamReader.close();
                        reader.close();
                        return true;
//                        return user.rank != 0;
                    } else {
                        System.out.println("[UserHandler] - The login server gave an unexpected response.");
                    }
                }
            }
        } catch (final Exception e) {
            System.out.println("[UserHandler] - Failed to get data from the login server.");
        }

        return false;
    }

    private static boolean checkGroup(final User user, final int primary, final int group) {
        if (primary == group) return true;

        switch (group) {
            case 3:
            case 19:
            case 13:
                if (!user.isMember())
                    user.rank += Rank.MEMBER.mask;
                break;
            case 10:
            case 15:
                if (!user.isVip())
                    user.rank += Rank.VIP.mask;
                break;
            case 11:
                if (!user.isSponsor())
                    user.rank += Rank.SPONSOR.mask;
                break;
            case 22:
            case 21:
            case 8:
            case 9:
            case 17:
                if (!user.isScripter())
                    user.rank += Rank.SCRIPTER.mask;

                break;
            case 4:
            case 12:
                if (!user.isStaff())
                    user.rank += Rank.STAFF.mask;
                break;
            default:
                System.exit(33);
                break;
        }

        return true;
    }

    public static boolean load() {
        if (Environment.offline) {
            User.user = User.OFFLINE_USER;
            Application.username = "Offline";
            Application.rank = Rank.MEMBER.mask;
            System.out.println("[UserHandler] - You are logged in with an offline account.");
            return true;
        }

        try {
            final String folder = Environment.getDataDirectory();
            if (!Environment.checkFolder(folder)) return false;

            final String dir = Environment.getDataDirectory() + File.separator + "user.dat";
            if (!Environment.checkFile(dir)) return false;

            final BufferedReader reader = new BufferedReader(new FileReader(dir));
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] split = line.split("\\|");
                if (split.length == 2) {
                    final User user = new User(split[0], split[1]);
                    if (loadUserData(user)) {
                        User.user = user;
                        Application.username = TextHandler.decode(user.name);
                        Application.rank = user.rank;

                        UserFrame.instance.dispose();
                        MainFrame.instance.setTitle(Application.TITLE + " - " + TextHandler.decode(user.name));
                        System.out.println("[UserHandler] - " + TextHandler.decode(user.name) + " logged in.");

                        return true;
                    }

                    break;
                }
            }

            reader.close();
        } catch (final Exception e) {
            System.out.println("[UserHandler] - Failed to load the user data, " + e.getMessage());
        }

        return false;
    }

    public static void save() {
        try {
            final String dir = Environment.getDataDirectory() + File.separator + "user.dat";
            final BufferedWriter out = new BufferedWriter(new FileWriter(dir));
            out.write(User.user.name + "|" + User.user.password);
            out.newLine();
            out.close();
        } catch (final Exception e) {
            System.out.println("[UserHandler] - Failed to save the user data, " + e.getMessage());
        }
    }

}
