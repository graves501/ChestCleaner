package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.utils.MaterialBlacklistUtils;
import io.github.graves501.chestcleanerx.utils.enums.BlacklistConstant;
import io.github.graves501.chestcleanerx.utils.enums.PlayerMessage;
import io.github.graves501.chestcleanerx.utils.enums.PluginPermission;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessage;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessageHandler;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessageType;
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

public class BlacklistCommand implements CommandExecutor, TabCompleter {

    private static final boolean VALID_COMMAND = true;
    private static final boolean INVALID_COMMAND = false;

    private static final int SINGLE_ARGUMENT = 1;
    private static final int TWO_ARGUMENTS = 2;
    private static final int THREE_ARGUMENTS = 3;

    private final List<String> commandList = new ArrayList<>();
    private final List<String> blacklists = new ArrayList<>();

    //TODO what's up with this magic number?
    private final int LIST_LENGTH = 8;

    public static List<Material> inventoryBlacklist = new ArrayList<>();

    public BlacklistCommand() {
        blacklists.add(BlacklistConstant.SORTING.getString());
        blacklists.add(BlacklistConstant.INVENTORIES.getString());

        commandList.add(BlacklistConstant.ADD_MATERIAL.getString());
        commandList.add(BlacklistConstant.REMOVE_MATERIAL.getString());
        commandList.add(BlacklistConstant.LIST.getString());
        commandList.add(BlacklistConstant.CLEAR.getString());
    }

    @Override
    public boolean onCommand(final CommandSender commandSender,
        final Command command,
        final String alias,
        final String[] arguments) {

        if (!(commandSender instanceof Player)) {

            // TODO is sending the console message really needed?
            //
            // According to https://hub.spigotmc.org/javadocs/spigot/org/bukkit/command/CommandExecutor.html:
            //
            // If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the player.

            InGameMessageHandler
                .sendConsoleMessage(InGameMessageType.ERROR, InGameMessage.NOT_A_PLAYER);
            return VALID_COMMAND;
        }

        Player player = (Player) commandSender;

        if (player.hasPermission(PluginPermission.BLACKLIST.getString())) {

            if (arguments.length <= SINGLE_ARGUMENT) {
                sendSyntaxErrorMessageToPlayer(player);
                return VALID_COMMAND;
            }

            if (arguments.length == TWO_ARGUMENTS || arguments.length == THREE_ARGUMENTS) {

                List<Material> materialList = null;

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

                if (arguments[1].equalsIgnoreCase(BlacklistConstant.ADD_MATERIAL.getString())) {

                    Material material;

                    // addMaterial with name
                    if (arguments.length == 3) {

                        material = Material.getMaterial(arguments[2]);

                        if (material == null) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                    InGameMessage.NO_MATERIAL_FOUND, arguments[2]);

                            return VALID_COMMAND;
                        }

                        // addMaterial with item in hand
                    } else {
                        material = getMaterialFromPlayerHand(player);

                        if (material.equals(Material.AIR)) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                    InGameMessage.ITEM_IN_HAND_REQUIRED
                                );
                            return VALID_COMMAND;
                        }

                    }

                    if (materialList.contains(material)) {
                        InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.ERROR,
                            InGameMessage.IS_ALREADY_ON_BLACKLIST, material.name());

                        return VALID_COMMAND;
                    }

                    materialList.add(material);
                    saveBlacklistInConfiguration(listNumber);

                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.ADDED_TO_BLACKLIST, material.name());
                    return VALID_COMMAND;

                } else if (arguments[1]
                    .equalsIgnoreCase(BlacklistConstant.REMOVE_MATERIAL.getString())) {

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
                                    InGameMessageHandler
                                        .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                            InGameMessage.INDEX_OUT_OF_BOUNDS,
                                            String.valueOf(index));
                                    return VALID_COMMAND;
                                }

                            } catch (NumberFormatException ex) {
                                InGameMessageHandler
                                    .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                        InGameMessage.NO_MATERIAL_FOUND, arguments[2]);
                                return VALID_COMMAND;

                            }

                        }

                        // removeMaterial with item in hand
                    } else {
                        material = getMaterialFromPlayerHand(player);

                        if (material == null) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                    InGameMessage.ITEM_IN_HAND_REQUIRED
                                );
                            return VALID_COMMAND;
                        }
                    }

                    if (!materialList.contains(material)) {
                        InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.ERROR,
                            InGameMessage.BLACKLIST_DOESNT_CONTAIN, material.name());
                        return VALID_COMMAND;
                    }

                    materialList.remove(material);
                    saveBlacklistInConfiguration(listNumber);

                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.REMOVED_FROM_BLACKLIST, material.name());
                    return VALID_COMMAND;

                } else if (arguments[1].equalsIgnoreCase(BlacklistConstant.LIST.getString())) {

                    if (materialList.isEmpty()) {
                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                InGameMessage.BLACKLIST_IS_EMPTY
                            );
                        return VALID_COMMAND;
                    }

                    int page = 1;
                    int pages = materialList.size() / LIST_LENGTH + 1;

                    if (arguments.length == 3) {

                        try {

                            page = Integer.valueOf(arguments[2]);

                        } catch (NumberFormatException ex) {

                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                    InGameMessage.INVALID_INPUT_FOR_INTEGER, arguments[1]);
                            return VALID_COMMAND;

                        }

                        if (!(page > 0 && page <= pages)) {
                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.ERROR,
                                    InGameMessage.INVALID_PAGE_NUMBER, "1 - " + pages);
                            return VALID_COMMAND;
                        }

                    }

                    MaterialBlacklistUtils
                        .sendListPageToPlayer(materialList, player, page, LIST_LENGTH, pages);
                    return VALID_COMMAND;

                    /*----- clear -----*/
                } else if (arguments[1].equalsIgnoreCase(BlacklistConstant.CLEAR.getString())) {

                    materialList.clear();
                    saveBlacklistInConfiguration(listNumber);
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.BLACKLIST_CLEARED
                        );
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
            InGameMessageHandler
                .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                    PluginPermission.BLACKLIST.getString()
                );
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
        InGameMessageHandler
            .sendMessageToPlayer(player, InGameMessageType.SYNTAX_ERROR,
                PlayerMessage.BLACKLIST_SYNTAX_ERROR.getString()
            );
    }

    private void saveBlacklistInConfiguration(final int blacklist) {
        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();

        final int SORTING_BLACKLIST = 0;
        final int INVENTORY_BLACKLIST = 1;

        if (blacklist == SORTING_BLACKLIST) {
            pluginConfiguration.setSortingBlacklist(InventorySorter.blacklist);
        } else if (blacklist == INVENTORY_BLACKLIST) {
            pluginConfiguration.setInventoryBlackList(inventoryBlacklist);
        }

    }

    @Override
    public List<String> onTabComplete(final CommandSender sender,
        final Command command,
        final String alias,
        final String[] arguments) {

        final List<String> tabCompletions = new ArrayList<>();

        if (arguments.length <= SINGLE_ARGUMENT) {
            StringUtil.copyPartialMatches(arguments[0], blacklists, tabCompletions);
        } else if (arguments.length == TWO_ARGUMENTS) {
            StringUtil.copyPartialMatches(arguments[1], commandList, tabCompletions);
        }

        return tabCompletions;
    }

}
