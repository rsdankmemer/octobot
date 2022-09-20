package org.octobot.bot.game.loader.internal;

import org.octobot.bot.GameDefinition;
import org.octobot.bot.game.loader.internal.callback.*;
import org.octobot.bot.game.loader.internal.wrappers.input.Canvas;
import org.octobot.bot.game.loader.internal.wrappers.input.Keyboard;
import org.octobot.bot.game.loader.internal.wrappers.input.Mouse;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Injector
 *
 * @author Pat-ji
 */
@SuppressWarnings("all")
public class Injector {
    private String widgetClass;

    public Injector(final Map<String, byte[]> classes) {
        final String canvasSuperClass = Canvas.class.getCanonicalName().replace('.', '/');
        final String mouseSuperClass = Mouse.class.getCanonicalName().replace('.', '/');
        final String keyboardSuperClass = Keyboard.class.getCanonicalName().replace('.', '/');

        for (final String index : classes.keySet()) {
            byte[] read = classes.get(index);
            final ClassReader reader = new ClassReader(read);
            final ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            if (node.superName.equals("java/awt/Canvas")) {
                node.superName = canvasSuperClass;
                for (final MethodNode method : node.methods)
                    if (method.name.equals("<init>"))
                        for (final AbstractInsnNode insn : method.instructions.toArray())
                            if (insn instanceof MethodInsnNode && ((MethodInsnNode) insn).owner.equals("java/awt/Canvas"))
                                ((MethodInsnNode) insn).owner = canvasSuperClass;

                final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                node.accept(writer);
                read = writer.toByteArray();
            } else {
                read = subclass(node, "MouseListener", mouseSuperClass, "mouse");
                read = subclass(node, "MouseMotionListener", mouseSuperClass);
                read = subclass(node, "KeyListener", keyboardSuperClass, "key", "focus");
            }

            classes.put(index, read);
        }
    }

    public byte[] subclass(final ClassNode node, final String impl, final String superClass, final String... methods) {
        for (final String inter : node.interfaces)
            if (inter.contains(impl)) {
                node.superName = superClass;
                for (final MethodNode mn : node.methods)
                    for (final String method : methods)
                        if (mn.name.contains(method))
                            mn.name = "_" + mn.name;

                superClass(node, superClass, "java/lang/Object");
                for (final MethodNode method : node.methods) {
                    if (!method.name.equals("<init>")) continue;

                    for (final AbstractInsnNode ain : method.instructions.toArray())
                        if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
                            ((MethodInsnNode) ain).owner = superClass;
                            break;
                        }
                }
            }

        superClass(node, superClass, "java/awt/event/" + impl);
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    private void superClass(final ClassNode node, final String superclass, final String subclass) {
        if (node.superName.equals(subclass))
            node.superName = superclass.replaceAll("\\.", "/");
    }

    private void readHookFile(final List<GetterCallback> getters, final Map<String, String> interfaces, final Map<String, List<Callback>> callbacks) throws Exception, Error {
        final InputStream fileInputStream = new URL("http://novakscripts.net/bot/hooks.txt").openStream();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        String s = null;

        /*/while ((s=bufferedReader.readLine())!=null)
        {

            System.out.println(s);
        }/*/

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.replace("accessors", "client");
            final String[] split = line.split("\\|");
            final String param = split[0];
            if (param.equals("f")) {
                final GetterCallback getter = new GetterCallback(split[1], split[2], split[3], split[4], split[5]);
                if (split.length == 7) {
                    final long multiplier = Long.parseLong(split[6]);
                    if (multiplier > -2147483648L && multiplier < 2147483647L) {
                        getter.multiplier = (int) multiplier;
                    } else {
                        getter.multiplier = multiplier;
                    }
                }

                getters.add(getter);
            } else if (param.equals("m")) {
                final MethodCallBack methodCallBack = new MethodCallBack();
                methodCallBack.methodName = split[2];
                methodCallBack.newName = split[3];
                methodCallBack.methodDesc = split[4];
                methodCallBack.newMethodDesc = split[5];

                if (!callbacks.containsKey(split[1]))
                    callbacks.put(split[1], new ArrayList<Callback>());

                callbacks.get(split[1]).add(methodCallBack);
            } else if (param.equals("xy")) {
                final MasterXYCallback masterXY = new MasterXYCallback();
                masterXY.widgetClass = split[1];
                masterXY.className = split[2];
                masterXY.methodName = split[3];
                masterXY.boundsIndex = split[4];

                widgetClass = masterXY.widgetClass;

                if (!callbacks.containsKey(masterXY.className))
                    callbacks.put(masterXY.className, new ArrayList<Callback>());

                callbacks.get(masterXY.className).add(masterXY);
            } else if (param.equals("cb")) {
                final CodeCallback callback = new CodeCallback();
                callback.className = split[1];
                callback.methodName = split[2];
                callback.methodDesc = split[3];
                callback.type = Integer.parseInt(split[4]);
                callback.data = Integer.parseInt(split[5]);

                if (!callbacks.containsKey(callback.className))
                    callbacks.put(callback.className, new ArrayList<Callback>());

                callbacks.get(callback.className).add(callback);
            } else if (param.equals("def")) {
                final DefinitionCallback callback = new DefinitionCallback();
                callback.className = split[1];
                callback.methodName = split[2];
                callback.methodDesc = split[3];
                callback.type = split[4];

                if (!callbacks.containsKey(callback.className))
                    callbacks.put(callback.className, new ArrayList<Callback>());

                callbacks.get(callback.className).add(callback);
            } else if (!interfaces.containsKey(split[1])) {
                interfaces.put(split[1], split[2]);
            }
        }

