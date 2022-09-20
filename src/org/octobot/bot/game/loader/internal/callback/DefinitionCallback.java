package org.octobot.bot.game.loader.internal.callback;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * DefinitionCallback
 *
 * @author Pat-ji
 */
public class DefinitionCallback extends Callback {
    public String className, methodName, methodDesc, type;

    @Override
    public void inject(final ClassNode client, final ClassNode classNode) {
        for (final MethodNode methodNode : classNode.methods)
            if (methodNode.name.equals(methodName) && methodNode.desc.contains(methodDesc)) {
                disarm(methodNode);

                final String callBackMethodName = type.equals("o") ? "getObjectDefinition" : "getNPCDefinition";
                for (final MethodNode mn : client.methods)
                    if (mn.name.equals(callBackMethodName)) return;

                final String callBackMethodDesc = type.equals("o") ? "Lorg/octobot/bot/game/client/RSObjectDefinition;" : "Lorg/octobot/bot/game/client/RSNPCDefinition;";
                final MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, callBackMethodName, "(I)" + callBackMethodDesc, null, null);
                final InsnList clientList = method.instructions;
                clientList.insert(new InsnNode(Opcodes.ARETURN));
                clientList.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, classNode.name, methodNode.name, methodNode.desc));
                clientList.insert(new VarInsnNode(Opcodes.BIPUSH, 10));
                clientList.insert(new VarInsnNode(Opcodes.ILOAD, 1));
                client.methods.add(method);

                break;
            }
    }

}
