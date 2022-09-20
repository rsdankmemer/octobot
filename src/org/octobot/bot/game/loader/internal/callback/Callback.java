package org.octobot.bot.game.loader.internal.callback;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * Callback
 *
 * @author Pat-ji
 */
public abstract class Callback {

    public abstract void inject(final ClassNode client, final ClassNode classNode);

    protected void disarm(final MethodNode methodNode) {
        final InsnList insnList = methodNode.instructions;
        for (final AbstractInsnNode ain : insnList.toArray())
            if (ain.getOpcode() == Opcodes.IF_ICMPNE || ain.getOpcode() == Opcodes.IF_ICMPLT || ain.getOpcode() == Opcodes.IF_ICMPLE
                    || ain.getOpcode() == Opcodes.IF_ICMPEQ || ain.getOpcode() == Opcodes.IF_ICMPGE || ain.getOpcode() == Opcodes.IF_ICMPGT) {
                final int index = insnList.indexOf(ain);
                AbstractInsnNode jin = insnList.get(index + 1);
                if (jin instanceof TypeInsnNode && ((TypeInsnNode) jin).desc.equals("java/lang/IllegalStateException")) {
                    jin = insnList.get(index - 1);
                    insnList.insert(jin, new LdcInsnNode(methodNode.desc.contains("B") ? Byte.MAX_VALUE : Integer.MAX_VALUE));
                    insnList.remove(jin);
                    insnList.set(insnList.get(index), new JumpInsnNode(Opcodes.IF_ICMPLE, ((JumpInsnNode) ain).label));
                }
            }
    }

}
