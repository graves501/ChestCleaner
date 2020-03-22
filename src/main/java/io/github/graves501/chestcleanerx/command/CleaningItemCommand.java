package io.github.graves501.chestcleanerx.command;

import io.github.graves501.chestcleanerx.configuration.PluginConfig;
import io.github.graves501.chestcleanerx.util.constant.Property;
import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

/**
 * @author tom2208
 */
public class CleaningItemCommand implements CommandExecutor, TabCompleter {

    private final List<String> cleaningItemCommands = new ArrayList<>();

    public CleaningItemCommand() {

        // possible commands for the first argument of this command
        cleaningItemCommands.add("rename");
        cleaningItemCommands.add("setLore");
        cleaningItemCommands.add("setItem");
        cleaningItemCommands.add("get");
        cleaningItemCommands.add("setActive");
        cleaningItemCommands.add("setDurabilityLoss");
        cleaningItemCommands.add("give");
        cleaningItemCommands.add("setEventDetectionMode");

    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String alias,
        final String[] arguments) {

        boolean isPlayer = sender instanceof Player;
        Player player = (Player) sender;

        final PluginConfig pluginConfiguration = PluginConfig.getInstance();

        if (arguments.length > 1) {

            /* RENAME SUBCOMMAND */
            if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(0))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.rename") || !isPlayer) {

                    String newCleaningItemName = new String();
                    for (int i = 1; i < arguments.length; i++) {

                        if (i == 1) {
                            newCleaningItemName = arguments[1];
                        } else {
                            newCleaningItemName = newCleaningItemName + " " + arguments[i];
                        }

                    }

                    //TODO find out what this line actually does
                    newCleaningItemName = newCleaningItemName.replace("&", "�");

                    if (isPlayer) {
                        InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.NEW_CLEANING_ITEM_NAME, newCleaningItemName);
                    } else {
                        InGameMessageHandler.sendConsoleMessage(InGameMessageType.SUCCESS,
                            InGameMessage.NEW_CLEANING_ITEM_NAME, newCleaningItemName);
                    }

                    final ItemStack newCleaningItem = pluginConfiguration.getCurrentCleaningItem();

                    final ItemMeta currentCleaningItemMeta = newCleaningItem.getItemMeta();
                    currentCleaningItemMeta.setDisplayName(newCleaningItemName);

                    newCleaningItem.setItemMeta(currentCleaningItemMeta);

                    pluginConfiguration.setCurrentCleaningItem(newCleaningItem);

