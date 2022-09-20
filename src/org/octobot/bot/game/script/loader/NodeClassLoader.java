package org.octobot.bot.game.script.loader;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

/**
 * NodeClassLoader
 *
 * @author Pat-ji
 */
public class NodeClassLoader extends ClassLoader {

    protected byte[] nodeToBytes(final ClassNode node) {
        final ClassWriter cw = new ClassWriter(0);
        node.accept(cw);
        return cw.toByteArray();
    }

    protected Class<?> nodeToClass(final String name, final ClassNode node) throws Exception {
        final Class<?> loaded = findLoadedClass(name);
        if (loaded != null) return loaded;

        try {
            final byte[] data = nodeToBytes(node);
            return data != null && data.length > 0 ? defineClass(name, data, 0, data.length) : null;
        } catch (final NoClassDefFoundError ignored) { }

        return null;
    }

}
