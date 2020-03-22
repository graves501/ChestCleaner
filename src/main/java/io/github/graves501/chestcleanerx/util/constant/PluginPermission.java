package io.github.graves501.chestcleanerx.util.constant;

public enum PluginPermission {
    PREFIX("chestCleanerX."),

    BLACKLIST(PREFIX.getString() + "command.blacklist"),
    CLEAN_INVENTORY(PREFIX.getString() + "command.cleanInventory"),
    COOLDOWN_TIMER(PREFIX.getString() + "command.cooldown"),
    TIMER_NO_EFFECT(PREFIX.getString() + "cooldown.noEffect"),

    SORT_OWN_INVENTORY(PREFIX.getString() + "cleaningItem.sortOwnInventory"),
    USE_CLEANING_ITEM(PREFIX.getString() + "cleaningItem.use"),

    AUTO_REFILL_BLOCKS(PREFIX.getString() + "autoRefill.blocks"),
    AUTO_REFILL_CONSUMABLES(PREFIX.getString() + "autoRefill.consumables");

    private String stringValue;

    PluginPermission(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
