package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.config.PlayerConfiguration;
import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import io.github.graves501.chestcleanerx.utils.enums.Property;
import io.github.graves501.chestcleanerx.utils.enums.SortingConfigConstant;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class SortingConfigCommand implements CommandExecutor, TabCompleter {

    private final List<String> commandList = new ArrayList<>();
    private final List<String> booleans = new ArrayList<>();
    private final List<String> adminControlSubCommand = new ArrayList<>();

    public SortingConfigCommand() {

        commandList.add(SortingConfigConstant.SORTING_PATTERN.getString());
        commandList.add(SortingConfigConstant.ITEM_EVALUATOR.getString());
        commandList.add(SortingConfigConstant.SET_AUTO_SORT_CHEST_ACTIVE.getString());
        commandList.add(SortingConfigConstant.ADMIN_CONFIGURATION.getString());

        booleans.add(SortingConfigConstant.TRUE.getString());
        booleans.add(SortingConfigConstant.FALSE.getString());

        adminControlSubCommand.add(SortingConfigConstant.SET_DEFAULT_SORTING_PATTERN.getString());
        adminControlSubCommand.add(SortingConfigConstant.SET_DEFAULT_ITEM_EVALUATOR.getString());
        adminControlSubCommand
            .add(SortingConfigConstant.SET_DEFAULT_AUTO_SORT_CHEST_ACTIVE.getString());
    }

    @Override
    public boolean onCommand(final CommandSender commandSender,
        final Command command,
        final String label,
        String[] arguments) {

        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();
        final PlayerConfiguration playerConfiguration = PlayerConfiguration.getInstance();

        if (arguments.length == 2) {

            /* PATTERN */
            if (arguments[0].equalsIgnoreCase(commandList.get(0))) {

                if (!player.hasPermission("chestcleaner.cmd.sortingConfig.pattern")) {
                    MessageSystem.sendConsoleMessage(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.pattern");
                    return true;
                }

                SortingPattern pattern = SortingPattern.getSortingPatternByName(arguments[1]);

                if (pattern == null) {

                    MessageSystem
                        .sendMessageToPlayer(MessageType.ERROR, MessageID.NO_PATTERN_ID, player);
                    return true;

                } else {

                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.NEW_PATTERN_SET,
                            player);
                    playerConfiguration.setAndSaveSortingPattern(pattern, player);
                    playerConfiguration.loadPlayerData(player);

                    return true;
                }

                /* EVALUATOR */
            } else if (arguments[0].equalsIgnoreCase(commandList.get(1))) {

                if (!player.hasPermission("chestcleaner.cmd.sortingConfig.evaluator")) {
                    MessageSystem.sendConsoleMessage(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.evaluator");
                    return true;
                }

                ItemEvaluatorType evaluator = ItemEvaluatorType
                    .getEvaluatorTypeByName(arguments[1]);

                if (evaluator == null) {

                    MessageSystem
                        .sendMessageToPlayer(MessageType.ERROR, MessageID.NO_EVALUATOR_ID, player);
                    return true;

                } else {

                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.NEW_EVALUATOR_SET,
                            player);
                    playerConfiguration.setAndSaveEvaluatorType(evaluator, player);
                    playerConfiguration.loadPlayerData(player);

                    return true;

                }

                /* SETAUTOSORT */
            } else if (arguments[0].equalsIgnoreCase(commandList.get(2))) {

                if (!player.hasPermission("chestcleaner.cmd.sortingConfig.setautosort")) {
                    MessageSystem.sendConsoleMessage(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.setautosort");
                    return true;
                }

                boolean isAutoSortChestActive = false;
                if (arguments[1].equalsIgnoreCase("true")) {
                    isAutoSortChestActive = true;
                } else if (!arguments[1].equalsIgnoreCase("false")) {
                    MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                        "/sortingConfig setautosort <true/false>", player);
                    return true;
                }

                MessageSystem.sendMessageToPlayer(
                    MessageType.SUCCESS,
                    Messages.getMessage(MessageID.AUTOSORT_WAS_SET, "%boolean", String.valueOf(
                        isAutoSortChestActive)),
                    player);
                playerConfiguration.setAndSaveIsAutoSortChestOnClosingActive(player,
                    isAutoSortChestActive);

                return true;

            } else {
                sendSyntaxErrorMessage(player);
                return true;
            }

        } else if (arguments.length == 3) {

            /* ADMINCONFIG */
            if (arguments[0].equalsIgnoreCase(commandList.get(3))) {

                if (!player.hasPermission("chestcleaner.cmd.sorting.config.admincontrol")) {
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sorting.config.admincontrol", player);
                    return true;
                }

                /* SETDEFAULTPATTERN */
                if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(0))) {

                    SortingPattern sortingPattern = SortingPattern
                        .getSortingPatternByName(arguments[2]);

                    if (sortingPattern == null) {
                        MessageSystem
                            .sendMessageToPlayer(MessageType.ERROR, MessageID.NO_PATTERN_ID,
                                player);
                        return true;
                    }

                    pluginConfiguration
                        .setAndSaveStringProperty(Property.DEFAULT_SORTING_PATTERN, sortingPattern
                            .name());
                    pluginConfiguration.setDefaultSortingPattern(sortingPattern);

                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        MessageID.NEW_DEFAULT_SORTING_PATTERN,
                        player);
                    return true;

                    /* SETDEFAULTEVALUATOR */
                } else if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(1))) {

                    ItemEvaluatorType evaluator = ItemEvaluatorType
                        .getEvaluatorTypeByName(arguments[2]);

                    if (evaluator == null) {
                        MessageSystem
                            .sendMessageToPlayer(MessageType.ERROR, MessageID.NO_EVALUATOR_ID,
                                player);
                        return true;
                    }

                    pluginConfiguration.setAndSaveDefaultEvaluatorType(evaluator);

                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.NEW_DEFAULT_EVALUATOR,
                            player);
                    return true;

                    /* SETDEFAULTAUTOSORTING */
                } else if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(2))) {

                    boolean isAutoSortChestActive = false;

                    if (arguments[2].equalsIgnoreCase("true")) {
                        isAutoSortChestActive = true;
                    } else if (!arguments[2].equalsIgnoreCase("false")) {
                        MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                            "/sortingConfig adminconfig setdefaultautosort <true/false>", player);
                        return true;
                    }

                    pluginConfiguration.setDefaultAutoSortChestActive(isAutoSortChestActive);
                    pluginConfiguration.setAndSaveBooleanProperty(Property.DEFAULT_AUTOSORT_CHEST_ACTIVE,
                        isAutoSortChestActive);

                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        Messages
                            .getMessage(MessageID.DEFAULT_AUTOSORT, "%boolean", String.valueOf(
                                isAutoSortChestActive)),
                        player);
                    return true;

                } else {
                    MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                        "/sortingConfig adminconfig <setdefaultpattern/setdefaultevaluator/setdefaultautosort>",
                        player);
                    return true;
                }

            } else {
                sendSyntaxErrorMessage(player);
                return true;
            }

        } else {
            sendSyntaxErrorMessage(player);
            return true;
        }

    }

    private void sendSyntaxErrorMessage(final Player player) {
        MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
            "/sortingConfig <pattern/evaluator/setautosort/adminconfig>", player);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender,
        final Command command,
        final String label,
        final String[] arguments) {

        final List<String> tabCompletions = new ArrayList<>();

        if (arguments.length <= 1) {

            StringUtil.copyPartialMatches(arguments[0], commandList, tabCompletions);

        } else if (arguments.length == 2) {

            if (arguments[0].equalsIgnoreCase(commandList.get(0))) {
                StringUtil
                    .copyPartialMatches(arguments[1], SortingPattern.getIDList(), tabCompletions);
            } else if (arguments[0].equalsIgnoreCase(commandList.get(1))) {
                StringUtil.copyPartialMatches(arguments[1], ItemEvaluatorType.getIDList(),
                    tabCompletions);
            } else if (arguments[0].equalsIgnoreCase(commandList.get(2))) {
                StringUtil.copyPartialMatches(arguments[1], booleans, tabCompletions);
            } else if (arguments[0].equalsIgnoreCase(commandList.get(3))) {
                StringUtil.copyPartialMatches(arguments[1], adminControlSubCommand, tabCompletions);
            }

        } else if (arguments.length == 3) {
            if (arguments[0].equalsIgnoreCase(commandList.get(3))) {
                if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(0))) {
                    StringUtil
                        .copyPartialMatches(arguments[2], SortingPattern.getIDList(),
                            tabCompletions);
                } else if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(1))) {
                    StringUtil
                        .copyPartialMatches(arguments[2], ItemEvaluatorType.getIDList(),
                            tabCompletions);
                } else if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(2))) {
                    StringUtil.copyPartialMatches(arguments[2], booleans, tabCompletions);
                }
            }
        }

        return tabCompletions;
    }

}
