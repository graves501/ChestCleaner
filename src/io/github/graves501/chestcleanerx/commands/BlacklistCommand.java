package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.utils.stringconstants.PlayerMessages;
import io.github.graves501.chestcleanerx.utils.stringconstants.PluginPermissions;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.StringUtil;

import io.github.graves501.chestcleanerx.config.Config;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.utils.MaterialListUtils;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.StringTable;

public class BlacklistCommand implements CommandExecutor, TabCompleter {

    private static class BlackListCommandString {

        static final String SORTING = "sorting";
        static final String INVENTORIES = "inventories";

        static final String ADD_MATERIAL = "addMaterial";
        static final String REMOVE_MATERIAL = "removeMaterial";
        static final String LIST = "list";
        static final String CLEAR = "clear";

    }

    private static final boolean VALID_COMMAND = true;
    private static final boolean INVALID_COMMAND = false;

    private static final int SINGLE_ARGUMENT = 1;
    private static final int TWO_ARGUMENTS = 2;
    private static final int THREE_ARGUMENTS = 3;

    private final List<String> commandList = new ArrayList<>();
    private final List<String> blacklists = new ArrayList<>();

    //TODO what's up with this magic number?
    private final int LIST_LENGTH = 8;

    public static ArrayList<Material> inventoryBlacklist = new ArrayList<>();

