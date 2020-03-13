package io.github.graves501.chestcleanerx.utils.enums;

public enum PlayerCommand {
    CLEAN_INVENTORY("cleanInventory"),
    TIMER("timer"),
    CLEANING_ITEM("cleaningItem"),
    BLACKLIST("blacklist"),
    SORTING_CONFIG("sortingConfig");

    private String stringValue;

    PlayerCommand(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
