package com.spiralstudio.mod.teleport;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.reflect.Method;

/**
 * Enter commands to go somewhere.
 *
 * @author Leego Yih
 * @see com.threerings.crowd.chat.client.a ChatDirector
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 * @see com.threerings.projectx.client.aC HudWindow
 * @see com.threerings.projectx.client.hud.a Activities
 * @see com.threerings.projectx.mission.client.MissionPanel
 * @see com.threerings.projectx.mission.client.u MissionWindow I guess?
 */
public class Main {
    static {
        try {
            redefineSomethingToPrintLogs();
            addTeleportChatCommands();
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    static void addTeleportChatCommands() throws Exception {
        Method addCommand = Class.forName("com.spiralstudio.mod.command.Command")
                .getDeclaredMethod("addCommand", String.class, String.class);
        // Go to Haven
        addCommand.invoke(null, "haven|hh", "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "((com.threerings.projectx.client.bh)ctx__.rq().aR(com.threerings.projectx.client.bh.class)).vG();\n");
        // Go to Ready Room
        addCommand.invoke(null, "readyroom|rr", "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "java.lang.reflect.Field afuField = com.threerings.projectx.client.aC.class.getDeclaredField(\"afu\");\n" +
                "afuField.setAccessible(true);\n" +
                "com.threerings.projectx.client.hud.a a__ = (com.threerings.projectx.client.hud.a) afuField.get(hud__);\n" +
                "java.lang.reflect.Field anNField = com.threerings.projectx.client.hud.a.class.getDeclaredField(\"anN\");\n" +
                "anNField.setAccessible(true);\n" +
                "com.threerings.opengl.gui.q rr__ = (com.threerings.opengl.gui.q) anNField.get(a__);\n" +
                "Class.forName(\"com.threerings.opengl.gui.e.a\")\n" +
                "    .getDeclaredMethod(\"postAction\", new Class[]{com.threerings.opengl.gui.q.class, java.lang.String.class})\n" +
                "    .invoke(null, new Object[]{rr__, \"readyroom\"});");

        // Go to Town Square
        addCommand.invoke(null, "townsquare|ts", doScene("1"));
        // Go to Bazaar
        addCommand.invoke(null, "bazaar|ba", doScene("2"));
        // Go to Garrison
        addCommand.invoke(null, "garrison|ga", doScene("445"));
        // Go to Arcade
        addCommand.invoke(null, "arcade|ar", doScene("3"));

        // Go to FSC1
        addCommand.invoke(null, "fsc1", "\n" +
                "        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "        com.threerings.projectx.dungeon.data.LevelPartyObject lpo__ = ctx__.xk().aW(com.threerings.projectx.dungeon.data.LevelPartyObject.class);\n" +
                "        java.lang.reflect.Field lpmField = com.threerings.projectx.dungeon.data.LevelPartyObject.class.getDeclaredField(\"levelPartyService\");\n" +
                "        lpmField.setAccessible(true);\n" +
                "        com.threerings.projectx.dungeon.data.LevelPartyMarshaller lpm__ = (com.threerings.projectx.dungeon.data.LevelPartyMarshaller) lpmField.get(lpo__);\n" +
                "        lpm__.bD(true);\n" +doZone("33554687","264"));
/*        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
        com.threerings.projectx.dungeon.data.LevelPartyObject lpo__ = ctx__.xk().aW(com.threerings.projectx.dungeon.data.LevelPartyObject.class);
        java.lang.reflect.Field lpmField = com.threerings.projectx.dungeon.data.LevelPartyObject.class.getDeclaredField("levelPartyService");
        lpmField.setAccessible(true);
        com.threerings.projectx.dungeon.data.LevelPartyMarshaller lpm__ = (com.threerings.projectx.dungeon.data.LevelPartyMarshaller) lpmField.get(lpo__);
        lpm__.bD(true);*/

        // Go to FSC2
        addCommand.invoke(null, "fsc2", "\n" +
                "        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "        com.threerings.projectx.dungeon.data.LevelPartyObject lpo__ = ctx__.xk().aW(com.threerings.projectx.dungeon.data.LevelPartyObject.class);\n" +
                "        java.lang.reflect.Field lpmField = com.threerings.projectx.dungeon.data.LevelPartyObject.class.getDeclaredField(\"levelPartyService\");\n" +
                "        lpmField.setAccessible(true);\n" +
                "        com.threerings.projectx.dungeon.data.LevelPartyMarshaller lpm__ = (com.threerings.projectx.dungeon.data.LevelPartyMarshaller) lpmField.get(lpo__);\n" +
                "        lpm__.a(true, new com.threerings.projectx.client.chat.a(ctx__, \"dungeon\"));\n");

/*        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
        com.threerings.projectx.dungeon.data.LevelPartyObject lpo__ = ctx__.xk().aW(com.threerings.projectx.dungeon.data.LevelPartyObject.class);
        java.lang.reflect.Field lpmField = com.threerings.projectx.dungeon.data.LevelPartyObject.class.getDeclaredField("levelPartyService");
        lpmField.setAccessible(true);
        com.threerings.projectx.dungeon.data.LevelPartyMarshaller lpm__ = (com.threerings.projectx.dungeon.data.LevelPartyMarshaller) lpmField.get(lpo__);
        lpm__.a(true, new com.threerings.projectx.client.chat.a(ctx__, "dungeon"));*/

/*        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
        com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);
        System.out.println(hud__.toString());
        System.out.println(hud__.getClass().toString());
        com.threerings.projectx.dungeon.client.EndOfLevelWindow eolw__ = new com.threerings.projectx.dungeon.client.EndOfLevelWindow(ctx__, hud__.vk());
        com.threerings.projectx.dungeon.client.Q q__ = new com.threerings.projectx.dungeon.client.Q(ctx__, hud__.vk(), eolw__);
        ctx__.getRoot().addWindow(eolw__);
        ctx__.xa().addWindow(q__);
        q__.by(true);
        java.lang.reflect.Field _interfaceField = com.threerings.opengl.gui.aC.class.getDeclaredField("_interface");
        _interfaceField.setAccessible(true);
        ((com.threerings.opengl.gui.ay) _interfaceField.get(q__)).runScript("Clicked-Advance");*/

        /*System.out.println(hud__.toString());
        System.out.println(hud__ instanceof com.threerings.projectx.dungeon.client.i);
        com.threerings.projectx.dungeon.client.i dhud = com.threerings.projectx.dungeon.client.i.A(ctx__);
        System.out.println(dhud.toString());
        ((com.threerings.projectx.dungeon.client.B) dhud).bw(true);
        ctx__.getRoot().addWindow(dhud);
        ctx__.getRoot().addWindow(new com.threerings.projectx.dungeon.client.H(ctx__));
        ctx__.getRoot().addWindow(new com.threerings.projectx.dungeon.client.al(ctx__));*/

        // Go to FSC
        addCommand.invoke(null, "fsc|vana", doMission("king_of_ashes"));
        // Go to Jelly King
        addCommand.invoke(null, "jk|rjp", doMission("sovereign_slime"));
        // Go to Built to Destroy
        addCommand.invoke(null, "imf|twins", doMission("built_to_destroy"));
        // Go to DaN
        addCommand.invoke(null, "dan", doMission("dreams_and_nightmares"));
        // Go to Axes of Evil
        addCommand.invoke(null, "aoe", doMission("axes_of_evil"));
        // Go to Shadowplay
        addCommand.invoke(null, "sp", doMission("shadowplay"));


    }

    static void redefineSomethingToPrintLogs() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = classPool.get("com.threerings.projectx.mission.client.MissionPanel");
        CtMethod ctMethod = ctClass.getDeclaredMethod("actionPerformed", classPool.get(new String[]{"com.threerings.opengl.gui.event.ActionEvent"}));
        ctMethod.insertBefore("com.threerings.projectx.mission.a.log.f(\"Mission \"+$1.toString(),new Object[0]);\n");
        ctClass.toClass();
        ctClass.detach();

        CtClass ctClass2 = classPool.get("com.threerings.projectx.client.ex");
        CtMethod ctMethod2 = ctClass2.getDeclaredMethod("e", classPool.get(new String[]{"java.lang.String", "int[]", "int[]"}));
        ctMethod2.insertBefore("System.out.println(\"hostname=\" + $1 + \", ports=\" + java.util.Arrays.toString($2) + \", datagramPorts=\" + java.util.Arrays.toString($3) + \", _postponeMove=\" + this.amh);\n\n");
        ctClass2.toClass();
        ctClass2.detach();

        CtClass ctClass3 = classPool.get("com.threerings.projectx.dungeon.data.LevelPartyMarshaller");
        CtMethod ctMethod31 = ctClass3.getDeclaredMethod("g");
        ctMethod31.insertBefore("System.out.println(\"LevelPartyMarshaller.g: \" + Integer.valueOf($1) + \", var2=\" + $2.toString());\n");
        CtMethod ctMethod32 = ctClass3.getDeclaredMethod("h");
        ctMethod32.insertBefore("System.out.println(\"LevelPartyMarshaller.h: \" + Integer.valueOf($1) + \", var2=\" + $2.toString());\n");
        CtMethod ctMethod33 = ctClass3.getDeclaredMethod("bD");
        ctMethod33.insertBefore("System.out.println(\"LevelPartyMarshaller.bD: \" + $1);\n");
        CtMethod ctMethod34 = ctClass3.getDeclaredMethod("a", classPool.get(new String[]{"boolean", "com.threerings.presents.client.D"}));
        ctMethod34.insertBefore("System.out.println(\"LevelPartyMarshaller.a: \" + $1 + \", var2=\" + $2.toString());\n");
        CtMethod ctMethod35 = ctClass3.getDeclaredMethod("b");
        ctMethod35.insertBefore("System.out.println(\"LevelPartyMarshaller.b: \" + $1 + \", var2=\" + $2.toString());\n");
        CtMethod ctMethod36 = ctClass3.getDeclaredMethod("c");
        ctMethod36.insertBefore("System.out.println(\"LevelPartyMarshaller.c: \" + $1 + \", var2=\" + $2.toString());\n");
        CtMethod ctMethod37 = ctClass3.getDeclaredMethod("a", classPool.get(new String[]{"java.util.List", "boolean", "com.threerings.presents.client.D"}));
        ctMethod37.insertBefore("System.out.println(\"LevelPartyMarshaller.a: \" + $1.toString() + \", var2=\" + $2 + \", var3=\" + $3.toString());\n");
        ctClass3.toClass();
        ctClass3.detach();
    }

    static String doMission(String name) {
/*com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);
com.threerings.projectx.mission.client.MissionPanel missionPanel__ = new com.threerings.projectx.mission.client.MissionPanel(ctx__, null);
java.lang.String mission = "king_of_ashes";
com.threerings.projectx.dungeon.data.DungeonCodes.Difficulty difficulty = com.threerings.projectx.dungeon.data.DungeonCodes.Difficulty.HARD;
java.lang.reflect.Constructor constructor = Class.forName("com.threerings.projectx.mission.client.f").getDeclaredConstructors()[0];
constructor.setAccessible(true);
com.threerings.presents.client.D di = (com.threerings.presents.client.D) constructor
        .newInstance(new Object[]{missionPanel__, ctx__, "board", com.threerings.projectx.client.ProjectXPrefs.ConfirmPrompt.AUTO_CREATE, mission, difficulty});
((com.threerings.projectx.mission.client.t) ctx__.rq().aR(com.threerings.projectx.mission.client.t.class)).a(mission, difficulty, true, false, false, di);*/

        return "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "com.threerings.projectx.mission.client.MissionPanel missionPanel__ = new com.threerings.projectx.mission.client.MissionPanel(ctx__, null);\n" +
                "java.lang.String mission = \"" + name + "\";\n" +
                "com.threerings.projectx.dungeon.data.DungeonCodes.Difficulty difficulty = com.threerings.projectx.dungeon.data.DungeonCodes.Difficulty.HARD;\n" +
                "java.lang.reflect.Constructor constructor = Class.forName(\"com.threerings.projectx.mission.client.f\").getDeclaredConstructors()[0];\n" +
                "constructor.setAccessible(true);\n" +
                "com.threerings.presents.client.D di = (com.threerings.presents.client.D) constructor\n" +
                "        .newInstance(new Object[]{missionPanel__, ctx__, \"board\", com.threerings.projectx.client.ProjectXPrefs.ConfirmPrompt.AUTO_CREATE, mission, difficulty});\n" +
                "((com.threerings.projectx.mission.client.t) ctx__.rq().aR(com.threerings.projectx.mission.client.t.class)).a(mission, difficulty, true, false, false, di);\n";
    }

    static String doScene(String sceneId) {
/*com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
ctx__.ru().au(1);*/

        return "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "ctx__.ru().au(" + sceneId + ");\n";
    }

    static String doZone(String zoneId, String sceneId) {
/*com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
ctx__.xd().ad(1, 1);*/

        return "\n" +
                "com.threerings.projectx.util.A ctx___ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "ctx___.xd().ad(" + zoneId + ", " + sceneId + ");\n";
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
