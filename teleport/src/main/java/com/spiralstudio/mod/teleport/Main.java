package com.spiralstudio.mod.teleport;

import com.threerings.projectx.client.ProjectXApp;

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

    static String doMission(String name) {
        return "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
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
        return "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "ctx__.ru().au(" + sceneId + ");\n";
    }

    /**
     * @deprecated access denied
     */
    static String doZone(String zoneId, String sceneId) {
        return "com.threerings.projectx.util.A ctx___ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "ctx___.xd().ad(" + zoneId + ", " + sceneId + ");\n";
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
