package com.spiralstudio.mod.core;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Leego Yih
 */
public final class Configs {
    private static final String DIR = System.getProperty("user.dir");

    public static <T> T readYaml(String filename, Class<T> clazz) throws IOException {
        File file = getConfigFile(filename);
        if (file == null) {
            return null;
        }
        try (InputStream is = Files.newInputStream(file.toPath())) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(is, clazz);
        }
    }

    public static <K, V> Map<K, V> readYaml(String filename) throws IOException {
        File file = getConfigFile(filename);
        if (file == null) {
            return null;
        }
        try (InputStream is = Files.newInputStream(file.toPath())) {
            Yaml yaml = new Yaml();
            return yaml.load(is);
        }
    }

    public static Config readRemote(String url, Map<String, String> headers) throws IOException {
        String res = HttpUtils.get(url, headers);
        if (res == null || res.isEmpty()) {
            return null;
        }
        Config config = JsonUtils.parse(res, Config.class);
        if (config == null) {
            throw new IOException("Failed to parse config");
        }
        return config;
    }

    public static File getConfigFile(String filename) {
        File file = new File(DIR + "/code-mods/" + filename);
        if (!file.exists()) {
            file = new File(DIR + "/" + filename);
        }
        return file.exists() ? file : null;
    }
}
