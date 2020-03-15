package io.github.graves501.chestcleanerx.util.constant;

public enum PlayerProperty {
    SORTING_PATTERN(".sortingPattern"),
    EVALUATOR_TYPE(".evaluatorType"),
    AUTO_SORT_CHEST(".autoSortChest");

    private String stringValue;

    PlayerProperty(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;

    }
}
