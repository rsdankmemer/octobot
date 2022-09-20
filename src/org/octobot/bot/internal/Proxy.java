package org.octobot.bot.internal;

/**
 * Proxy
 *
 * @author Pat-ji
 */
public class Proxy {
    public final String name;
    public String host, port, username, password;

    public Proxy(final String name, final String host, final String port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

}
