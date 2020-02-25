package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.main.Main;
import io.github.graves501.chestcleanerx.playerdata.PlayerDataManager;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.Timer;
import io.github.graves501.chestcleanerx.utils.BlockDetector;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import io.github.graves501.chestcleanerx.utils.stringconstants.PluginPermissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Tom
 */
public class CleanInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender commandSender,
        final Command command,
        final String label,
        final String[] arguments) {
        Player player = (Player) commandSender;
        boolean isPlayer = commandSender instanceof Player;
        if (isPlayer) {
            if (!player.hasPermission(PluginPermissions.CLEAN_INVENTORY_PERMISSION)
                && Main.cleanInvPermission) {
                MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
                    "chestcleaner.cmd.cleanInventory",
                    player);
                return true;
            }
        }

        if (arguments.length == 0) {

            // if cs is a console
            if (!(commandSender instanceof Player)) {
                MessageSystem
                    .sendConsoleMessage(MessageType.SYNTAX_ERROR, "/cleanInventory <x> <y> <z>");
                return true;
            }

            Block block = BlockDetector.getTargetBlock(player);

            if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                MessageSystem
                    .sendMessageToPlayer(MessageType.ERROR, MessageID.INVENTORY_ON_BLACKLIST,
                        player);
                return true;
            }

            if (Timer.playerCheck(player)) {

                // if the block has no inventory
                if (!InventorySorter.sortPlayerBlock(block,
                    player, PlayerDataManager.getSortingPatternOfPlayer(player),
                    PlayerDataManager.getEvaluatorTypOfPlayer(
                        player))) {

                    MessageSystem.sendMessageToPlayer(MessageType.ERROR,
                        Messages.getMessage(MessageID.BLOCK_HAS_NO_INV, "%location",
                            "(" + block.getX() + " / " + block.getY() + " / " + block.getZ() + ", "
                                + block.getType().name() + ")"),
                        player);
                    return true;

                } else {
                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.INVENTORY_SORTED,
                            player);
                    return true;
                }

            }

        } else if (arguments.length == 3 || arguments.length == 4) {

            int xCoordinate = (int) Double.parseDouble(arguments[0]);
            int yCoordinate = (int) Double.parseDouble(arguments[1]);
            int zCoordinate = (int) Double.parseDouble(arguments[2]);

            World world;

            if (isPlayer) {
                if (arguments.length == 4) {
                    world = Bukkit.getWorld(arguments[3]);
                } else {
                    world = player.getWorld();
                }
                if (world == null) {
                    MessageSystem.sendMessageToPlayer(
                        MessageType.ERROR,
                        Messages
                            .getMessage(MessageID.INVALID_WORLD_NAME, "%worldname", arguments[3]),
                        player);

                    return true;
                }
            } else {
                world = Bukkit.getWorld(arguments[3]);
                if (world == null) {
                    MessageSystem.sendConsoleMessage(MessageType.ERROR,
                        Messages
                            .getMessage(MessageID.INVALID_WORLD_NAME, "%worldname", arguments[3]));
                    return true;
                }

            }

            if (arguments.length == 4) {
                world = Bukkit.getWorld(arguments[3]);
            }

            Block block = BlockDetector.getBlockByLocation(new Location(world, xCoordinate,
                yCoordinate, zCoordinate));

            if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                MessageSystem
                    .sendMessageToPlayer(MessageType.ERROR, MessageID.INVENTORY_ON_BLACKLIST,
                        player);
                return true;
            }

            if (isPlayer && !Timer.playerCheck(player)) {
                return true;
            }

            if (!InventorySorter.sortPlayerBlock(block,
                player, PlayerDataManager.getSortingPatternOfPlayer(player),
                PlayerDataManager.getEvaluatorTypOfPlayer(
                    player))) {

                if (isPlayer) {
                    MessageSystem.sendMessageToPlayer(MessageType.ERROR, Messages.getMessage(
                        MessageID.BLOCK_HAS_NO_INV, "%location",
                        "(" + xCoordinate + " / " + yCoordinate + " / " + zCoordinate + ")"),
                        player);
                } else {
                    MessageSystem.sendConsoleMessage(MessageType.ERROR, Messages.getMessage(
                        MessageID.BLOCK_HAS_NO_INV, "%location",
                        "(" + xCoordinate + " / " + yCoordinate + " / " + zCoordinate + ")"));
                }

                return true;

            } else {
                if (isPlayer) {
                    MessageSystem
                        .sendMessageToPlayer(MessageType.SUCCESS, MessageID.INVENTORY_SORTED,
                            player);
                } else {
                    MessageSystem
                        .sendConsoleMessage(MessageType.SUCCESS, MessageID.INVENTORY_SORTED);
                }

                return true;
            }

        }
        return true;
    }

}
