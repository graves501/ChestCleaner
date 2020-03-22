package io.github.graves501.chestcleanerx.util.constant;

public enum PluginPermission {
    BLACKLIST("chestcleaner.command.blacklist"),
    CLEAN_INVENTORY("chestcleaner.command.cleanInventory"),
    COOLDOWN_TIMER("chestcleaner.command.cooldown"),
    TIMER_NO_EFFECT("chestcleaner.timer.noeffect");

    private String stringValue;

    PluginPermission(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
