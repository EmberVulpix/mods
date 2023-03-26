package com.spiralstudio.mod.nonews;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * @author Leego Yih
 * @see com.threerings.tudey.a.k TudeySceneView
 * @see com.threerings.projectx.client.bi LoadingWindow
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
        redefineLoadingWindow();
    }

    static void redefineLoadingWindow() {
        ClassPool.from("com.threerings.projectx.client.bi")
                .modifyMethod(new MethodModifier()
                        .methodName("vH")
                        .body("{return false;}"));
    }

    public static void main(String[] args) {
    }
}
