package io.github.graves501.chestcleanerx.util.constant;

public enum PlayerMessage {
    BLACKLIST_SYNTAX_ERROR("/blacklist <sorting/inventory> <addMaterial/removeMaterial/list>"),
    TIMER_SYNTAX_ERROR("/timer <setActive/setTime>");

    private String stringValue;

    PlayerMessage(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getString() {
        return stringValue;
    }
}
