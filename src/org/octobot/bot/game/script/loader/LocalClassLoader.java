package org.octobot.bot.game.script.loader;

import org.octobot.bot.game.loader.Crawler;
import org.octobot.bot.game.loader.JarStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;

/**
 * LocalClassLoader
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class LocalClassLoader extends NodeClassLoader {
    private final Map<String, Map<String, Class<?>>> jarFiles;
    private final URL base;

    public LocalClassLoader(final URL url) {
        jarFiles = new HashMap<String, Map<String, Class<?>>>();
        base = url;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        name = name.replace('/', '.');
        Class<?> clazz = super.findLoadedClass(name);
        if (clazz != null) return clazz;

        try {
            if (name.contains(".jar")) {
                clazz = loadJar(name);
            } else {
                final InputStream inputStream = getResourceAsStream(name.replace('.', '/') + ".class");
                final ClassReader reader = new ClassReader(inputStream);
                final ClassNode node = new ClassNode();
                reader.accept(node, 0);

                try {
                    name = node.name.replace('/', '.');
                    clazz = nodeToClass(name, node);
                } catch (final LinkageError ignored) {
                } finally {
                    inputStream.close();
                }

                if (clazz == null) return super.loadClass(name);
            }
        } catch (final Exception e) {
            try {
                if (clazz == null)
                    clazz = super.loadClass(name);
            } catch (final Exception ignored) { }
        }

        return clazz;
    }

    @Override
    public URL getResource(final String name) {
        try {
            return new URL(base, name);
        } catch (final MalformedURLException e) {
            return null;
        }
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        try {
            return new URL(base, name).openStream();
        } catch (final Exception e) {
            return null;
        }
    }

    private Class<?> loadJar(final String name) throws Exception {
        final String jarName = name.substring(0, name.indexOf(".jar") + 4);
        System.out.println(jarName);
        if (!jarFiles.containsKey(jarName)) {
            jarFiles.put(jarName, new HashMap<String, Class<?>>());

            final String jarUrl = base.getPath().substring(1) + jarName;
            final File jarFile = new File(jarUrl);
            final JarStream stream = new JarStream(new ByteArrayInputStream(new Crawler().readInputSteam(new FileInputStream(jarUrl))));

            final JarClassLoader classLoader = new JarClassLoader();
            JarEntry entry;
            while ((entry = stream.getNextJarEntry()) != null) {
                final String entryName = entry.getName().replace('/', '.');
                if (entryName.endsWith(".class")) {
                    System.out.println("entry: " + entryName);
                    final String className = entryName.substring(0, entryName.length() - 6);
                    if (!classLoader.getClasses().containsKey(className)) {
                        classLoader.getClasses().put(className, stream.readStream());
                        System.out.println("putting class" + className);
                    }
                }
            }

            for (final String entryName : classLoader.getClasses().keySet()) {
                final Class clazz = classLoader.loadClass(entryName);
                if (clazz != null) {
                    jarFiles.get(jarName).put(entryName, clazz);
                } else {
                    System.out.println("[LocalClassLoader] - Failed to load: " + clazz);
                }
            }

            stream.close();
        }

        return jarFiles.get(jarName).get(name.substring(jarName.length() + 1));
    }

}

