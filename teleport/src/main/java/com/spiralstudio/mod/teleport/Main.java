package com.spiralstudio.mod.teleport;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Configs;
import com.spiralstudio.mod.core.MethodModifier;
import com.spiralstudio.mod.core.Registers;
import lombok.Data;

import java.util.Map;

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
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        addTeleportChatCommands();
    }

    static void addTeleportChatCommands() {
        // Go to Haven
        Commands.addCommand("haven|hh", "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "((com.threerings.projectx.client.bh)ctx__.rq().aR(com.threerings.projectx.client.bh.class)).vG();\n");
        // Go to Ready Room
        Commands.addCommand("readyroom|rr", "\n" +
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
        try {
            // Read custom configuration
            Config config = Configs.readYaml("teleport.yml", Config.class);
            if (config != null && config.getScene() != null) {
                for (Map.Entry<String, String> entry : config.getScene().entrySet()) {
                    Commands.addCommand(entry.getValue(), doScene(entry.getKey()));
                }
            }
            if (config != null && config.getMission() != null) {
                for (Map.Entry<String, String> entry : config.getMission().entrySet()) {
                    Commands.addCommand(entry.getValue(), doMission(entry.getKey()));
                }
            }
        } catch (Exception e) {
            System.out.println("[Teleport] Failed to read config");
            e.printStackTrace();
        }
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

    static void printAllMissions() {
        ClassPool.from("com.threerings.projectx.mission.client.MissionPanel")
                .modifyMethod(new MethodModifier()
                        .methodName("actionPerformed")
                        .insertBefore("System.out.println(this.aNB);"));
    }

    @Data
    public static class Config {
        private Map<String, String> scene;
        private Map<String, String> mission;
    }

    public static void main(String[] args) {
    }
}
