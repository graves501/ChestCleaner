package io.github.graves501.chestcleanerx.command;

import io.github.graves501.chestcleanerx.configuration.PluginConfig;
import io.github.graves501.chestcleanerx.util.constant.PlayerMessage;
import io.github.graves501.chestcleanerx.util.constant.PluginPermission;
import io.github.graves501.chestcleanerx.util.constant.Property;
import io.github.graves501.chestcleanerx.util.constant.TimerCommandConstant;
import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
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
            InGameMessageHandler
                .sendConsoleMessage(InGameMessageType.ERROR, InGameMessage.NOT_A_PLAYER);
            return true;
        }

        final Player player = (Player) commandSender;
        final PluginConfig pluginConfiguration = PluginConfig.getInstance();

        if (player.hasPermission(PluginPermission.COOLDOWN_TIMER.getString())) {

            if (arguments.length == 2) {

                /* SETACTIVE SUBCOMMAND */
                if (arguments[0].equalsIgnoreCase(timerCommands.get(0))) {

                    if (arguments[1].equalsIgnoreCase(TimerCommandConstant.TRUE.getString())) {

                        if (!pluginConfiguration.isCooldownTimerActive()) {
                            pluginConfiguration
                                .setAndSaveBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE, true);
                        }
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                InGameMessage.COOLDOWN_TIMER_ACTIVATE
                            );
                        return true;

                    } else if (arguments[1]
                        .equalsIgnoreCase(TimerCommandConstant.FALSE.getString())) {

                        if (!pluginConfiguration.isCooldownTimerActive()) {
                            pluginConfiguration
                                .setAndSaveBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE, false);
                        }
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                InGameMessage.COOLDOWN_TIMER_INACTIVE
                            );
                        return true;

                    } else {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                                PlayerMessage.TIMER_SYNTAX_ERROR.getString());
                        return true;
                    }

                    /* SETTIMER SUBCOMMAND */
                } else if (arguments[0].equalsIgnoreCase(timerCommands.get(1))) {

                    final int cooldownTimeInSeconds = Integer.valueOf(arguments[1]);

                    if (pluginConfiguration.getCooldownTimeInSeconds() != cooldownTimeInSeconds) {
                        pluginConfiguration.setAndSaveCooldownTime(cooldownTimeInSeconds);
                    }
                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.COOLDOWN_TIMER_NEW_TIME_SET,
                        String.valueOf(cooldownTimeInSeconds));
//                        Messages
//                            .getMessage(InGameMessage.COOLDOWN_TIMER_NEW_TIME_SET,
//                                TimerCommandConstant.TIME_TARGET.getString(),
//                                String.valueOf(
//                                    cooldownTimeInSeconds))
//                    );
                    return true;

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                            PlayerMessage.TIMER_SYNTAX_ERROR.getString()
                        );
                    return true;
                }

            } else {
                InGameMessageHandler
                    .sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                        PlayerMessage.TIMER_SYNTAX_ERROR.getString()
                    );
                return true;
            }

        } else {
            InGameMessageHandler
                .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                    PluginPermission.COOLDOWN_TIMER.getString()
                );
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
