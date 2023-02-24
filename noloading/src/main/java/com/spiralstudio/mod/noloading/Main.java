package com.spiralstudio.mod.noloading;

import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * @author Leego Yih
 * @see com.threerings.tudey.a.k TudeySceneView
 * @see com.threerings.projectx.client.bi LoadingWindow
 */
public class Main {
    private static boolean mounted = false;

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineLoadingWindow();
        redefineProjectXSceneView();
        redefineScriptRunner();
    }

    /**
     * Hides the loading screen and removes the fading animations.
     */
    static void redefineLoadingWindow() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.client.bi")
                .modifyMethod(new MethodModifier()
                        .methodName("J") // update
                        .paramTypeNames("float")
                        .insertBefore("" +
                                "if ($1 <= 0.0F) {\n" +
                                "    this.setAlpha(0.0F);\n" +
                                "    this.getComponent(\"depth\").setAlpha(1.0F);\n" +
                                "    this.getComponent(\"progress\").setAlpha(1.0F);\n" +
                                "    this.getComponent(\"gate\").setAlpha(1.0F);\n" +
                                "    this.getComponent(\"zonename\").setAlpha(1.0F);\n" +
                                "}" +
                                ""))
                .modifyMethod(new MethodModifier()
                        .methodName("vK") // fadeOut
                        .body("" +
                                "{\n" +
                                "System.out.println(\"fadeOut\"+Long.toString(System.currentTimeMillis()));" +
                                "    ((com.threerings.opengl.gui.Label) this.getComponent(\"progress\")).setText(\"\");\n" +
                                "    this._ctx.xb().getStreamGain().value = 0.0F;\n" +
                                "    if (this.afM) {\n" +
                                "        this.acf.yO().vd();\n" +
                                "        this._ctx.xa().moveToTop(this);\n" +
                                "        this._ctx.xg().KQ();\n" +
                                "    }\n" +
                                "    this.acf.yO().uG();\n" +
                                "    this._ctx.xa().addTickParticipant(new com.threerings.projectx.client.bj(this,\n" +
                                "            new Object[]{\"fadeBackground\", Float.valueOf(0.0F), \"linger\", Float.valueOf(0.0F), \"fadeTitle\", Float.valueOf(0.0F)}));\n" +
                                "}"))
                .modifyMethod(new MethodModifier()
                        .methodName("vL") // fadeIn
                        .insertBefore("System.out.println(\"fadeIn\"+Long.toString(System.currentTimeMillis()));"))
                .build();
    }

    static void redefineProjectXSceneView() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.client.et")
                .modifyMethod(new MethodModifier()
                        .methodName("j")
                        .paramTypeNames("java.lang.Runnable")
                        .insertBefore("" +
                                "{\n" +
                                "    System.out.println(\"Look At me\" + Long.toString(System.currentTimeMillis()));\n" +
                                "    if (this.alY.isAdded()) {\n" +
                                "        this.alY.saveState();\n" +
                                "        this.alY.uH();\n" +
                                "    }\n" +
                                "    com.threerings.opengl.gui.aj var2;\n" +
                                "    (var2 = new com.threerings.opengl.gui.aj(this._ctx, (com.threerings.opengl.gui.d.g) null)).setLayer(1);\n" +
                                "    this._ctx.xa().addWindow(var2);\n" +
                                "    com.threerings.opengl.gui.a.d var3 = new com.threerings.opengl.gui.a.d(com.threerings.opengl.renderer.Color4f.BLACK);\n" +
                                "    var2.setBackground(0, var3);\n" +
                                "    var2.setBackground(1, var3);\n" +
                                "    this._ctx.xa().addTickParticipant(new com.threerings.projectx.client.eu(this, new Object[]{\"fade\", Float.valueOf(0.0F)}, var2, $1));\n" +
                                "}"))
                .build();
    }

    static void redefineScriptRunner() throws Exception {
        /*ClassBuilder.fromClass("com.threerings.opengl.gui.ay")
                .modifyMethod(new MethodModifier()
                        .methodName("runScript")
                        .paramTypeNames("java.lang.String")
                        .insertBefore("" +
                                "{\n" +
                                "    java.lang.String var1 = $1;\n" +
                                "    com.threerings.opengl.gui.config.InterfaceScriptConfig var2 = (com.threerings.opengl.gui.config.InterfaceScriptConfig) this._ctx$2c98ba92.getConfigManager().c(com.threerings.opengl.gui.config.InterfaceScriptConfig.class, var1);\n" +
                                "    System.out.println(\"runScript 0 \" + var1 + \", config=\" + var2.toString());\n" +
                                "    this.runScript(var2);\n" +
                                "}"))
                .modifyMethod(new MethodModifier()
                        .methodName("runScript")
                        .paramTypeNames("com.threerings.opengl.gui.config.InterfaceScriptConfig")
                        .insertBefore("" +
                                "{\n" +
                                "    com.threerings.opengl.gui.config.InterfaceScriptConfig var1 = $1;\n" +
                                "    com.threerings.opengl.gui.config.InterfaceScriptConfig.Original original = var1.getOriginal(this._ctx$2c98ba92.getConfigManager());\n" +
                                "    System.out.println(\"runScript 1 \" + var1.toString());\n" +
                                "    System.out.println(\"runScript 2 \" + original.toString());\n" +
                                "}"))
                .build();*/
        ClassBuilder.fromClass("com.threerings.opengl.gui.ay$a")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .paramTypeNames("float")
                        .insertBefore("$1=1000.0F;"))
                .build();
    }

    public static void main(String[] args) {
    }
}
