package io.github.graves501.chestcleanerx.util.constant;

public enum SortingConfigConstant {
    SORTING_PATTERN("sortingPattern"),
    ITEM_EVALUATOR("itemEvaluator"),
    SET_AUTO_SORT_CHEST_ACTIVE("setAutoSortChestActive"),
    ADMIN_CONFIGURATION("adminConfiguration"),

    TRUE("true"),
    FALSE("false"),

    SET_DEFAULT_AUTO_SORT_CHEST_ACTIVE("setDefaultAutoSortChestActive"),
    SET_DEFAULT_SORTING_PATTERN("setDefaultSortingPattern"),
    SET_DEFAULT_ITEM_EVALUATOR("setDefaultItemEvaluator");

    private String stringValue;

    SortingConfigConstant(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
