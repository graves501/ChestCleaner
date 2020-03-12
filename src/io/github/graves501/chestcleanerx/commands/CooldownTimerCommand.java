package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.utils.enums.Permission;
import io.github.graves501.chestcleanerx.utils.enums.PlayerMessage;
import io.github.graves501.chestcleanerx.utils.enums.Property;
import io.github.graves501.chestcleanerx.utils.enums.TimerCommandConstant;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CooldownTimerCommand implements CommandExecutor, TabCompleter {

    private final List<String> timerCommands = new ArrayList<>();

    public CooldownTimerCommand() {
        timerCommands.add(TimerCommandConstant.SET_ACTIVE.getString());
        timerCommands.add(TimerCommandConstant.SET_TIME.getString());
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

        final Player player = (Player) commandSender;
        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();

        if (player.hasPermission(Permission.COOLDOWN_TIMER.getString())) {

            if (arguments.length == 2) {

                /* SETACTIVE SUBCOMMAND */
                if (arguments[0].equalsIgnoreCase(timerCommands.get(0))) {

                    if (arguments[1].equalsIgnoreCase(TimerCommandConstant.TRUE.getString())) {

                        if (!pluginConfiguration.isCooldownTimerActive()) {
                            pluginConfiguration
                                .setAndSaveBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE, true);
                        }
                        MessageSystem
                            .sendMessageToPlayer(MessageType.SUCCESS, MessageID.TIMER_ACTIVATED,
                                player);
                        return true;

                    } else if (arguments[1]
                        .equalsIgnoreCase(TimerCommandConstant.FALSE.getString())) {

                        if (!pluginConfiguration.isCooldownTimerActive()) {
                            pluginConfiguration
                                .setAndSaveBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE, false);
                        }
                        MessageSystem
                            .sendMessageToPlayer(MessageType.SUCCESS, MessageID.TIMER_DEACTIVATED,
                                player);
                        return true;

                    } else {
                        MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                            PlayerMessage.TIMER_SYNTAX_ERROR.getString(), player);
                        return true;
                    }

                    /* SETTIMER SUBCOMMAND */
                } else if (arguments[0].equalsIgnoreCase(timerCommands.get(1))) {

                    final int cooldownTimeInSeconds = Integer.valueOf(arguments[1]);

                    if (pluginConfiguration.getCooldownTimeInSeconds() != cooldownTimeInSeconds) {
                        pluginConfiguration.setAndSaveCooldownTime(cooldownTimeInSeconds);
                    }

                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        Messages
                            .getMessage(MessageID.TIMER_NEW_TIME,
                                TimerCommandConstant.TIME_TARGET.getString(),
                                String.valueOf(
                                    cooldownTimeInSeconds)),
                        player);
                    return true;

                } else {
                    MessageSystem
                        .sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                            PlayerMessage.TIMER_SYNTAX_ERROR.getString(),
                            player);
                    return true;
                }

            } else {
                MessageSystem
                    .sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                        PlayerMessage.TIMER_SYNTAX_ERROR.getString(),
                        player);
                return true;
            }

        } else {
            MessageSystem
                .sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                    Permission.COOLDOWN_TIMER.getString(),
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
                    commands.add(TimerCommandConstant.TRUE.getString());
                    commands.add(TimerCommandConstant.FALSE.getString());

                    StringUtil.copyPartialMatches(arguments[1], commands, tabCompletions);
                }

        }

        Collections.sort(tabCompletions);
        return tabCompletions;
    }

}
