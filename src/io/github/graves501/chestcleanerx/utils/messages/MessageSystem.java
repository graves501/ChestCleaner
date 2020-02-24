package io.github.graves501.chestcleanerx.utils.messages;

import org.bukkit.entity.Player;

import io.github.graves501.chestcleanerx.main.Main;

public class MessageSystem {

    private MessageSystem() {
    }

    public static void sendMessageToPlayer(MessageType type, String arg, Player player) {
        player.sendMessage(getMessageString(type, arg));
    }

    public static void sendMessageToPlayer(MessageType type, MessageID messageID, Player player) {
        player.sendMessage(getMessageString(type, Messages.getMessage(messageID)));
    }

    public static void sendConsoleMessage(MessageType type, String arg) {
        Main.main.getServer().getConsoleSender().sendMessage(getMessageString(type, arg));
    }

    public static void sendConsoleMessage(MessageType type, MessageID messageID) {
        Main.main.getServer().getConsoleSender()
            .sendMessage(getMessageString(type, Messages.getMessage(messageID)));
    }

    private static String getMessageString(MessageType type, String arg) {

        String out = "�6[ChestCleanerX] ";

        switch (type) {
            case SYNTAX_ERROR:
                out += "�c" + Messages.getMessage(MessageID.SYNTAX_ERROR) + ": " + arg;
                break;
            case ERROR:
                out += "�c" + Messages.getMessage(MessageID.ERROR) + ": " + arg;
                break;
            case SUCCESS:
                out += "�a" + arg;
                break;
            case MISSING_PERMISSION:
                out +=
                    "�c" + Messages.getMessage(MessageID.PERMISSION_DENIED) + " ( " + arg + " )";
                break;
            case UNHEADED_INFORMATION:
                out = "�7" + arg;
                break;
            default:
                throw new IllegalArgumentException();
        }

        return out;

    }

}
