package org.octobot.bot.internal;

/**
 * Account
 *
 * @author Pat-ji
 */
public class Account {
    public String name, password, pin;
    public Reward reward;

    public Account(final String name, final String password, final String pin) {
        this.name = name;
        this.password = password;
        this.pin = pin;

        reward = Reward.AGILITY;
    }

    public Account(final String name, final String password) {
        this(name, password, "");
    }

}
