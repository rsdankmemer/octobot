package org.octobot.bot.internal.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * TextureLoader
 *
 * @author Pat-ji
 */
public class TextureLoader {

    public static BufferedImage load(final String name) {
        try {
            return ImageIO.read(TextureLoader.class.getResource("/textures/" + name + ".png"));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
