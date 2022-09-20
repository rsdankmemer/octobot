package org.octobot.bot.game.loader.internal.callback;

import org.octobot.bot.game.client.RSModel;
import org.octobot.bot.game.loader.internal.Injector;
import org.octobot.script.wrappers.Model;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * MethodCallBack
 *
 * @author Pat-ji
 */
public class MethodCallBack extends Callback {
    public String methodName, newName, methodDesc, newMethodDesc;

    @Override
    public void inject(final ClassNode client, final ClassNode classNode) {
        for (int i = 0; i < classNode.methods.size(); i++) {
            final MethodNode methodNode = classNode.methods.get(i);
            if (methodNode.name.equals(methodName) && methodNode.desc.equals(methodDesc)) {
                if (newName.equals("getModelInstance")) {
                    boolean fieldInjected = false;
                    for (final FieldNode fieldNode : classNode.fields)
                        if (fieldNode.name.equals("modelInstance")) {
                            fieldInjected = true;
                            break;
                        }

                    if (!fieldInjected) {
                        final String fieldDesc = "L" + Model.class.getCanonicalName().replace(".", "/") + ";";
                        classNode.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_VOLATILE, "modelInstance", fieldDesc, null, null);
                        Injector.addGetter(classNode, classNode.name, Injector.getField(classNode, "modelInstance"), "getModelInstance", fieldDesc, null);
                    }

                    final InsnList insnList = methodNode.instructions;
                    final AbstractInsnNode[] nodes = insnList.toArray();
                    for (final AbstractInsnNode node : nodes)
                        if (node.getOpcode() == Opcodes.PUTFIELD) {
                            insnList.insert(node, new FieldInsnNode(Opcodes.PUTFIELD, classNode.name, "modelInstance", "L" + Model.class.getCanonicalName().replace(".", "/") + ";"));
                            insnList.insert(node, new MethodInsnNode(Opcodes.INVOKESPECIAL, Model.class.getCanonicalName().replace(".", "/"),
                                    "<init>", "(L" + RSModel.class.getCanonicalName().replace(".", "/") + ";)V"));
                            insnList.insert(node, new VarInsnNode(Opcodes.ALOAD, 10));
                            insnList.insert(node, new InsnNode(Opcodes.DUP));
                            insnList.insert(node, new TypeInsnNode(Opcodes.NEW, Model.class.getCanonicalName().replace(".", "/")));
                            insnList.insert(node, new VarInsnNode(Opcodes.ALOAD, 0));
                            break;
                        }
                } else {
                    final MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, newName, newMethodDesc, null, null);
                    if (methodNode.desc.contains("I") || methodNode.desc.contains("B")) {
                        disarm(methodNode);

                        method.visitVarInsn(Opcodes.ALOAD, 0);
                        method.visitVarInsn(Opcodes.BIPUSH, 10);
                    } else {
                        method.visitVarInsn(Opcodes.ALOAD, 0);
                    }

                    method.visitMethodInsn(Opcodes.INVOKESPECIAL, classNode.name, methodNode.name, methodNode.desc);

                    final int op = Injector.getReturnOpcode(methodNode.desc);
                    method.visitInsn(op);
                    method.visitMaxs(op == Opcodes.LRETURN || op == Opcodes.DRETURN ? 2 : 1, (methodNode.access & Opcodes.ACC_STATIC) == 0 ? 1 : 0);
                    classNode.methods.add(method);
                }
            }
        }
    }

}
