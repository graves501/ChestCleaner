package io.github.graves501.chestcleanerx.util.constant;

public enum Property {
    BLOCK_REFILL("isBlockRefillActive"),
    CLEAN_INVENTORY_COMMAND_ACTIVE("isCleanInventoryCommandActive"),
    CLEANING_ITEM("cleaningItem"),
    CLEANING_ITEM_ACTIVE("isCleaningItemActive"),
    CONSUMABLES_REFILL("isConsumablesRefillActive"),
    DEFAULT_SORTING_PATTERN("defaultSortingPattern"),
    DURABILITY_LOSS("isDurabilityLossActive"),
    AUTOSORT_CHEST_ACTIVE("isAutoSortChestActive"),
    DEFAULT_ITEM_EVALUATOR("defaultItemEvaluator"),
    INVENTORY_BLACKLIST("inventoryBlacklist"),
    COOLDOWN_IN_SECONDS("cooldownInSeconds"),
    COOLDOWN_ACTIVE("isCooldownActive"),
    OPEN_INVENTORY_EVENT_DETECTION_MODE("openInventoryEventDetectionMode"),
    SORTING_BLACKLIST("sortingBlacklist"),

    PLUGIN_FILE_PATH("plugins/ChestCleanerX"),

    //TODO Get distinct name for player config
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