        fileInputStream.close();
        bufferedReader.close();
    }

    public Map<String, byte[]> inject(final Map<String, byte[]> classes) throws Error, Exception {
        final List<GetterCallback> getters = new ArrayList<GetterCallback>();
        final Map<String, String> interfaces = new HashMap<String, String>();
        final Map<String, List<Callback>> callbacks = new HashMap<String, List<Callback>>();

        readHookFile(getters, interfaces, callbacks);

        ClassReader classReader = new ClassReader(classes.get("client"));

        final ClassNode client = new ClassNode();
        classReader.accept(client, ClassReader.SKIP_DEBUG);

        for (final byte[] bytes : classes.values()) {
            classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_DEBUG);
            if (classNode.name.equals("client"))
                classNode = client;

            for (final GetterCallback g : getters) {
                if (g.owner.equals(classNode.name))
                    for (final FieldNode fieldNode : classNode.fields)
                        if (fieldNode.name.equals(g.fieldName)) {
                            if(fieldNode.desc.equals("[Lcr;")) {
                                System.out.println(g.owner + "." + fieldNode.name);
                            }
                            if (fieldNode.desc.contains("L") && !fieldNode.desc.contains("java") && !fieldNode.desc.equals("[Lcr;")) {
                                //System.out.println(g.fieldName + " desc = " + g.owner);
                                g.desc = fieldNode.desc.replaceAll(fieldNode.desc.substring(fieldNode.desc.indexOf("L"),
                                        fieldNode.desc.indexOf(";")), "Lorg/octobot/bot/game/client/RS" + g.methodDesc);
                            }
                            addGetter(Modifier.isStatic(fieldNode.access) ? client : classNode, g.owner, fieldNode,
                                    (g.desc.equals("Z") ? "is" : "get") + g.methodName.substring(0, 1).toUpperCase() + g.methodName.substring(1), g.desc, g.multiplier);
                        }

            }
            if (callbacks.containsKey(classNode.name)) {
                for (final Callback callback : callbacks.get(classNode.name))
                    callback.inject(client, classNode);

                callbacks.remove(classNode.name);
            }

            if (classNode.name.equals(widgetClass)) {
                classNode.visitField(Opcodes.ACC_PUBLIC, "masterX", "I", null, null);
                classNode.visitField(Opcodes.ACC_PUBLIC, "masterY", "I", null, null);
                addGetter(classNode, classNode.name, getField(classNode, "masterX"), "getMasterX", "I", null);
                addGetter(classNode, classNode.name, getField(classNode, "masterY"), "getMasterY", "I", null);
            }

            final String impl = interfaces.get(classNode.name);
            if (impl != null)
                classNode.interfaces.add("org/octobot/bot/game/client/RS" + impl);

            if (classNode == client) continue;

            final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            classes.put(classNode.name, classWriter.toByteArray());
        }

        client.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "game", "L" + GameDefinition.class.getCanonicalName().replace(".", "/") + ";", null, null);

        final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        client.accept(classWriter);
        classes.put(client.name, classWriter.toByteArray());

        return classes;
    }

    public static FieldNode getField(final ClassNode classNode, final String name) {
        for (final FieldNode fieldNode : classNode.fields)
            if (fieldNode.name.equals(name))
                return fieldNode;

        return null;
    }

    public static void addGetter(final ClassNode node, final String owner, final FieldNode field, final String name, final String desc, final Object multiplier) {
        final MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, name, "()" + desc, null, null);
        if (!Modifier.isStatic(field.access))
            method.visitVarInsn(Opcodes.ALOAD, 0);

        method.visitFieldInsn(Modifier.isStatic(field.access) ? Opcodes.GETSTATIC : Opcodes.GETFIELD, owner, field.name, field.desc);
        if (multiplier != null) {
            method.visitLdcInsn(multiplier);
            if (multiplier instanceof Long) {
                method.visitInsn(Opcodes.LMUL);
            } else {
                method.visitInsn(Opcodes.IMUL);
            }
        }

        final int op = getReturnOpcode(field.desc);
        method.visitInsn(op);
        method.visitMaxs(op == Opcodes.LRETURN || op == Opcodes.DRETURN ? 2 : 1, (field.access & Opcodes.ACC_STATIC) == 0 ? 1 : 0);
        node.methods.add(method);
    }

    public static int getReturnOpcode(String desc) {
        desc = desc.substring(desc.indexOf(")") + 1);
        if (desc.length() > 1)
            return Opcodes.ARETURN;

        final char c = desc.charAt(0);
        switch (c) {
            case 'I':
            case 'Z':
            case 'B':
            case 'S':
            case 'C':
                return Opcodes.IRETURN;
            case 'J':
                return Opcodes.LRETURN;
            case 'F':
                return Opcodes.FRETURN;
            case 'D':
                return Opcodes.DRETURN;
        }

        throw new RuntimeException("bad_return");
    }

}
