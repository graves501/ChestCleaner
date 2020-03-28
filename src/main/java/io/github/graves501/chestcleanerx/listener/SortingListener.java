package io.github.graves501.chestcleanerx.listener;

import static io.github.graves501.chestcleanerx.util.inventory.InventoryUtil.damageCleaningItemOfPlayer;
import static io.github.graves501.chestcleanerx.util.inventory.InventoryUtil.isCleaningItemInMainHand;
import static io.github.graves501.chestcleanerx.util.inventory.InventoryUtil.isInventoryCloseEventCausedByChest;
import static io.github.graves501.chestcleanerx.util.inventory.InventoryUtil.isPlayerRightClickingAirOrBlock;
import static io.github.graves501.chestcleanerx.util.inventory.InventoryUtil.preventConsumptionOfCleaningItem;

import io.github.graves501.chestcleanerx.command.BlacklistCommand;
import io.github.graves501.chestcleanerx.config.PermissionHandler;
import io.github.graves501.chestcleanerx.config.PlayerConfig;
import io.github.graves501.chestcleanerx.config.PluginConfig;
import io.github.graves501.chestcleanerx.main.ChestCleanerX;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.CooldownHandler;
import io.github.graves501.chestcleanerx.util.BlockDetector;
import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
import java.util.logging.Logger;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Tom2208
 * @author graves501
 */
public class SortingListener implements org.bukkit.event.Listener {

    //TODO log more events in this class
    private final Logger logger = JavaPlugin.getPlugin(ChestCleanerX.class).getLogger();
    private final PluginConfig pluginConfig = PluginConfig.getInstance();
    private final PlayerConfig playerConfig = PlayerConfig.getInstance();

    @EventHandler
    private void onRightClick(final PlayerInteractEvent playerRightClickEvent) {

        final Player player = playerRightClickEvent.getPlayer();

        if (preventSortingTwice(playerRightClickEvent) || CooldownHandler.isSortingOnCooldownForPlayer(player)) {
            return;
        }

        if (isCleaningItemInMainHand(player)
            && isPlayerRightClickingAirOrBlock(playerRightClickEvent)) {

            if (PermissionHandler.playerHasPermissionToSortOwnInventory(player) && player
                .isSneaking()) {

                sortInventoryOfPlayer(player);
                damageCleaningItemOfPlayer(player);
                preventConsumptionOfCleaningItem(playerRightClickEvent);

            } else if (!pluginConfig.isOpenInventoryEventDetectionModeActive()
                && PermissionHandler.playerHasPermissionToUseCleaningItem(player)) {

                sortInventoryOfBlockSelectedByPlayer(player);
                damageCleaningItemOfPlayer(player);
                preventConsumptionOfCleaningItem(playerRightClickEvent);
            }
        }
    }

    private void sortInventoryOfPlayer(final Player player) {
        //TODO refactor InventorySorter
        InventorySorter.sortPlayerInventory(
            player, playerConfig.getSortingPatternOfPlayer(
                player),
            playerConfig.getEvaluatorTypOfPlayer(
                player));

        InventorySorter.playSortingSound(player);

        InGameMessageHandler
            .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                InGameMessage.INVENTORY_SORTED
            );
    }

    private void sortInventoryOfBlockSelectedByPlayer(final Player player) {
        final Block block = BlockDetector.getTargetBlock(player);

        if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
            return;
        }

        if (InventorySorter.sortBlockSelectedByPlayer(player, block,
            playerConfig.getSortingPatternOfPlayer(
                player),
            playerConfig.getEvaluatorTypOfPlayer(
                player))) {

            InGameMessageHandler
                .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                    InGameMessage.INVENTORY_SORTED);
        }
    }

    private boolean preventSortingTwice(
        final PlayerInteractEvent rightClickEvent) {
        return rightClickEvent.getHand() == EquipmentSlot.OFF_HAND;
    }

    @EventHandler
    private void onOpenInventory(final InventoryOpenEvent inventoryOpenEvent) {

        if (pluginConfig.isOpenInventoryEventDetectionModeActive()) {

            final Player player = (Player) inventoryOpenEvent.getPlayer();

            if (CooldownHandler.isSortingOnCooldownForPlayer(player)) {
                return;
            }

            if (PermissionHandler.playerHasPermissionToUseCleaningItem(player)
                && isCleaningItemInMainHand(player)) {

                InventorySorter.sortInventory(inventoryOpenEvent.getInventory(),
                    playerConfig.getSortingPatternOfPlayer(player),
                    playerConfig.getEvaluatorTypOfPlayer(player));

                InventorySorter.playSortingSound(player);
                damageCleaningItemOfPlayer(player);
                preventConsumptionOfCleaningItem(inventoryOpenEvent);

                InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                    InGameMessage.INVENTORY_SORTED);
            }
        }
    }

    @EventHandler
    private void onCloseInventory(final InventoryCloseEvent inventoryCloseEvent) {
        if (isInventoryCloseEventCausedByChest(inventoryCloseEvent)) {
            final Player player = (Player) inventoryCloseEvent.getPlayer();

            if (CooldownHandler.isSortingOnCooldownForPlayer(player)) {
                return;
            }

            if (playerConfig.getAutoSortChestConfigOfPlayer(player)) {
                InventorySorter.sortInventoryOfPlayer(inventoryCloseEvent.getInventory(), player);
                InventorySorter.playSortingSound(player);
                InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                    InGameMessage.INVENTORY_SORTED);
            }
        }
    }
}
