package com.spiralstudio.mod.core;

import lombok.Data;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enter "/xhelp" to learn more about each command.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.chat.ProjectXChatDirector
 * @see com.threerings.crowd.chat.client.a ChatDirector
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 */
public final class Commands {
    private static final Map<String, FieldBuilder> fields = new LinkedHashMap<>();
    private static final Map<String, String> commands = new LinkedHashMap<>();

    /**
     * Adds a custom field to {@code ChatDirector}, which can be accessed through {@code this}.
     *
     * @param name the name of the field to be added.
     * @param type the type of the field to be added.
     */
    public static void addField(String name, String type) {
        fields.put(name, new FieldBuilder().fieldName(name).typeName(type).modifiers(Modifier.PUBLIC | Modifier.TRANSIENT));
    }

    /**
     * Adds a custom field to {@code ChatDirector}, which can be accessed through {@code this}.
     *
     * @param name      the name of the field to be added.
     * @param type      the type of the field to be added.
     * @param modifiers the modifiers of the field to be added.
     */
    public static void addField(String name, String type, int modifiers) {
        fields.put(name, new FieldBuilder().fieldName(name).typeName(type).modifiers(modifiers));
    }

    /**
     * Adds a custom command to {@code ChatDirector#requestChat}.
     *
     * @param name the name of the command to be added.
     * @param body the body of the command to be added.
     */
    public static void addCommand(String name, String body) {
        commands.put(name, body);
    }

    /**
     * Adds custom commands to {@code ChatDirector#requestChat}.
     *
     * @param map the commands to be added.
     */
    public static void addCommands(Map<String, String> map) {
        commands.putAll(map);
    }

    public static void init() throws Exception {
        // Do nothing if no fields and commands need to be added
        if (fields.isEmpty() && commands.isEmpty()) {
            return;
        }
        // Read custom configuration
        Config config = Configs.readYaml("cmd.yml", Config.class);
        // Override class `com.threerings.crowd.chat.client.ChatDirector`
        ClassPool.from("com.threerings.crowd.chat.client.a")
                // Add custom fields
                .addFields(fields.entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toList()))
                // Add custom commands, override method `com.threerings.crowd.chat.client.ChatDirector.requestChat`
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .paramTypeNames("com.threerings.crowd.chat.client.m", "java.lang.String", "boolean")
                        .insertBefore(buildCommands(commands, config)));
    }

    static String buildCommands(Map<String, String> commands, Config config) {
        Map<String, String> aliasMap = config != null && config.getAlias() != null ? config.getAlias() : Collections.emptyMap();
        Set<String> disableSet = config != null && config.getDisable() != null ? config.getDisable() : Collections.emptySet();
        // Build commands body
        StringBuilder body = new StringBuilder();
        StringBuilder help = new StringBuilder();
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            List<String> names = new ArrayList<>();
            if (key.indexOf('|') > 0) {
                for (String s : key.split("\\|")) {
                    String name = s.replace("/", "");
                    if (!disableSet.contains(name)) {
                        names.add(name);
                    }
                    String alias = aliasMap.get(name);
                    if (alias != null) {
                        names.add(alias);
                    }
                }
            } else {
                String name = key.replace("/", "");
                if (!disableSet.contains(name)) {
                    names.add(name);
                }
                String alias = aliasMap.get(name);
                if (alias != null) {
                    names.add(alias);
                }
            }
            if (!names.isEmpty()) {
                buildCommand(body, help, names, value);
            }
        }
        // Add '/xhelp'
        body.append("if ($2.equalsIgnoreCase(\"/xhelp\")) { \nreturn \"Available commands: ").append(help).append("\"; }\n");
        System.out.println("[Commands] Available commands: " + help);
        return body.toString();
    }

    static void buildCommand(StringBuilder body, StringBuilder help, List<String> names, String src) {
        int size = names.size();
        if (size == 0) {
            return;
        } else if (size == 1) {
            String name = names.get(0);
            help.append("/").append(name).append(" ");
            body.append("if ($2.equalsIgnoreCase(\"/").append(name).append("\"))");
        } else {
            StringBuilder condition = new StringBuilder();
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i).replace("/", "");
                help.append("/").append(name).append(" ");
                condition.append("$2.equalsIgnoreCase(\"/").append(name).append("\")");
                if (i < names.size() - 1) {
                    condition.append(" || ");
                }
            }
            body.append("if (").append(condition).append(")");
        }
        body.append("{ ").append(src).append(" return \"success\"; }\n");
    }

    @Data
    public static class Config {
        private Map<String, String> alias;
        private Set<String> disable;
    }

    public static void main(String[] args) {
    }
}
