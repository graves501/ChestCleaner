package io.github.graves501.chestcleanerx.utils.messages;

import io.github.graves501.chestcleanerx.main.PluginMain;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class InGameMessageHandler {

    private InGameMessageHandler() {
    }

    public static void sendMessageToPlayer(final Player player,
        final InGameMessageType inGameMessageType,
        final String inGameMessageString) {
        player.sendMessage(getTypedInGameMessage(inGameMessageType, inGameMessageString));
    }

    public static void sendMessageToPlayer(final Player player, final InGameMessageType type,
        final InGameMessage inGameMessage) {
        player.sendMessage(getTypedInGameMessage(type, inGameMessage.get()));
    }

    public static void sendMessageToPlayer(final Player player,
        final InGameMessageType inGameMessageType,
        final InGameMessage inGameMessage, final String replacement) {

        final String messageToBeSent = replacePlaceholder(inGameMessage, replacement);
        player.sendMessage(getTypedInGameMessage(inGameMessageType, messageToBeSent));
    }

    public static void sendConsoleMessage(final InGameMessageType inGameMessageType,
        final String inGameMessageString) {
        getConsoleCommandSender().sendMessage(
            getTypedInGameMessage(inGameMessageType, inGameMessageString));
    }

    public static void sendConsoleMessage(final InGameMessageType inGameMessageType,
        final InGameMessage inGameMessage) {
        getConsoleCommandSender()
            .sendMessage(getTypedInGameMessage(inGameMessageType, inGameMessage.get()));
    }

    public static void sendConsoleMessage(final InGameMessageType type,
        final InGameMessage inGameMessage, final String replacement) {
        final String messageToBeSent = replacePlaceholder(inGameMessage, replacement);
        getConsoleCommandSender().sendMessage(getTypedInGameMessage(type, messageToBeSent));
    }

    private static String replacePlaceholder(final InGameMessage inGameMessage,
        final String replacement) {
        String messageToBeSent = inGameMessage.get();
        return messageToBeSent.replace(InGameMessage.PLACEHOLDER.get(), replacement);
    }

    private static ConsoleCommandSender getConsoleCommandSender() {
        return JavaPlugin.getPlugin(PluginMain.class).getServer().getConsoleSender();
    }

    private static String getTypedInGameMessage(final InGameMessageType inGameMessageType,
        final String inGameMessage) {

        final StringBuilder inGameMessageStringBuilder = new StringBuilder();
        inGameMessageStringBuilder.append(InGameMessage.MESSAGE_PREFIX.get());

        switch (inGameMessageType) {
            case SYNTAX_ERROR:
                inGameMessageStringBuilder.append(ChatColor.RED);
                inGameMessageStringBuilder.append(InGameMessage.SYNTAX_ERROR.get());
                inGameMessageStringBuilder.append(InGameMessage.COLON);
                inGameMessageStringBuilder.append(inGameMessage);
                break;
            case ERROR:
                inGameMessageStringBuilder.append(ChatColor.RED);
                inGameMessageStringBuilder.append(InGameMessage.ERROR.get());
                inGameMessageStringBuilder.append(InGameMessage.COLON);
                inGameMessageStringBuilder.append(inGameMessage);
                break;
            case SUCCESS:
                inGameMessageStringBuilder.append(ChatColor.GREEN);
                inGameMessageStringBuilder.append(inGameMessage);
                break;
            case MISSING_PERMISSION:
                inGameMessageStringBuilder.append(ChatColor.RED);
                inGameMessageStringBuilder.append(InGameMessage.PERMISSION_DENIED.get());
                inGameMessageStringBuilder.append(InGameMessage.PARENTHESIS_LEFT);
                inGameMessageStringBuilder.append(inGameMessage);
                inGameMessageStringBuilder.append(InGameMessage.PARENTHESIS_RIGHT);
                break;
            case UNHEADED_INFORMATION:
                inGameMessageStringBuilder.append(ChatColor.GRAY);
                inGameMessageStringBuilder.append(inGameMessage);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return inGameMessageStringBuilder.toString();
    }

}
