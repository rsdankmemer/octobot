package org.octobot.bot.internal;

/**
 * Rank
 *
 * @author Pat-ji
 */
public enum Rank {
    MEMBER(1, 1),
    SCRIPTER(3, 2),
    VIP(5, 4),
    SPONSOR(100, 8),
    STAFF(100, 16);

    public final int maxClients, mask;

    private Rank(final int maxClients, final int mask) {
        this.maxClients = maxClients;
        this.mask = mask;
    }

}