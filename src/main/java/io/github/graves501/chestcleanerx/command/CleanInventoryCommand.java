package io.github.graves501.chestcleanerx.command;

import io.github.graves501.chestcleanerx.config.PlayerConfig;
import io.github.graves501.chestcleanerx.config.PluginConfig;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.CooldownTimerHandler;
import io.github.graves501.chestcleanerx.util.BlockDetector;
import io.github.graves501.chestcleanerx.util.constant.PluginPermission;
import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
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

        final PluginConfig pluginConfig = PluginConfig.getInstance();
        final PlayerConfig playerConfig = PlayerConfig.getInstance();

        Player player = (Player) commandSender;

        boolean isPlayer = commandSender instanceof Player;

        if (isPlayer) {
            if (!player.hasPermission(PluginPermission.CLEAN_INVENTORY.getString())
                && pluginConfig.isCleanInventoryActive()) {
                InGameMessageHandler
                    .sendMessageToPlayer(player, InGameMessageType.MISSING_PERMISSION,
                        "chestcleaner.cmd.cleanInventory"
                    );
                return true;
            }
        }

        if (arguments.length == 0) {

            // if cs is a console
            if (!(commandSender instanceof Player)) {
                InGameMessageHandler
                    .sendConsoleMessage(InGameMessageType.SYNTAX_ERROR,
                        "/cleanInventory <x> <y> <z>");
                return true;
            }

            Block block = BlockDetector.getTargetBlock(player);

            if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                InGameMessageHandler
                    .sendMessageToPlayer(player, InGameMessageType.ERROR,
                        InGameMessage.INVENTORY_ON_BLACKLIST
                    );
                return true;
            }

            if (!CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {

                // if the block has no inventory
                if (!InventorySorter.sortBlockSelectedByPlayer(player, block,
                    playerConfig.getSortingPatternOfPlayer(player),
                    playerConfig.getEvaluatorTypOfPlayer(
                        player))) {

                    String blockMessage = "("
                        + block.getX()
                        + " / "
                        + block.getY()
                        + " / "
                        + block.getZ()
                        + ", "
                        + block.getType().name()
                        + ")";
                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.ERROR,
                        InGameMessage.BLOCK_HAS_NO_INVENTORY,
                        blockMessage);
                    return true;

                } else {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.INVENTORY_SORTED
                        );
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
                    InGameMessageHandler.sendMessageToPlayer(
                        player, InGameMessageType.ERROR, InGameMessage.INVALID_WORLD_NAME,
                        arguments[3]);

                    return true;
                }
            } else {
                world = Bukkit.getWorld(arguments[3]);
                if (world == null) {
                    InGameMessageHandler.sendConsoleMessage(InGameMessageType.ERROR,
                        InGameMessage.INVALID_WORLD_NAME, arguments[3]);
                    return true;
                }

            }

            if (arguments.length == 4) {
                world = Bukkit.getWorld(arguments[3]);
            }

            Block block = BlockDetector.getBlockByLocation(new Location(world, xCoordinate,
                yCoordinate, zCoordinate));

            if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                InGameMessageHandler
                    .sendMessageToPlayer(player, InGameMessageType.ERROR,
                        InGameMessage.INVENTORY_ON_BLACKLIST
                    );
                return true;
            }

            if (isPlayer && CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {
                return true;
            }

            if (!InventorySorter.sortBlockSelectedByPlayer(player, block,
                playerConfig.getSortingPatternOfPlayer(player),
                playerConfig.getEvaluatorTypOfPlayer(
                    player))) {

                final String blockMessage = "("
                    + xCoordinate
                    + " / "
                    + yCoordinate
                    + " / "
                    + zCoordinate
                    + ")";

                if (isPlayer) {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.ERROR,
                            InGameMessage.BLOCK_HAS_NO_INVENTORY, blockMessage);
                } else {
                    InGameMessageHandler
                        .sendConsoleMessage(InGameMessageType.ERROR,
                            InGameMessage.BLOCK_HAS_NO_INVENTORY, blockMessage);
                }

                return true;

            } else {
                if (isPlayer) {
                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.INVENTORY_SORTED
                        );
                } else {
                    InGameMessageHandler
                        .sendConsoleMessage(InGameMessageType.SUCCESS,
                            InGameMessage.INVENTORY_SORTED);
                }

                return true;
            }

        }
        return true;
    }

}
