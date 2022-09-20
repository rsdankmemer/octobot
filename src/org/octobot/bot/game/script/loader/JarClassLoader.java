package org.octobot.bot.game.script.loader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

/**
 * JarClassLoader
 *
 * @author Pat-ji
 */
public class JarClassLoader extends NodeClassLoader {
    private final Map<String, byte[]> classes;

    public JarClassLoader() throws Exception {
        this.classes = new HashMap<String, byte[]>();
    }

    public Map<String, byte[]> getClasses() {
        return classes;
    }

    @Override
    public final Class<?> loadClass(String name) {
        try {
            name = name.replace("\\.", "/");
            Class<?> clazz = super.findLoadedClass(name);
            if (clazz != null) return clazz;

            final byte buffer[] = classes.get(name);
            if (buffer != null) {
                try {
                    return defineClass(name, buffer, 0, buffer.length);
                } catch (final Error ignored) {
                } catch (final Exception ignored) { }
            }

            try {
                final byte[] data = classes.get(name);
                if (data != null) {
                    final ClassReader reader = new ClassReader(data);
                    final ClassNode node = new ClassNode();
                    reader.accept(node, 0);
                    return nodeToClass(name, node);
                }
            } catch (final IllegalArgumentException ignored) { }

            clazz = super.loadClass(name);
            if (clazz != null) return clazz;
        } catch (final Error ignored) {
        } catch (final Exception ignored) { }

        return null;
    }

}
