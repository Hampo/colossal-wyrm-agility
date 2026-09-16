package org.zhbot.colossal_wyrm_agility;

public enum DurationMode {
    TICKS("Ticks"),
    SECONDS("Seconds");

    private final String name;

    DurationMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
