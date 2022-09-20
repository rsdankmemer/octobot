package org.octobot.bot.internal;

/**
 * User
 *
 * @author Pat-ji
 */
public class User {
    public static final User OFFLINE_USER;

    public static User user;

    static {
        OFFLINE_USER = new User("Offline", "offline");
        OFFLINE_USER.rank = Rank.MEMBER.mask;
    }

    public int rank;
    public final String name, password;

    public User(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public boolean isMember() {
        return (rank & Rank.MEMBER.mask) == Rank.MEMBER.mask;
    }

    public boolean isVip() {
        return (rank & Rank.VIP.mask) == Rank.VIP.mask;
    }

    public boolean isScripter() {
        return (rank & Rank.SCRIPTER.mask) == Rank.SCRIPTER.mask;
    }

    public boolean isSponsor() {
        return (rank & Rank.SPONSOR.mask) == Rank.SPONSOR.mask;
    }

    public boolean isStaff() {
        return (rank & Rank.STAFF.mask) == Rank.STAFF.mask;
    }

    public int getMaxClients() {
        if (isStaff()) {
            return Rank.STAFF.maxClients;
        } else if (isSponsor()) {
            return Rank.SPONSOR.maxClients;
        }  else if (isVip()) {
            return Rank.VIP.maxClients;
        } else if (isScripter()) {
            return Rank.SCRIPTER.maxClients;
        } else if (isMember()) {
            return Rank.MEMBER.maxClients;
        }

        return 0;
    }

}
