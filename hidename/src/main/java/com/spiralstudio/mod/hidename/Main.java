package com.spiralstudio.mod.hidename;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.FieldBuilder;
import com.spiralstudio.mod.core.MethodModifier;
import com.spiralstudio.mod.core.Registers;

import java.lang.reflect.Modifier;

/**
 * @author Leego Yih
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
        redefineConversationDialog();
        addAutoAdvCommands();
    }

    static void redefineConversationDialog() {
        ClassPool.from("com.threerings.util.Name")
                .addField(new FieldBuilder()
                        .fieldName("_variableName")
                        .typeName("java.lang.String")
                        .modifiers(Modifier.PUBLIC | Modifier.STATIC))
                .modifyMethod(new MethodModifier()
                        .methodName("toString")
                        .body("{\n" +
                                "java.lang.Object _ve = com.threerings.util.Name.class.getDeclaredField(\"_variableName\").get(null);\n" +
                                "if (_ve == null) {\n" +
                                "    return this._name;\n" +
                                "} else {\n" +
                                "    return \"FuckGH\";\n" +
                                "}\n" +
                                "}"));
        /*ClassPool.from("com.threerings.projectx.data.PlayerObject")
                .modifyMethod(new MethodModifier()
                        .methodName("iw")
                        .body("{\n" +
                                "        return new com.threerings.projectx.util.GameMasterName(\"FuckGH\");\n" +
                                "    }"));*/
    }

    static void addAutoAdvCommands() {
        // Add a command "/autoadvon"
        Commands.addCommand("nameset", "" +
                "System.out.println(\"Auto Advance On\");\n" +
                "java.lang.Class.forName(\"com.threerings.util.Name\").getDeclaredField(\"_variableName\").set(null, \"sb\");\n");
        // Add a command "/autoadvoff"
        Commands.addCommand("nameclear", "" +
                "System.out.println(\"Auto Advance Off\");\n" +
                "java.lang.Class.forName(\"com.threerings.util.Name\").getDeclaredField(\"_variableName\").set(null, null);\n");
    }

    public static void main(String[] args) {
    }
}
