package com.spiralstudio.mod.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Leego Yih
 */
public final class HttpUtils {

    public static String get(String uri, Map<String, String> headers) throws IOException {
        return call("GET", uri, headers, null);
    }

    public static String post(String uri, Map<String, String> headers, String body) throws IOException {
        return call("POST", uri, headers, body);
    }

    public static String call(String method, String uri, Map<String, String> headers, String body) throws IOException {
        HttpURLConnection conn = null;
        OutputStreamWriter writer = null;
        InputStream is = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json;charset=\"UTF-8\"");
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.connect();
            if (body != null) {
                writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
                writer.write(body);
                writer.flush();
                writer.close();
            }
            int code = conn.getResponseCode();
            if (code >= 400) {
                System.out.println("Failed to request, code status: " + code + ", uri: " + uri + "\nbody: " + body);
            }
            is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                result.append('\n');
            }
            if (result.length() > 0) {
                result.deleteCharAt(result.length() - 1);
            }
            reader.close();
            is.close();
            conn.disconnect();
            return result.toString();
        } finally {
            try {
                if (writer != null) {writer.close();}
                if (reader != null) {reader.close();}
                if (is != null) {is.close();}
                if (conn != null) {conn.disconnect();}
            } catch (Exception ignored) {}
        }
    }

}
