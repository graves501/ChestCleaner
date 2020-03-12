package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.playerdata.PlayerData;
import io.github.graves501.chestcleanerx.playerdata.PlayerDataManager;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import io.github.graves501.chestcleanerx.utils.enums.Property;
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
    private final List<String> admincontrolSubCMD = new ArrayList<>();

    public SortingConfigCommand() {

        commandList.add("pattern");
        commandList.add("evaluator");
        commandList.add("setautosort");
        commandList.add("adminconfig");

        booleans.add("true");
        booleans.add("false");

        admincontrolSubCMD.add("setdefaultpattern");
        admincontrolSubCMD.add("setdefaultevaluator");
        admincontrolSubCMD.add("setdefaultautosort");

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
                    PlayerData.setSortingPattern(pattern, player);
                    PlayerDataManager.loadPlayerData(player);
                    return true;

                }

                /* EVALUATOR */
            } else if (arguments[0].equalsIgnoreCase(commandList.get(1))) {

                if (!player.hasPermission("chestcleaner.cmd.sortingConfig.evaluator")) {
                    MessageSystem.sendConsoleMessage(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.evaluator");
                    return true;
                }

                EvaluatorType evaluator = EvaluatorType.getEvaluatorTypeByName(arguments[1]);

                if (evaluator == null) {

                    MessageSystem
                        .sendMessageToPlayer(MessageType.ERROR, MessageID.NO_EVALUATOR_ID, player);
                    return true;

                } else {

                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.NEW_EVALUATOR_SET,
                            player);
                    PlayerData.setEvaluatorTyp(evaluator, player);
                    PlayerDataManager.loadPlayerData(player);
                    return true;

                }

                /* SETAUTOSORT */
            } else if (arguments[0].equalsIgnoreCase(commandList.get(2))) {

                if (!player.hasPermission("chestcleaner.cmd.sortingConfig.setautosort")) {
                    MessageSystem.sendConsoleMessage(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.sortingConfig.setautosort");
                    return true;
                }

                boolean b = false;
                if (arguments[1].equalsIgnoreCase("true")) {
                    b = true;
                } else if (!arguments[1].equalsIgnoreCase("false")) {
                    MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                        "/sortingConfig setautosort <true/false>", player);
                    return true;
                }

                MessageSystem.sendMessageToPlayer(
                    MessageType.SUCCESS,
                    Messages.getMessage(MessageID.AUTOSORT_WAS_SET, "%boolean", String.valueOf(b)),
                    player);
                PlayerData.setAutoSort(b, player);
                PlayerDataManager.loadPlayerData(player);
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
                if (arguments[1].equalsIgnoreCase(admincontrolSubCMD.get(0))) {

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
                    SortingPattern.defaultSortingPattern = sortingPattern;
                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        MessageID.NEW_DEFAULT_SORTING_PATTERN,
                        player);
                    return true;

                    /* SETDEFAULTEVALUATOR */
                } else if (arguments[1].equalsIgnoreCase(admincontrolSubCMD.get(1))) {

                    EvaluatorType evaluator = EvaluatorType.getEvaluatorTypeByName(arguments[2]);

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
                } else if (arguments[1].equalsIgnoreCase(admincontrolSubCMD.get(2))) {

                    boolean defaultAutoSorting = false;

                    if (arguments[2].equalsIgnoreCase("true")) {
                        defaultAutoSorting = true;
                    } else if (!arguments[2].equalsIgnoreCase("false")) {
                        MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                            "/sortingConfig adminconfig setdefaultautosort <true/false>", player);
                        return true;
                    }

                    PlayerDataManager.setDefaultAutoSorting(defaultAutoSorting);

                    pluginConfiguration.setAndSaveBooleanProperty(Property.DEFAULT_AUTOSORTING,
                        defaultAutoSorting);

                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        Messages
                            .getMessage(MessageID.DEFAULT_AUTOSORT, "%boolean", String.valueOf(
                                defaultAutoSorting)),
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
                StringUtil.copyPartialMatches(arguments[1], EvaluatorType.getIDList(),
                    tabCompletions);
            } else if (arguments[0].equalsIgnoreCase(commandList.get(2))) {
                StringUtil.copyPartialMatches(arguments[1], booleans, tabCompletions);
            } else if (arguments[0].equalsIgnoreCase(commandList.get(3))) {
                StringUtil.copyPartialMatches(arguments[1], admincontrolSubCMD, tabCompletions);
            }

        } else if (arguments.length == 3) {
            if (arguments[0].equalsIgnoreCase(commandList.get(3))) {
                if (arguments[1].equalsIgnoreCase(admincontrolSubCMD.get(0))) {
                    StringUtil
                        .copyPartialMatches(arguments[2], SortingPattern.getIDList(),
                            tabCompletions);
                } else if (arguments[1].equalsIgnoreCase(admincontrolSubCMD.get(1))) {
                    StringUtil
                        .copyPartialMatches(arguments[2], EvaluatorType.getIDList(),
                            tabCompletions);
                } else if (arguments[1].equalsIgnoreCase(admincontrolSubCMD.get(2))) {
                    StringUtil.copyPartialMatches(arguments[2], booleans, tabCompletions);
                }
            }
        }

        return tabCompletions;

    }

}
