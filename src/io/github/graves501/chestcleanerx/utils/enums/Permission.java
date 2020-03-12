package io.github.graves501.chestcleanerx.utils.enums;

public enum Permission {
    CLEAN_INVENTORY_PERMISSION("chestcleaner.command.cleanInventory"),
    BLACKLIST_PERMISSION("chestcleaner.command.blacklist"),
    COOLDOWN_TIMER_PERMISSION("chestcleaner.command.cooldownTimer");

    private String stringValue;

    Permission(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
