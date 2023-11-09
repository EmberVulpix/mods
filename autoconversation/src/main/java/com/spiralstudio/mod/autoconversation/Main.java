package com.spiralstudio.mod.autoconversation;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.ConstructorModifier;
import com.spiralstudio.mod.core.Registers;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.client.F ConversationDialog
 */
public class Main {
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineConversationDialog();
    }

    static void redefineConversationDialog() {
        ClassPool.from("com.threerings.projectx.client.F")
                .modifyConstructor(new ConstructorModifier()
                        .insertAfter("System.out.println(\"autoconversation\" + java.util.Arrays.toString(this.acX))"));
    }

    public static void main(String[] args) {
    }
}