    public BlacklistCommand() {
        blacklists.add(BlackListCommandString.SORTING);
        blacklists.add(BlackListCommandString.INVENTORIES);

        commandList.add(BlackListCommandString.ADD_MATERIAL);
        commandList.add(BlackListCommandString.REMOVE_MATERIAL);
        commandList.add(BlackListCommandString.LIST);
        commandList.add(BlackListCommandString.CLEAR);
    }

    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command,
        final String alias,
        final String[] arguments) {

        if (!(commandSender instanceof Player)) {

            // TODO is sending the console message really needed?
            //
            // According to https://hub.spigotmc.org/javadocs/spigot/org/bukkit/command/CommandExecutor.html:
            //
            // If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the player.

            MessageSystem.sendConsoleMessage(MessageType.ERROR, MessageID.NOT_A_PLAYER);
            return VALID_COMMAND;
        }

        Player player = (Player) commandSender;

        if (player.hasPermission(PluginPermissions.BLACKLIST_PERMISSION)) {

            if (arguments.length <= SINGLE_ARGUMENT) {
                sendSyntaxErrorMessageToPlayer(player);
                return VALID_COMMAND;
            }

            if (arguments.length == TWO_ARGUMENTS || arguments.length == THREE_ARGUMENTS) {

                ArrayList<Material> materialList = null;

                int listNumber = -1;
                if (arguments[0].equalsIgnoreCase(blacklists.get(0))) {
                    materialList = InventorySorter.blacklist;
                    listNumber = 0;
                } else if (arguments[0].equalsIgnoreCase(blacklists.get(1))) {
                    materialList = inventoryBlacklist;
                    listNumber = 1;
                } else {
                    sendSyntaxErrorMessageToPlayer(player);
                    return VALID_COMMAND;
                }

                /** subCommands */

                /*----- addMaterial -----*/
                if (arguments[1].equalsIgnoreCase(commandList.get(0))) {

                    Material material;

                    // addMaterial with name
                    if (arguments.length == 3) {

                        material = Material.getMaterial(arguments[2]);

                        if (material == null) {
                            MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                                StringTable
                                    .getMessage(MessageID.NO_MATERIAL_FOUND, "%material",
                                        arguments[2]),
                                player);
                            return VALID_COMMAND;
                        }

                        // addMaterial with item in hand
                    } else {
                        material = getMaterialFromPlayerHand(player);

                        if (material.equals(Material.AIR)) {
                            MessageSystem
                                .sendMessageToPlayer(MessageType.ERROR, MessageID.HOLD_AN_ITEM,
                                    player);
                            return VALID_COMMAND;
                        }

                    }

                    if (materialList.contains(material)) {
                        MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                            StringTable.getMessage(MessageID.IS_ALREADY_ON_BLACKLIST, "%material",
                                material.name()),
                            player);
                        return VALID_COMMAND;
                    }

                    materialList.add(material);
                    saveBlacklistInConfiguration(listNumber);

                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        StringTable
                            .getMessage(MessageID.SET_TO_BLACKLIST, "%material", material.name()),
                        player);
                    return VALID_COMMAND;

                    /*----- removeMaterial -----*/
                } else if (arguments[1].equalsIgnoreCase(commandList.get(1))) {

                    Material material;

                    // removeMaterial with name
                    if (arguments.length == 3) {

                        material = Material.getMaterial(arguments[2]);

                        if (material == null) {

                            try {

                                int index = Integer.valueOf(arguments[2]);

                                if (index - 1 >= 0 && index - 1 < materialList.size()) {

                                    material = materialList.get(index - 1);

                                } else {
                                    MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                                        StringTable.getMessage(
                                            MessageID.INDEX_OUT_OF_BOUNDS, "%biggestindex",
                                            String.valueOf(index)),
                                        player);
                                    return VALID_COMMAND;
                                }

                            } catch (NumberFormatException ex) {

                                MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                                    StringTable.getMessage(MessageID.NO_MATERIAL_FOUND, "%material",
                                        arguments[2]),
                                    player);
                                return VALID_COMMAND;

                            }

                        }

                        // removeMaterial with item in hand
                    } else {
                        material = getMaterialFromPlayerHand(player);

                        if (material == null) {
                            MessageSystem
                                .sendMessageToPlayer(MessageType.ERROR, MessageID.HOLD_AN_ITEM,
                                    player);
                            return VALID_COMMAND;
                        }
                    }

                    if (!materialList.contains(material)) {
                        MessageSystem.sendMessageToPlayer(MessageType.ERROR, StringTable
                                .getMessage(MessageID.BLACKLIST_DOESNT_CONTAINS, "%material",
                                    material.name()),
                            player);
                        return VALID_COMMAND;
                    }

                    materialList.remove(material);
                    saveBlacklistInConfiguration(listNumber);

                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        StringTable.getMessage(MessageID.REMOVED_FORM_BLACKLIST, "%material",
                            material.name()),
                        player);
                    return VALID_COMMAND;

                    /*----- list -----*/
                } else if (arguments[1].equalsIgnoreCase(commandList.get(2))) {

                    if (materialList.isEmpty()) {
                        MessageSystem
                            .sendMessageToPlayer(MessageType.ERROR, MessageID.BLACKLIST_IS_EMPTY,
                                player);
                        return VALID_COMMAND;
                    }

                    int page = 1;
                    int pages = materialList.size() / LIST_LENGTH + 1;

                    if (arguments.length == 3) {

                        try {

                            page = Integer.valueOf(arguments[2]);

                        } catch (NumberFormatException ex) {

                            MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                                StringTable
                                    .getMessage(MessageID.INVALID_INPUT_FOR_INTEGER, "%index",
                                        arguments[1]),
                                player);
                            return VALID_COMMAND;

                        }

                        if (!(page > 0 && page <= pages)) {
                            MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                                StringTable.getMessage(MessageID.INVALID_PAGE_NUMBER, "%range",
                                    "1 - " + pages),
                                player);
                            return VALID_COMMAND;
                        }

                    }

                    MaterialListUtils
                        .sendListPageToPlayer(materialList, player, page, LIST_LENGTH, pages);
                    return VALID_COMMAND;

                    /*----- clear -----*/
                } else if (arguments[1].equalsIgnoreCase(commandList.get(3))) {

                    materialList.clear();
                    saveBlacklistInConfiguration(listNumber);
                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.BLACKLIST_CLEARED,
                            player);
                    return VALID_COMMAND;

                } else {
                    sendSyntaxErrorMessageToPlayer(player);
                    return VALID_COMMAND;
                }

            } else {
                sendSyntaxErrorMessageToPlayer(player);
                return VALID_COMMAND;
            }

        } else {
            MessageSystem
                .sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                    PluginPermissions.BLACKLIST_PERMISSION,
                    player);
            return VALID_COMMAND;
        }

    }

    private Material getMaterialFromPlayerHand(final Player player) {
        final PlayerInventory playerInventory = player.getInventory();

        final Material materialInMainHand = playerInventory.getItemInMainHand().getType();

        if (isHandEquippedWithMaterial(materialInMainHand)) {
            return materialInMainHand;
        }

        final Material materialInOffHand = playerInventory.getItemInOffHand().getType();

        if (isHandEquippedWithMaterial(materialInOffHand)) {
            return materialInOffHand;
        }

        //TODO how does Spigot/Minecraft handle Nullpointer exceptions?
        return null;
    }

    private boolean isHandEquippedWithMaterial(final Material materialInHand) {
        return !materialInHand.equals(Material.AIR);
    }

    private void sendSyntaxErrorMessageToPlayer(final Player player) {
        MessageSystem
            .sendMessageToPlayer(MessageType.SYNTAX_ERROR, PlayerMessages.BLACKLIST_SYNTAX_ERROR,
                player);
    }

    private void saveBlacklistInConfiguration(final int blacklist) {
        final int SORTING_BLACKLIST = 0;
        final int INVENTORY_BLACKLIST = 1;

        if (blacklist == SORTING_BLACKLIST) {
            Config.setSortingBlackList(InventorySorter.blacklist);
        } else if (blacklist == INVENTORY_BLACKLIST) {
            Config.setInventoryBlackList(inventoryBlacklist);
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias,
        String[] args) {

        final List<String> tabCompletions = new ArrayList<>();

        if (args.length <= SINGLE_ARGUMENT) {
            StringUtil.copyPartialMatches(args[0], blacklists, tabCompletions);
        } else if (args.length == TWO_ARGUMENTS) {
            StringUtil.copyPartialMatches(args[1], commandList, tabCompletions);
        }

        return tabCompletions;
    }

}
