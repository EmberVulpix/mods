package com.spiralstudio.mod.demo;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.ConstructorBuilder;
import com.spiralstudio.mod.core.ConstructorModifier;
import com.spiralstudio.mod.core.FieldBuilder;
import com.spiralstudio.mod.core.FieldModifier;
import com.spiralstudio.mod.core.MethodBuilder;
import com.spiralstudio.mod.core.MethodModifier;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.StaticConstructorBuilder;

import java.lang.reflect.Modifier;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.client.ProjectXApp
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
        redefineClass();
        addCommand();
    }

    static void redefineClass() {
        ClassPool.from("com.threerings.projectx.client.ProjectXApp")
                // Add a constructor
                .addConstructor(new ConstructorBuilder()
                        .modifiers(Modifier.PUBLIC)
                        .parameters("java.lang.String")
                        .body("{\n" +
                                "    System.out.println($1);\n" +
                                "}"))
                // Modify the constructor
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("java.lang.String", "java.lang.String", "boolean", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String")
                        .body("{\n" +
                                "    System.out.println(\"Body\");\n" +
                                "}")
                        .insertBefore("System.out.println(\"Before\");")
                        .insertAfter("System.out.println(\"After\");"))
                // Add a static constructor
                .setStaticConstructor(new StaticConstructorBuilder()
                        .body("{\n" +
                                "    System.out.println(\"Static Block\");\n" +
                                "}"))
                // Add a field
                .addField(new FieldBuilder()
                        .fieldName("name")
                        .typeName("java.lang.String")
                        .modifiers(Modifier.PUBLIC))
                // Add a static field
                .addField(new FieldBuilder()
                        .fieldName("status")
                        .typeName("int")
                        .modifiers(Modifier.PUBLIC | Modifier.STATIC))
                // Modify the field
                .modifyField(new FieldModifier()
                        .fieldName("id")
                        .modifiers(Modifier.PUBLIC | Modifier.VOLATILE))
                // Add a method
                .addMethod(new MethodBuilder()
                        .body("public boolean test(java.lang.Object o) {\n" +
                                "    return o != null;\n" +
                                "}"))
                // Modify the method
                .modifyMethod(new MethodModifier()
                        .modifiers(Modifier.PUBLIC)
                        .methodName("test")
                        .paramTypeNames("java.lang.Object")
                        .body("{\n" +
                                "    System.out.println(\"Body\");\n" +
                                "}")
                        .insertBefore("System.out.println(\"Before\");")
                        .insertAfter("System.out.println(\"After\");"));
    }

    static void addCommand() {
        Commands.addCommand("hello", "System.out.println(\"Hello, World!\");");
    }

    public static void main(String[] args) {
    }
}
