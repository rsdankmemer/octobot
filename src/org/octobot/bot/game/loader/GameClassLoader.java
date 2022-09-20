package org.octobot.bot.game.loader;

import java.util.Map;

/**
 * GameClassLoader
 *
 * @author Pat-ji
 */
public class GameClassLoader extends ClassLoader {
    private final Crawler crawler;
    private final Map<String, byte[]> classes;

    public GameClassLoader(final Crawler crawler, final Map<String, byte[]> classes) {
        this.crawler = crawler;
        this.classes = classes;
    }

    @Override
    public final Class<?> loadClass(final String name) throws ClassNotFoundException {
        final String key = name.replaceAll("\\.", "/");
        if (classes.containsKey(key)) {
            final byte buffer[] = classes.get(key);

            final Class<?> loaded = super.findLoadedClass(key);
            if (loaded != null) {
                classes.remove(key);
                return loaded;
            }

            try {
                return defineClass(name, buffer, 0, buffer.length);
            } catch (final Error error) {
                crawler.error = true;
                System.out.println("[GameClassLoader] - Error while defining class: " + name + ", throwing: " + error.getMessage());
                return null;
            } catch (final Exception e) {
                crawler.error = true;
                System.out.println("[GameClassLoader] - Exception while defining class: " + name + ", throwing: " + e.getMessage());
                return null;
            }
        }

        return super.loadClass(name);
    }

}
