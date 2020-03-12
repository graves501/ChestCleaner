package io.github.graves501.chestcleanerx.utils.enums;

public enum Property {
    BLOCK_REFILL("blockRefill"),
    CLEAN_INVENTORY_PERMISSION_ACTIVE("cleanInventory.permissionActive"),
    CLEANING_ITEM("cleaningItem"),
    CLEANING_ITEM_ACTIVE("cleaningItem.active"),
    CONSUMABLES_REFILL("consumablesRefill"),
    DEFAULT_SORTING_PATTERN("defaultSortingPattern"),
    DURABILITY_LOSS("durabilityLoss"),
    DEFAULT_AUTOSORT("defaultAutosort"),
    DEFAULT_EVALUATOR("defaultEvaluator"),
    INVENTORY_BLACKLIST("inventoryBlacklist"),
    MESSAGES("messages"),
    TIMER_TIME("timer.time"),
    TIMER_ACTIVE("timer.active"),
    OPEN_INVENTORY_EVENT_MODE("openInventoryEventMode"),
    SORTING_BLACKLIST("sortingBlacklist"),

    PLUGIN_FILE_PATH("plugins/ChestCleanerX"),
    PLAYER_DATA_YAML_CONFIG_FILE_NAME("config.yml"),
    PLUGIN_YAML_CONFIG_FILE_NAME("config.yml");

    private String stringValue;

    Property(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
