package io.github.graves501.chestcleanerx.util.message;

import org.bukkit.ChatColor;

public enum InGameMessage {

    // Helper
    PLACEHOLDER("{}"),
    COLON(": "),
    PARENTHESIS_LEFT(" ( "),
    PARENTHESIS_RIGHT(" )"),
    DOUBLE_QUOTES("\""),
    PLACEHOLDER_WITH_DOUBLE_QUOTES(DOUBLE_QUOTES.get() + PLACEHOLDER.get() + DOUBLE_QUOTES),
    MESSAGE_SEPARATOR("---------------------------"),

    MESSAGE_PREFIX(ChatColor.RED + "[ChestCleanerX] "),

    TRUE("true"),
    FALSE("false"),

    SYNTAX_ERROR("Syntax Error"),
    ERROR("Error"),
    PERMISSION_DENIED("I'm sorry, but you do not have permission to perform this command."),

    NOT_A_PLAYER("You have to be a player to perform this command"),
    COOLDOWN_TIMER_ACTIVATE("Cooldown timer active: true"),
    COOLDOWN_TIMER_INACTIVE("Cooldown timer active: false"),
    COOLDOWN_TIMER_NEW_TIME_SET(String.format("Cooldown time set to: %s", PLACEHOLDER.get())),
    NEW_CLEANING_ITEM_LORE("Cleaning item lore was set."),
    NEW_CLEANING_ITEM_SET(String.format("%s is now the new cleaning item.", PLACEHOLDER.get())),
    ITEM_IN_HAND_REQUIRED("You have to hold an item in the main hand to do this."),
    RECEIVED_CLEANING_ITEM("You received a cleaning item."),
    CLEANING_ITEM_ACTIVE("Cleaning item active: true"),
    CLEANING_ITEM_INACTIVE("Cleaning item active: false"),
    PLAYER_IS_NOT_ONLINE(String.format("Player %s is not online.", PLACEHOLDER.get())),
    DURABILITYLOSS_ACTIVE(String.format("DurabilityLoss active: %s", PLACEHOLDER.get())),
    BLACKLIST_IS_EMPTY("The blacklist is empty."),
    BLACKLIST_PAGE_INDEX(String.format("The blackList page %s:", PLACEHOLDER.get())),
    NEXT_ENTRIES(String.format("For the next entries: /list %nextpage", PLACEHOLDER.get())),
    INVALID_INPUT_FOR_INTEGER(String.format("Invalid input for an integer: %s", PLACEHOLDER.get())),
    BLACKLIST_CLEARED("The blacklist was successfully cleared."),
    INVENTORY_ON_BLACKLIST("This inventory is on the blacklist, you can't sort it."),
    NO_PATTERN_ID("There is no pattern with this id."),
    NEW_PATTERN_SET("Pattern was set."),
    NO_EVALUATOR_ID("There is no evaluator with this id."),
    NEW_EVALUATOR_SET("Evaluator was set."),
    AUTOSORT_WAS_SET(String.format("Auto sort chest was set to %s.", PLACEHOLDER.get())),
    NEW_DEFAULT_SORTING_PATTERN("New default sorting pattern has been set."),
    NEW_DEFAULT_EVALUATOR("New default item evaluator was set."),

    SORTING_ON_COOLDOWN(
        String.format("You can sort the next inventory in %s seconds.", PLACEHOLDER.get())),

    BLOCK_HAS_NO_INVENTORY(
        String.format("The block at the location %s has no inventory.", PLACEHOLDER.get())),

    INVENTORY_SORTED("Inventory sorted."),

    INVALID_WORLD_NAME(String.format("There is no world with the name %s.",
        PLACEHOLDER_WITH_DOUBLE_QUOTES.get())),

    NEW_CLEANING_ITEM_NAME(String.format("The name of your new cleaning item is now: %s",
        DOUBLE_QUOTES.get() + PLACEHOLDER.get()
            + ChatColor.GREEN + DOUBLE_QUOTES.get())),

    PLAYER_RECEIVED_CLEANING_ITEM(
        String.format("The player %s received a cleaning item.", PLACEHOLDER.get())),

    SET_INVENTORY_DETECTION_MODE(
        String.format("OpenInventoryEvent-DetectionMode was set to: %s", PLACEHOLDER.get())),

    NEW_UPDATE_AVAILABLE("A new update is available at:" + ChatColor.AQUA
        + " https://www.spigotmc.org/resources/40313/updates"),

    ADDED_TO_BLACKLIST(String.format("The material of the item %s was added to the blacklist.",
        PLACEHOLDER_WITH_DOUBLE_QUOTES.get())),

    REMOVED_FROM_BLACKLIST(String.format("The material %s was removed form the blacklist.",
        PLACEHOLDER_WITH_DOUBLE_QUOTES.get())),

    BLACKLIST_DOESNT_CONTAIN(String.format("The blacklist does not contain the material %s.",
        PLACEHOLDER_WITH_DOUBLE_QUOTES.get())),

    INDEX_OUT_OF_BOUNDS(
        String.format("Index is out of bounds, it have to be bigger than -1 and smaller than %s.",
            PLACEHOLDER.get())),

    INVALID_PAGE_NUMBER(
        String.format("Invalid page number (valid  page number range: %s", PLACEHOLDER.get())),

    NO_MATERIAL_FOUND(String
        .format("There is no material with the name %s.", PLACEHOLDER_WITH_DOUBLE_QUOTES.get())),

    IS_ALREADY_ON_BLACKLIST(
        String.format("The material %s is already on the blacklist.", PLACEHOLDER.get())),

    DEFAULT_AUTOSORT_CHEST_ACTIVE(
        String.format("Default auto sort chest was set to %s.", PLACEHOLDER.get()));

    private String stringValue;

    InGameMessage(final String stringValue) {
        this.stringValue = stringValue;
    }

    public String get() {
        return stringValue;
    }
}
