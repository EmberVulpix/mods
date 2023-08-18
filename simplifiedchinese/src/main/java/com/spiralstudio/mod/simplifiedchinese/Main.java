package com.spiralstudio.mod.simplifiedchinese;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.ConstructorModifier;
import com.spiralstudio.mod.core.MethodModifier;
import com.spiralstudio.mod.core.Registers;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Properties;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.util.a#Nm
 * @see com.samskivert.util.m#b(String, String[])
 * @see com.threerings.projectx.client.cs.a
 */
public class Main {
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        // Redefine to print ProjectXBootstrapData
        redefineClient();
        // Redefine to replace language
        redefineProjectXCredentials();
        // Redefine to prevent image requests from failing
        redefineNewsPanel();
        // Reset locale to SIMPLIFIED_CHINESE
        resetLocale();
    }

    static void redefineClient() {
        ClassPool.from("com.threerings.projectx.client.du")
                .modifyMethod(new MethodModifier()
                        .methodName("b")
                        .paramTypeNames("com.threerings.presents.net.BootstrapData", "com.threerings.presents.dobj.e")
                        .body("{" +
                                "System.out.println(\"BootstrapData\" + $1.toString());\n" +
                                "com.threerings.projectx.data.ProjectXBootstrapData var3 = (com.threerings.projectx.data.ProjectXBootstrapData)$1;\n" +
                                "this.b(var3.hostname, var3.ports, var3.datagramPorts);\n" +
                                "super.b($1, $2);" +
                                "}"));
    }

    static void redefineProjectXCredentials() {
        ClassPool.from("com.threerings.projectx.data.ProjectXCredentials")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("com.threerings.util.Name", "java.lang.String", "boolean")
                        .body("{" +
                                "super($1, $2 == null ? \"\" : ($3 ? $2 : com.samskivert.util.aq.af($2)));\n" +
                                "this.ident = com.threerings.util.G.QK();\n" +
                                "if (this.ident != null && !this.ident.matches(\"S[A-Za-z0-9/+]{32}\")) {\n" +
                                "    this.ident = \"C\" + this.ident;\n" +
                                "}\n" +
                                "System.out.println(\"[ProjectXCredentials] Fixed Language: en, Original Language: \"+com.threerings.projectx.client.ProjectXPrefs.getLanguage());\n" +
                                "this.language = \"en\";\n" +
                                "this.region = com.threerings.projectx.client.ProjectXPrefs.xG();\n" +
                                "this.steamClient = com.threerings.froth.SteamAPI.isInitialized();\n" +
                                "this.siteId = java.lang.Integer.getInteger(\"siteId\", 0).intValue();\n" +
                                "this.invite = java.lang.System.getProperty(\"invite\");\n" +
                                "}"));
    }

    static void redefineNewsPanel() {
        //2023/02/01 13:05:30:956 WARNING K$a.b: Could not read news image [key=left.image, url=http://content.spiralknights.com/images/rank_10/rank_10-2_uplink-sml_zh.png]
        //javax.imageio.IIOException: Can't get input stream from URL!
        //	at javax.imageio.ImageIO.read(ImageIO.java:1395)
        //	at com.threerings.projectx.client.cs$a.a(SourceFile:304)
        //	at com.threerings.projectx.client.cs$a.fZ(SourceFile:267)
        //	at com.samskivert.util.H.fF(SourceFile:210)
        //	at com.samskivert.util.V.run(SourceFile:60)
        ClassPool.from("com.threerings.projectx.client.cs$a")
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .body("{" +
                                "String var3;\n" +
                                "if ((var3 = $0.getProperty($1)) != null) {\n" +
                                "    java.net.URL var6 = com.threerings.projectx.util.a.dl(var3.replaceAll(\"\\\\{lang\\\\}\", \"en\"));\n" +
                                "    try {\n" +
                                "        return javax.imageio.ImageIO.read(var6);\n" +
                                "    } catch (java.lang.Throwable var5) {\n" +
                                "        if (com.threerings.projectx.util.a.MP() && $0.j(var5)) {\n" +
                                "            com.threerings.projectx.a.log.f(\"Could not read news image\", new Object[]{\"key\", $1, \"url\", var6});\n" +
                                "        } else {\n" +
                                "            com.threerings.projectx.a.log.f(\"Could not read news image\", new Object[]{\"key\", $1, \"url\", var6, var5});\n" +
                                "            $2[0] = true;\n" +
                                "        }\n" +
                                "    }\n" +
                                "}\n" +
                                "return null;\n" +
                                "}"));
    }

    static void resetLocale() throws Exception {
        // com.threerings.projectx.util.DeploymentConfig
        Class deploymentConfigClass = Class.forName("com.threerings.projectx.util.a");
        Field configField = deploymentConfigClass.getDeclaredField("akf");
        configField.setAccessible(true);
        Object config = configField.get(null);
        // com.samskivert.util.Config
        Class configClass = Class.forName("com.samskivert.util.m");
        Field propsField = configClass.getDeclaredField("AQ");
        propsField.setAccessible(true);
        Properties props = (Properties) propsField.get(config);
        // Add a new locale "zh"
        String newLocale = Locale.SIMPLIFIED_CHINESE.getLanguage();
        String supportedLocales = props.getProperty("supported_locales");
        props.put("supported_locales", String.format("%s, %s", supportedLocales, newLocale));
        // Modify default locale
        props.put("default_locale", newLocale);
        System.out.printf("Supported locales: %s, additional locale: %s, default locale: %s%n", supportedLocales, newLocale, newLocale);
    }

    public static void main(String[] args) {
    }
}