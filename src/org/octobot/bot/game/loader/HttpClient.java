package org.octobot.bot.game.loader;

import org.octobot.bot.Environment;
import org.octobot.bot.handler.TextHandler;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpClient
 *
 * @author Pat-ji
 */
public class HttpClient {
    private static String httpUserAgent;

    private static String getHttpUserAgent() {
        if (httpUserAgent == null)
            httpUserAgent = getDefaultHttpUserAgent();

        return httpUserAgent;
    }

    private static String getDefaultHttpUserAgent() {
        final boolean x64 = System.getProperty("sun.arch.data.model").equals("64");
        final String os;
        switch (Environment.SYSTEM) {
            case MAC:
                os = "Macintosh; Intel Mac OS X 10_6_6";
                break;
            case LINUX:
                os = "X11; Linux " + (x64 ? "x86_64" : "i686");
                break;
            default:
                os = "Windows NT 6.1" + (x64 ? "; WOW64" : "");
                break;
        }

        final StringBuilder builder = new StringBuilder(125);
        builder.append("Mozilla/5.0 (").append(os).append(")");
        builder.append(" AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.68 Safari/534.24");
        return builder.toString();
    }

    public static InputStream getHttpsInputStream(final URL url) {
        try {
            // TODO: fix this cluster
            final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            final String encodedAuthorization = new BASE64Encoder().encode(TextHandler.decode("Ns0DgIy6lIdfGV00GYFtSw==").getBytes());
            connection.addRequestProperty(TextHandler.decode("+0pBct+clg3ZTrKIWfTodA=="), TextHandler.decode("0qTA12NQbvs=") + encodedAuthorization);
            connection.addRequestProperty("User-Agent", getHttpUserAgent());
            return connection.getInputStream();
        } catch (final Exception ignored) { }

        return null;
    }

    private static HttpURLConnection getHttpConnection(final URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connection.addRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        connection.addRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
        connection.addRequestProperty("Host", url.getHost());
        connection.addRequestProperty("Expires", "0");
        connection.addRequestProperty("Pragma", "no-cache");
        connection.addRequestProperty("Cache-Control", "no-cache");
        connection.addRequestProperty("User-Agent", getHttpUserAgent());
        connection.setConnectTimeout(10000);
        return connection;
    }

    public static HttpURLConnection getCachedConnection(final URL url) throws IOException {
        final HttpURLConnection connection = getHttpConnection(url);
        connection.setUseCaches(true);
        return connection;
    }

}
