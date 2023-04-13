package com.spiralstudio.mod.core;

/**
 * @author Leego Yih
 */
public class Config {
    private String group;
    private String key;
    private String value;
    private Integer version;

    public Config() {
    }

    public Config(String group, String key, String value, Integer version) {
        this.group = group;
        this.key = key;
        this.value = value;
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
