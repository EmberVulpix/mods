package com.spiralstudio.mod.bookkeeper;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Configs;
import com.spiralstudio.mod.core.MethodModifier;
import com.spiralstudio.mod.core.Registers;
import lombok.Data;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.client.aq EnergyPanel
 * @see com.threerings.projectx.item.client.g BaseItemListPanel
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
        Config config = getConfig();
        initConfig(config);
        redefineEnergyPanel();
        redefineBaseItemListPanel();
    }

    static void initConfig(Config config) {
        if (config == null) {
            return;
        }
        BookkeeperHandler.endpoint = config.endpoint;
        BookkeeperHandler.filename = config.filename;
    }

    static void redefineEnergyPanel() {
        ClassPool.from("com.threerings.projectx.client.aq")
                .modifyMethod(new MethodModifier()
                        .methodName("uv")
                        .insertAfter("com.spiralstudio.mod.bookkeeper.BookkeeperHandler.dump(this._ctx, \"energy\", this._ctx.uk().energy);"));
    }

    static void redefineBaseItemListPanel() {
        ClassPool.from("com.threerings.projectx.item.client.g")
                .modifyMethod(new MethodModifier()
                        .methodName("updateCrowns")
                        .insertAfter("com.spiralstudio.mod.bookkeeper.BookkeeperHandler.dump(this._ctx, \"crowns\", this._ctx.uk().crowns);"));
    }

    static Config getConfig() {
        try {
            return Configs.readYaml("bookkeeper.yml", Config.class);
        } catch (Exception e) {
            System.out.println("Failed to read config: " + e.getMessage());
            return null;
        }
    }

    @Data
    public static class Config {
        private String endpoint;
        private String filename;
    }

    public static void main(String[] args) {
    }
}
