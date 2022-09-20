package org.octobot.bot.game.loader.internal.callback;

import org.octobot.bot.GameDefinition;
import org.octobot.script.event.MessageEvent;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

/**
 * CodeCallback
 *
 * @author Pat-ji
 */
public class CodeCallback extends Callback {
    public String className, methodName, methodDesc;
    public int type, data;

    @Override
    public void inject(final ClassNode client, final ClassNode classNode) {
        switch (type) {
            case 0:
                for (final MethodNode methodNode : classNode.methods)
                    if (methodNode.name.equals(methodName) && methodNode.desc.equals(methodDesc)) {
                        final InsnList insnList = methodNode.instructions;
                        final String botClass = GameDefinition.class.getCanonicalName().replace(".", "/");
                        insnList.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MessageEvent.class), "create", "(L" + botClass + ";ILjava/lang/String;Ljava/lang/String;)V"));
                        insnList.insert(new VarInsnNode(Opcodes.ALOAD, 2));
                        insnList.insert(new VarInsnNode(Opcodes.ALOAD, 1));
                        insnList.insert(new VarInsnNode(Opcodes.ILOAD, 0));
                        insnList.insert(new FieldInsnNode(Opcodes.GETSTATIC, "client", "game", "L" + botClass + ";"));
                        break;
                    }

                break;
            case 1:
                for (final MethodNode methodNode : classNode.methods)
                    if (methodNode.name.contains("clinit")) {
                        final InsnList insnList = methodNode.instructions;
                        final AbstractInsnNode[] nodes = insnList.toArray();
                        int index = 0;
                        for (final AbstractInsnNode node : nodes) {
                            if (node.getOpcode() == Opcodes.PUTSTATIC && ((FieldInsnNode) node).owner.equals(className)
                                    && ((FieldInsnNode) node).name.equals(methodName) && ((FieldInsnNode) node).desc.equals(methodDesc)) {
                                for (int i = index; i >= 0; i--)
                                    if (nodes[i] instanceof IntInsnNode) {
                                        insnList.set(nodes[i], new IntInsnNode(Opcodes.SIPUSH, 600));
                                        break;
                                    }
                            }

                            index++;
                        }

                        break;
                    }

                break;
        }
    }

}
