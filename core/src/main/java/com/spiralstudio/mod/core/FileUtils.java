package com.spiralstudio.mod.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Leego Yih
 */
public final class FileUtils {

    public static void write(String filename, String content) throws IOException {
        write(filename, content, false);
    }

    public static void write(String filename, String content, boolean append) throws IOException {
        File file = new File(filename);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            boolean created = dir.mkdir();
            if (!created) {
                throw new IOException("Failed to create dir: " + dir.getAbsolutePath());
            }
        }
        try (FileWriter writer = new FileWriter(file, append)) {
            writer.write(content);
            writer.flush();
        }
    }

}
