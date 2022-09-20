package org.octobot.bot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Controller
 *
 * @author Pat-ji
 */
public class Controller {
    public static boolean isServer;

    public static boolean registerInstance() {
        try {
            final ServerSocket socket = new ServerSocket(1898, 10, InetAddress.getLocalHost());
            final Thread instanceListenerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean socketClosed = false;
                    while (!socketClosed) {
                        if (socket.isClosed())
                            socketClosed = true;

                        try {
                            Thread.sleep(1000);
                        } catch (final Exception ignored) { }
                    }
                }
            });

            instanceListenerThread.start();
            isServer = true;
        } catch (final IOException e) {
            return false;
        }

        return true;
    }

}
