package io.github.graves501.chestcleanerx.utils.messages;

import io.github.graves501.chestcleanerx.main.PluginMain;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
        getConsoleCommandSender().sendMessage(getMessageString(type, arg));
    }

    public static void sendConsoleMessage(MessageType type, MessageID messageID) {
        getConsoleCommandSender()
            .sendMessage(getMessageString(type, Messages.getMessage(messageID)));
    }

    private static ConsoleCommandSender getConsoleCommandSender() {
        return PluginMain.getInstance().getServer().getConsoleSender();
    }


    private static String getMessageString(MessageType type, String arg) {

        String message = "�6[ChestCleanerX] ";

        switch (type) {
            case SYNTAX_ERROR:
                message += "�c" + Messages.getMessage(MessageID.SYNTAX_ERROR) + ": " + arg;
                break;
            case ERROR:
                message += "�c" + Messages.getMessage(MessageID.ERROR) + ": " + arg;
                break;
            case SUCCESS:
                message += "�a" + arg;
                break;
            case MISSING_PERMISSION:
                message +=
                    "�c" + Messages.getMessage(MessageID.PERMISSION_DENIED) + " ( " + arg + " )";
                break;
            case UNHEADED_INFORMATION:
                message = "�7" + arg;
                break;
            default:
                throw new IllegalArgumentException();
        }

        return message;
    }

}
