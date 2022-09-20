package org.octobot.bot.game.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarInputStream;

/**
 * JarStream
 *
 * @author Pat-ji
 */
public class JarStream extends JarInputStream {

    public JarStream(final InputStream in) throws IOException {
        super(in);
    }

    public byte[] readStream() throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final byte[] buffer = new byte[2048];
        while (available() > 0) {
            final int read = read(buffer, 0, buffer.length);
            if (read >= 0)
                out.write(buffer, 0, read);
        }

        return out.toByteArray();
    }

}
