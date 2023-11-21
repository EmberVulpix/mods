package com.spiralstudio.mod.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Leego Yih
 */
public class Caches {
    private static final ConcurrentMap<String, Object> global = new ConcurrentHashMap<>();

    public static Object get(String key) {
        return global.get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        Object v = global.get(key);
        if (v == null) {
            return null;
        }
        return clazz.cast(v);
    }

    public static Object put(String key, Object config) {
        return global.put(key, config);
    }
}
