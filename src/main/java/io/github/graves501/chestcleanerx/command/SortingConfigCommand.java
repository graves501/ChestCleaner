package io.github.graves501.chestcleanerx.command;

import io.github.graves501.chestcleanerx.configuration.PlayerConfiguration;
import io.github.graves501.chestcleanerx.configuration.PluginConfiguration;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import io.github.graves501.chestcleanerx.util.constant.Property;
import io.github.graves501.chestcleanerx.util.constant.SortingConfigConstant;
import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
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
                    InGameMessageHandler.sendConsoleMessage(InGameMessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.pattern");
                    return true;
                }

                SortingPattern pattern = SortingPattern.getSortingPatternByName(arguments[1]);

                if (pattern == null) {

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.ERROR,
                            InGameMessage.NO_PATTERN_ID);
                    return true;

                } else {

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.NEW_PATTERN_SET
                        );
                    playerConfiguration.setAndSaveSortingPattern(pattern, player);
                    playerConfiguration.loadPlayerData(player);

                    return true;
                }

                /* EVALUATOR */
            } else if (arguments[0].equalsIgnoreCase(commandList.get(1))) {

                if (!player.hasPermission("chestcleaner.cmd.sortingConfig.evaluator")) {
                    InGameMessageHandler.sendConsoleMessage(InGameMessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.evaluator");
                    return true;
                }

                ItemEvaluatorType evaluator = ItemEvaluatorType
                    .getEvaluatorTypeByName(arguments[1]);

                if (evaluator == null) {

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.ERROR,
                            InGameMessage.NO_EVALUATOR_ID);
                    return true;

                } else {

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.NEW_EVALUATOR_SET
                        );
                    playerConfiguration.setAndSaveEvaluatorType(evaluator, player);
                    playerConfiguration.loadPlayerData(player);

                    return true;

                }

                /* SETAUTOSORT */
            } else if (arguments[0].equalsIgnoreCase(commandList.get(2))) {

                if (!player.hasPermission("chestcleaner.cmd.sortingConfig.setautosort")) {
                    InGameMessageHandler.sendConsoleMessage(InGameMessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.setautosort");
                    return true;
                }

                boolean isAutoSortChestActive = false;
                if (arguments[1].equalsIgnoreCase("true")) {
                    isAutoSortChestActive = true;
                } else if (!arguments[1].equalsIgnoreCase("false")) {
                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                        "/sortingConfig setautosort <true/false>");
                    return true;
                }

                InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                    InGameMessage.AUTOSORT_WAS_SET, String.valueOf(isAutoSortChestActive));

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
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.sorting.config.admincontrol");
                    return true;
                }

                /* SETDEFAULTPATTERN */
                if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(0))) {

                    SortingPattern sortingPattern = SortingPattern
                        .getSortingPatternByName(arguments[2]);

                    if (sortingPattern == null) {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                InGameMessage.NO_PATTERN_ID
                            );
                        return true;
                    }

                    pluginConfiguration
                        .setAndSaveStringProperty(Property.DEFAULT_SORTING_PATTERN, sortingPattern
                            .name());
                    pluginConfiguration.setDefaultSortingPattern(sortingPattern);

                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.NEW_DEFAULT_SORTING_PATTERN
                    );
                    return true;

                    /* SETDEFAULTEVALUATOR */
                } else if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(1))) {

                    ItemEvaluatorType evaluator = ItemEvaluatorType
                        .getEvaluatorTypeByName(arguments[2]);

                    if (evaluator == null) {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                InGameMessage.NO_EVALUATOR_ID
                            );
                        return true;
                    }

                    pluginConfiguration.setAndSaveDefaultEvaluatorType(evaluator);

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.NEW_DEFAULT_EVALUATOR
                        );
                    return true;

                    /* SETDEFAULTAUTOSORTING */
                } else if (arguments[1].equalsIgnoreCase(adminControlSubCommand.get(2))) {

                    boolean isAutoSortChestActive = false;

                    if (arguments[2].equalsIgnoreCase("true")) {
                        isAutoSortChestActive = true;
                    } else if (!arguments[2].equalsIgnoreCase("false")) {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                                "/sortingConfig adminconfig setdefaultautosort <true/false>");
                        return true;
                    }

                    pluginConfiguration.setDefaultAutoSortChestActive(isAutoSortChestActive);
                    pluginConfiguration
                        .setAndSaveBooleanProperty(Property.DEFAULT_AUTOSORT_CHEST_ACTIVE,
                            isAutoSortChestActive);

                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.DEFAULT_AUTOSORT_CHEST_ACTIVE,
                        String.valueOf(isAutoSortChestActive));
                    return true;

                } else {
                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                        "/sortingConfig adminconfig <setdefaultpattern/setdefaultevaluator/setdefaultautosort>"
                    );
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
        InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
            "/sortingConfig <pattern/evaluator/setautosort/adminconfig>");
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