                    if (arguments.length == 1) {
                        return true;
                    }

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.rename");
                    return true;
                }
                return true;

                /* SETLORE SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(1))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.setlore") || !isPlayer) {

                    String lore = arguments[1];
                    for (int i = 2; i < arguments.length; i++) {
                        lore = lore + " " + arguments[i];
                    }

                    String[] lorearray = lore.split("/n");

                    ArrayList<String> lorelist = new ArrayList<>();

                    for (String obj : lorearray) {
                        obj = obj.replace("&", "�");
                        lorelist.add(obj);

                    }

                    ItemMeta itemMeta = pluginConfiguration.getCurrentCleaningItem()
                        .getItemMeta();
                    itemMeta.setLore(lorelist);

                    final ItemStack currentCleaningItem = pluginConfiguration
                        .getCurrentCleaningItem();
                    currentCleaningItem.setItemMeta(itemMeta);

                    pluginConfiguration.setCurrentCleaningItem(currentCleaningItem);
                    pluginConfiguration.setAndSaveCleaningItem(currentCleaningItem);

                    if (isPlayer) {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                InGameMessage.NEW_CLEANING_ITEM_LORE
                            );
                    } else {
                        InGameMessageHandler
                            .sendConsoleMessage(InGameMessageType.SUCCESS,
                                InGameMessage.NEW_CLEANING_ITEM_LORE);
                    }
                    return true;

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.setlore");
                    return true;
                }

            }

        }

        if (arguments.length == 1) {

            /* RENAME SUBCOMMAND ERRORS */
            if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(0))) {
                if (isPlayer) {
                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                        "/cleaningItem rename <name>");
                } else {
                    InGameMessageHandler.sendConsoleMessage(InGameMessageType.SYNTAX_ERROR,
                        "/cleaningItem rename <name>");
                }
                return true;
            }

            /* SETITEM SUBCOMMAND */
            else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(2)) && isPlayer) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.setitem")) {

                    final ItemStack cleaningItem = player.getInventory().getItemInMainHand()
                        .clone();

                    if (cleaningItem != null) {
                        cleaningItem.setDurability((short) 0);
                        cleaningItem.setAmount(1);

                        pluginConfiguration.setCurrentCleaningItem(cleaningItem);

                        InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.NEW_CLEANING_ITEM_SET, cleaningItem.toString());
                        return true;

                    } else {
                        InGameMessageHandler
                            .sendMessageToPlayer(player,
                                InGameMessageType.ERROR, InGameMessage.ITEM_IN_HAND_REQUIRED);
                        return true;
                    }
                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.setitem");
                    return true;
                }

                /* GET SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(3)) && isPlayer) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.get")) {

                    player.getInventory()
                        .addItem(PluginConfig.getInstance().getCurrentCleaningItem());

                    InGameMessageHandler
                        .sendMessageToPlayer(player,
                            InGameMessageType.SUCCESS, InGameMessage.RECEIVED_CLEANING_ITEM);
                    return true;

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.get");
                    return true;
                }

            }

        } else if (arguments.length == 2) {

            /* SETACTIVE SUBCOMMAND */
            if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(4))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.setactive") || !isPlayer) {

                    if (arguments[1].equalsIgnoreCase("true") || arguments[1]
                        .equalsIgnoreCase("false")) {

                        boolean isCleaningItemActive = false;
                        if (arguments[1].equalsIgnoreCase("true")) {
                            isCleaningItemActive = true;
                        }

                        pluginConfiguration
                            .setAndSaveBooleanProperty(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE,
                                isCleaningItemActive);

                        if (isCleaningItemActive) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                    InGameMessage.CLEANING_ITEM_ACTIVE
                                );
                        } else {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                    InGameMessage.CLEANING_ITEM_INACTIVE);
                        }
                        return true;

                    } else {
                        if (isPlayer) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                                    "/cleaningItem setactive <true/false>");
                        } else {
                            InGameMessageHandler.sendConsoleMessage(InGameMessageType.SYNTAX_ERROR,
                                "/cleaningItem setactive <true/false>");
                        }
                        return true;
                    }

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.setactive");
                    return true;
                }

                /* SETDURIBILITYLOSS SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(5))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.setdurabilityloss")
                    || !isPlayer) {

                    if (arguments[1].equalsIgnoreCase("true") || arguments[1]
                        .equalsIgnoreCase("false")) {

                        boolean durabilityLoss = false;
                        if (arguments[1].equalsIgnoreCase("true")) {
                            durabilityLoss = true;
                        }

                        pluginConfiguration.setDurabilityLossActive(durabilityLoss);

                        if (pluginConfiguration.isDurabilityLossActive()) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                    InGameMessage.DURABILITYLOSS_ACTIVE, InGameMessage.TRUE.get()
                                );
                        } else {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                    InGameMessage.DURABILITYLOSS_ACTIVE, InGameMessage.FALSE.get()
                                );
                        }
                        return true;

                    } else {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                                "/cleaningItem setactive <true/false>");
                        return true;
                    }

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.durabilityloss");
                    return true;
                }

                /* GIVE SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(6))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.give") || !isPlayer) {

                    Player otherPlayer = Bukkit.getPlayer(arguments[1]);

                    if (otherPlayer != null) {
                        otherPlayer.getInventory()
                            .addItem(pluginConfiguration.getCurrentCleaningItem());
                        if (isPlayer) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                    InGameMessage.PLAYER_RECEIVED_CLEANING_ITEM,
                                    otherPlayer.getName());
                        } else {
                            InGameMessageHandler.sendConsoleMessage(InGameMessageType.SUCCESS,
                                InGameMessage.PLAYER_RECEIVED_CLEANING_ITEM, player.getName());
                        }
                        return true;

                    } else {

                        if (arguments[1].equalsIgnoreCase("@a")) {

                            // TODO iterate via collection
                            Object[] onlinePlayers = Bukkit.getOnlinePlayers().toArray();

                            for (Object onlinePlayer : onlinePlayers) {
                                Player tempPlayer = (Player) onlinePlayer;
                                tempPlayer.getInventory()
                                    .addItem(pluginConfiguration.getCurrentCleaningItem());
                                if (isPlayer) {
                                    InGameMessageHandler
                                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                            InGameMessage.PLAYER_IS_NOT_ONLINE, tempPlayer
                                                .getName());
                                }
                            }
                            return true;
                        }

                        if (isPlayer) {
                            InGameMessageHandler
                                .sendMessageToPlayer(otherPlayer, InGameMessageType.ERROR,
                                    InGameMessage.PLAYER_IS_NOT_ONLINE, arguments[1]);
                        } else {
                            InGameMessageHandler.sendConsoleMessage(InGameMessageType.ERROR,
                                InGameMessage.PLAYER_IS_NOT_ONLINE, arguments[1]);
                        }
                        return true;
                    }

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.give");
                    return true;
                }

                /* SETEVENTDETECTIONMODE SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(7))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.seteventdetectionmode")) {

                    boolean eventDetectionModeActive = Boolean.parseBoolean(arguments[1]);
                    pluginConfiguration
                        .setOpenInventoryEventDetectionModeActive(eventDetectionModeActive);

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.SET_INVENTORY_DETECTION_MODE,
                            String.valueOf(eventDetectionModeActive));

                    pluginConfiguration
                        .setAndSaveBooleanProperty(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE,
                            eventDetectionModeActive);
                    return true;

                } else {
                    if (isPlayer) {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                                "chestcleaner.cmd.cleaningItem.seteventdetectionmode");
                    }
                    return true;
                }

            } else {

                if (isPlayer) {
                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                        "/cleaningItem <setitem/setactive/setdurabilityLoss/get/give/rename/setlore/seteventdetectionmode>"
                    );
                } else {
                    InGameMessageHandler.sendConsoleMessage(InGameMessageType.SYNTAX_ERROR,
                        "/cleaningItem <setactive/setdurabilityloss/give/rename/setlore>");
                }
                return true;
            }

        } else {
            if (isPlayer) {
                InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                    "/cleaningItem <setitem/setactive/setdurabilityLoss/get/give/rename/setlore/seteventdetectionmode>"
                );
            } else {
                InGameMessageHandler.sendConsoleMessage(InGameMessageType.SYNTAX_ERROR,
                    "/cleaningItem <setactive/setdurabilityloss/give/rename/setlore>");
            }
            return true;
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(final CommandSender sender,
        final Command command,
        final String alias,
        final String[] args) {

        final List<String> tabCompletions = new ArrayList<>();
        switch (args.length) {
            case 0:
                StringUtil.copyPartialMatches(args[0], cleaningItemCommands, tabCompletions);
                break;
            case 1:
                StringUtil.copyPartialMatches(args[0], cleaningItemCommands, tabCompletions);
                break;
            case 2:
                /* SETACTIVE || SETDURABILITYLOSS || SETEVENTDETECTIONMODE */
                if (args[0].equalsIgnoreCase(cleaningItemCommands.get(4))
                    || args[0].equalsIgnoreCase(cleaningItemCommands.get(5))
                    || args[0].equalsIgnoreCase(cleaningItemCommands.get(7))) {

                    ArrayList<String> commands = new ArrayList<>();
                    commands.add("true");
                    commands.add("false");

                    StringUtil.copyPartialMatches(args[1], commands, tabCompletions);
                }

        }

        Collections.sort(tabCompletions);
        return tabCompletions;
    }

}
