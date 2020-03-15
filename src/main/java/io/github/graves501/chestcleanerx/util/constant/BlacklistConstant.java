package io.github.graves501.chestcleanerx.util.constant;

public enum BlacklistConstant {
    SORTING("sorting"),
    INVENTORIES("inventories"),

    ADD_MATERIAL("addMaterial"),
    REMOVE_MATERIAL("removeMaterial"),
    LIST("list"),
    CLEAR("clear");

    private String stringValue;

    BlacklistConstant(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
