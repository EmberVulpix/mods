package com.spiralstudio.mod.bookkeeper;

import com.spiralstudio.mod.core.FileUtils;
import com.spiralstudio.mod.core.HttpUtils;
import com.spiralstudio.mod.core.JsonUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Leego Yih
 */
public class BookkeeperHandler {
    public static String endpoint = "";
    public static String filename = "";
    public static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void dump(com.threerings.projectx.util.A ctx, String type, int value) {
        String name = getKnightName(ctx);
        if (name != null) {
            executor.submit(() -> {
                callAPI(name, type, value);
                writeFile(name, type, value);
            });
        }
    }

    public static void callAPI(String name, String type, int value) {
        String body = null;
        try {
            if (endpoint == null || endpoint.isEmpty()) {
                return;
            }
            Map<String, Object> obj = new HashMap<>();
            obj.put("knight", name);
            obj.put("type", type);
            obj.put("value", value);
            obj.put("timestamp", System.currentTimeMillis());
            body = JsonUtils.toString(obj);
            HttpUtils.post(endpoint, Collections.emptyMap(), body);
        } catch (Exception e) {
            System.out.println("[Bookkeeper] Failed to call api: " + endpoint + ", data: " + body);
            e.printStackTrace();
        }
    }

    public static void writeFile(String name, String type, int value) {
        String body = null;
        try {
            if (filename == null || filename.isEmpty()) {
                return;
            }
            body = "[" + LocalDateTime.now() + "] " + name + ": " + type + "=" + value + "\n";
            FileUtils.write(filename, body + "\n", true);
        } catch (Exception e) {
            System.out.println("[Bookkeeper] Failed to write file: " + filename + ", data: " + body);
            e.printStackTrace();
        }
    }

    private static String getKnightName(com.threerings.projectx.util.A ctx) {
        Object name = null;
        try {
            Field field = Class.forName("com.threerings.util.Name").getDeclaredField("_name");
            field.setAccessible(true);
            name = field.get(ctx.uk().knight);
        } catch (Exception e) {
            System.out.println("[Bookkeeper] Failed to get knight name");
        }
        if (name != null) {
            return (String) name;
        } else {
            return null;
        }
    }
}
