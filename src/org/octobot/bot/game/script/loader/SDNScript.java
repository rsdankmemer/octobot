package org.octobot.bot.game.script.loader;

import org.octobot.bot.game.loader.Crawler;
import org.octobot.bot.game.loader.HttpClient;
import org.octobot.bot.game.loader.JarStream;
import org.octobot.bot.game.script.GameScript;
import org.octobot.bot.game.script.ScriptCategory;
import org.octobot.bot.handler.TextHandler;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.jar.JarEntry;

/**
 * SDNScript
 *
 * @author Pat-ji
 */
@SuppressWarnings("unchecked")
public class SDNScript implements ScriptSource {
    private final String name, description;
    private final double version;
    private final int id;
    private final String authors;

    public SDNScript( final int id, final String name, final String description, final double version, final String authors) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.authors = authors;
    }

    @Override
    public GameScript getScript() {
        try {
            final URL url = new URL("https://octobot.org/panel/bot/script/" + id + ".jar");

            final JarStream jarStream = new JarStream(new ByteArrayInputStream(new Crawler().readInputSteam(HttpClient.getHttpsInputStream(url))));

            final JarClassLoader loader = new JarClassLoader();
            JarEntry entry;
            while ((entry = jarStream.getNextJarEntry()) != null) {
                final String name = entry.getName().replace('/', '.');
                if (name.endsWith(".class")) {
                    final String classname = name.substring(0, name.length() - 6);
                    if (!loader.getClasses().containsKey(classname))
                        loader.getClasses().put(classname, jarStream.readStream());
                }
            }

            jarStream.close();

            GameScript script = null;
            for (final String name : loader.getClasses().keySet()) {
                final Class clazz = loader.loadClass(name);
                if (clazz != null && GameScript.class.isAssignableFrom(clazz))
                    try {
                        script = (GameScript) clazz.asSubclass(GameScript.class).newInstance();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
            }

            if (script != null) return script;
        } catch (final Exception e) {
            System.out.println("[SDNScript] - Exception thrown: " + e.getMessage());
        }

        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public double version() {
        return version;
    }

    @Override
    public String authors() {
        return authors;
    }

    @Override
    public ScriptCategory category() {
        return ScriptCategory.OTHER;
    }

}