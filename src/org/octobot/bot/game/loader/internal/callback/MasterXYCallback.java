package org.octobot.bot.game.loader.internal.callback;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * MasterXY
 *
 * @author Pat-ji
 */
public class MasterXYCallback extends Callback {
    public String className, methodName, widgetClass, boundsIndex;

    @Override
    public void inject(final ClassNode client, final ClassNode classNode) {
        for (final MethodNode methodNode : classNode.methods)
            if (methodNode.name.equals(methodName)) {
                final InsnList insnList = methodNode.instructions;
                int var = 0;
                for (final AbstractInsnNode ain : insnList.toArray()) {
                    if (ain.getOpcode() == Opcodes.ALOAD)
                        var = ((VarInsnNode) ain).var;

                    if (ain instanceof FieldInsnNode) {
                        final FieldInsnNode fin = (FieldInsnNode) ain;
                        if (fin.owner.equals(widgetClass) && fin.name.equals(boundsIndex)) {
                            insnList.insert(fin, new FieldInsnNode(Opcodes.PUTFIELD, widgetClass, "masterY", "I"));
                            insnList.insert(fin, new VarInsnNode(Opcodes.ILOAD, 7));
                            insnList.insert(fin, new VarInsnNode(Opcodes.ALOAD, var));

                            insnList.insert(fin, new FieldInsnNode(Opcodes.PUTFIELD, widgetClass, "masterX", "I"));
                            insnList.insert(fin, new VarInsnNode(Opcodes.ILOAD, 6));
                            insnList.insert(fin, new VarInsnNode(Opcodes.ALOAD, var));
                            return;
                        }
                    }
                }
            }
    }

}
