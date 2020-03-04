package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.config.Config;
import io.github.graves501.chestcleanerx.main.Main;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import io.github.graves501.chestcleanerx.utils.stringconstants.PlayerMessages;
import io.github.graves501.chestcleanerx.utils.stringconstants.PluginPermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class TimerCommand implements CommandExecutor, TabCompleter {

    private static class TimerCommandStrings {

        static final String SET_ACTIVE = "setActive";
        static final String SET_TIME = "setTime";
        static final String TRUE = "true";
        static final String FALSE = "false";
        static final String TIME_TARGET = "%time";
    }

    private final List<String> timerCommands = new ArrayList<>();

    public TimerCommand() {
        timerCommands.add(TimerCommandStrings.SET_ACTIVE);
        timerCommands.add(TimerCommandStrings.SET_TIME);
    }

    @Override
    public boolean onCommand(final CommandSender commandSender,
        final Command command,
        final String label,
        final String[] arguments) {

        if (!(commandSender instanceof Player)) {
            MessageSystem.sendConsoleMessage(MessageType.ERROR, MessageID.NOT_A_PLAYER);
            return true;
        }

        Player player = (Player) commandSender;

        if (player.hasPermission(PluginPermissions.TIMER_PERMISSION)) {

            if (arguments.length == 2) {

                /* SETACTIVE SUBCOMMAND */
                if (arguments[0].equalsIgnoreCase(timerCommands.get(0))) {

                    if (arguments[1].equalsIgnoreCase(TimerCommandStrings.TRUE)) {

                        if (!Main.timer) {
                            Config.setTimerPermission(true);
                            Main.timer = true;
                        }
                        MessageSystem
                            .sendMessageToPlayer(MessageType.SUCCESS, MessageID.TIMER_ACTIVATED,
                                player);
                        return true;

                    } else if (arguments[1].equalsIgnoreCase(TimerCommandStrings.FALSE)) {

                        if (Main.timer) {
                            Config.setTimerPermission(false);
                            Main.timer = false;
                        }
                        MessageSystem
                            .sendMessageToPlayer(MessageType.SUCCESS, MessageID.TIMER_DEACTIVATED,
                                player);
                        return true;

                    } else {
                        MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                            PlayerMessages.TIMER_SYNTAX_ERROR, player);
                        return true;
                    }

                    /* SETTIMER SUBCOMMAND */
                } else if (arguments[0].equalsIgnoreCase(timerCommands.get(1))) {

                    int time = Integer.valueOf(arguments[1]);
                    if (Main.time != time) {
                        Main.time = time;
                        Config.setTime(time);
                    }
                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        Messages
                            .getMessage(MessageID.TIMER_NEW_TIME, TimerCommandStrings.TIME_TARGET, String.valueOf(time)),
                        player);
                    return true;

                } else {
                    MessageSystem
                        .sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                            PlayerMessages.TIMER_SYNTAX_ERROR,
                            player);
                    return true;
                }

            } else {
                MessageSystem
                    .sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                        PlayerMessages.TIMER_SYNTAX_ERROR,
                        player);
                return true;
            }

        } else {
            MessageSystem
                .sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                    PluginPermissions.TIMER_PERMISSION,
                    player);
            return true;
        }

    }

    @Override
    public List<String> onTabComplete(final CommandSender sender,
        final Command command,
        final String alias,
        final String[] arguments) {

        final List<String> tabCompletions = new ArrayList<>();
        switch (arguments.length) {
            case 0:
            case 1:
                StringUtil.copyPartialMatches(arguments[0], timerCommands, tabCompletions);
                break;
            case 2:
                /* SETACTIVE */
                if (arguments[0].equalsIgnoreCase(timerCommands.get(0))) {

                    ArrayList<String> commands = new ArrayList<>();
                    commands.add(TimerCommandStrings.TRUE);
                    commands.add(TimerCommandStrings.FALSE);

                    StringUtil.copyPartialMatches(arguments[1], commands, tabCompletions);
                }

        }

        Collections.sort(tabCompletions);
        return tabCompletions;
    }

}
