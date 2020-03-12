package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
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

        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();

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

                    newCleaningItemName = newCleaningItemName.replace("&", "�");
                    if (isPlayer) {
                        MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                            Messages.getMessage(MessageID.NEW_ITEM_NAME, "%itemname",
                                newCleaningItemName),
                            player);
                    } else {
                        MessageSystem.sendConsoleMessage(MessageType.SUCCESS,
                            Messages.getMessage(MessageID.NEW_ITEM_NAME, "%itemname",
                                newCleaningItemName));
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
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleaningItem.rename", player);
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

                    ItemMeta itemMeta = pluginConfiguration.getInstance().getCurrentCleaningItem()
                        .getItemMeta();
                    itemMeta.setLore(lorelist);

                    final ItemStack currentCleaningItem = pluginConfiguration
                        .getCurrentCleaningItem();
                    currentCleaningItem.setItemMeta(itemMeta);

                    pluginConfiguration.setCurrentCleaningItem(currentCleaningItem);
                    pluginConfiguration.saveConfigurationToFile();

                    if (isPlayer) {
                        MessageSystem
                            .sendMessageToPlayer(MessageType.SUCCESS, MessageID.NEW_ITEM_LORE,
                                player);
                    } else {
                        MessageSystem
                            .sendConsoleMessage(MessageType.SUCCESS, MessageID.NEW_ITEM_LORE);
                    }
                    return true;

                } else {
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleaningItem.setlore", player);
                    return true;
                }

            }

        }

        if (arguments.length == 1) {

            /* RENAME SUBCOMMAND ERRORS */
            if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(0))) {
                if (isPlayer) {
                    MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                        "/cleaningItem rename <name>", player);
                } else {
                    MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR,
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

                        MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                            Messages.getMessage(MessageID.NEW_ITEM, "%newitem",
                                cleaningItem.toString()), player);
                        return true;

                    } else {
                        MessageSystem
                            .sendMessageToPlayer(MessageType.ERROR, MessageID.HOLD_AN_ITEM, player);
                        return true;
                    }
                } else {
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleaningItem.setitem", player);
                    return true;
                }

                /* GET SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(3)) && isPlayer) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.get")) {

                    player.getInventory()
                        .addItem(PluginConfiguration.getInstance().getCurrentCleaningItem());

                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.GOT_ITEM, player);
                    return true;

                } else {
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleaningItem.get", player);
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

                        pluginConfiguration.setCleaningItemActive(isCleaningItemActive);
//                        PluginMain.getInstance().setCleaningItemActive(isCleaningItemActive);

                        if (isCleaningItemActive) {
                            MessageSystem
                                .sendMessageToPlayer(MessageType.SUCCESS, MessageID.ITEM_ACTIVATED,
                                    player);
                        } else {
                            MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                                MessageID.ITEM_DEACTIVATED, player);
                        }
                        return true;

                    } else {
                        if (isPlayer) {
                            MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                                "/cleaningItem setactive <true/false>", player);
                        } else {
                            MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR,
                                "/cleaningItem setactive <true/false>");
                        }
                        return true;
                    }

                } else {
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleaningItem.setactive", player);
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
                            MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                                MessageID.DURABILITYLOSS_ACTIVATED,
                                player);
                        } else {
                            MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                                MessageID.DURABILITYLOSS_DEACTIVATED,
                                player);
                        }
                        return true;

                    } else {
                        MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                            "/cleaningItem setactive <true/false>", player);
                        return true;
                    }

                } else {
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleaningItem.durabilityloss", player);
                    return true;
                }

                /* GIVE SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(6))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.give") || !isPlayer) {

                    Player p2 = Bukkit.getPlayer(arguments[1]);

                    if (p2 != null) {

                        p2.getInventory()
                            .addItem(pluginConfiguration.getCurrentCleaningItem());
                        if (isPlayer) {
                            MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                                Messages.getMessage(MessageID.PLAYER_GOT_ITEM, "%playername",
                                    p2.getName()),
                                player);
                        } else {
                            MessageSystem.sendConsoleMessage(MessageType.SUCCESS,
                                Messages.getMessage(MessageID.PLAYER_GOT_ITEM, "%playername",
                                    player.getName()));
                        }
                        return true;

                    } else {

                        if (arguments[1].equalsIgnoreCase("@a")) {

                            Object[] players = Bukkit.getOnlinePlayers().toArray();

                            for (Object p : players) {
                                Player pl = (Player) p;
                                pl.getInventory()
                                    .addItem(pluginConfiguration.getCurrentCleaningItem());
                                if (isPlayer) {
                                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                                        Messages.getMessage(
                                            MessageID.PLAYER_GOT_ITEM, "%playername", pl.getName()),
                                        player);
                                }
                            }
                            return true;
                        }

                        if (isPlayer) {
                            MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                                Messages.getMessage(MessageID.PLAYER_IS_NOT_ONLINE, "%playername",
                                    arguments[1]), p2);
                        } else {
                            MessageSystem.sendConsoleMessage(MessageType.ERROR,
                                Messages.getMessage(MessageID.PLAYER_IS_NOT_ONLINE, "%playername",
                                    arguments[1]));
                        }
                        return true;
                    }

                } else {
                    MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleaningItem.give", player);
                    return true;
                }

                /* SETEVENTDETECTIONMODE SUBCOMMAND */
            } else if (arguments[0].equalsIgnoreCase(cleaningItemCommands.get(7))) {

                if (player.hasPermission("chestcleaner.cmd.cleaningItem.seteventdetectionmode")) {

                    boolean eventMode = Boolean.parseBoolean(arguments[1]);
                    pluginConfiguration.setEventModeActive(eventMode);

                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, Messages.getMessage(
                        MessageID.SET_INVENTORY_DETECTION_MODE, "%modeBoolean", String.valueOf(
                            eventMode)),
                        player);
                    pluginConfiguration.enableOpenInventoryEventMode(eventMode);
                    return true;

                } else {
                    if (isPlayer) {
                        MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                            "chestcleaner.cmd.cleaningItem.seteventdetectionmode", player);
                    }
                    return true;
                }

            } else {

                if (isPlayer) {
                    MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                        "/cleaningItem <setitem/setactive/setdurabilityLoss/get/give/rename/setlore/seteventdetectionmode>",
                        player);
                } else {
                    MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR,
                        "/cleaningItem <setactive/setdurabilityloss/give/rename/setlore>");
                }
                return true;
            }

        } else {
            if (isPlayer) {
                MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
                    "/cleaningItem <setitem/setactive/setdurabilityLoss/get/give/rename/setlore/seteventdetectionmode>",
                    player);
            } else {
                MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR,
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
