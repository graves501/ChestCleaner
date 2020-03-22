package io.github.graves501.chestcleanerx.listener;

import static io.github.graves501.chestcleanerx.util.inventory.InventoryUtil.*;

import io.github.graves501.chestcleanerx.command.BlacklistCommand;
import io.github.graves501.chestcleanerx.config.PlayerConfig;
import io.github.graves501.chestcleanerx.config.PluginConfig;
import io.github.graves501.chestcleanerx.main.ChestCleanerX;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.CooldownTimerHandler;
import io.github.graves501.chestcleanerx.util.BlockDetector;
import io.github.graves501.chestcleanerx.util.logging.PluginLoggerUtil;
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

    private final Logger logger = JavaPlugin.getPlugin(ChestCleanerX.class).getLogger();
    private final PluginConfig pluginConfig = PluginConfig.getInstance();
    private final PlayerConfig playerConfig = PlayerConfig.getInstance();

    @EventHandler
    private void onRightClick(final PlayerInteractEvent playerRightClickEvent) {
        final Player player = playerRightClickEvent.getPlayer();

        if (isPlayerHoldingCleaningItemInAHand(player)
            && isPlayerRightClickingAirOrBlock(playerRightClickEvent)) {

            if (isPlayerPermittedToSortOwnInventory(player) && player.isSneaking()) {

                if (CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {
                    return;
                }

                if (isPlayerHoldingCleaningItemInBothHands(player)) {
                    final EquipmentSlot currentHand = playerRightClickEvent.getHand();

                    if (currentHand == EquipmentSlot.OFF_HAND) {
                        return;
                    }
                } else if (preventSortingTwice(playerRightClickEvent)) {
                    return;
                }

                damageCleaningItemOfPlayer(player);
                preventConsumptionOfCleaningItem(playerRightClickEvent);

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

            } else if (!pluginConfig.isOpenInventoryEventDetectionModeActive()
                && isPlayerPermittedToUseCleaningItem(player)) {

                final Block block = BlockDetector.getTargetBlock(player);

                if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                    return;
                }

                if (CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {
                    return;
                }

                if (InventorySorter.sortBlockSelectedByPlayer(player, block,
                    playerConfig.getSortingPatternOfPlayer(
                        player),
                    playerConfig.getEvaluatorTypOfPlayer(
                        player))) {

                    damageCleaningItemOfPlayer(player);
                    preventConsumptionOfCleaningItem(playerRightClickEvent);

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.INVENTORY_SORTED);
                }
            }
        }
    }

    private boolean preventSortingTwice(
        final PlayerInteractEvent rightClickEvent) {

        final Player player = rightClickEvent.getPlayer();
        final EquipmentSlot currentHand = rightClickEvent.getHand();

        if (isCleaningItemInMainHand(player) && currentHand == EquipmentSlot.OFF_HAND) {
            PluginLoggerUtil.logPlayerInfo(logger, player,
                "MAIN HAND: PREVENT TRUE");
            return true;
        }

        if (isCleaningItemInOffHand(player) && currentHand == EquipmentSlot.HAND) {
            PluginLoggerUtil.logPlayerInfo(logger, player,
                "OFF HANDS");
            return true;
        }

        return false;
    }

    @EventHandler
    private void onOpenInventory(final InventoryOpenEvent inventoryOpenEvent) {

        if (pluginConfig.isOpenInventoryEventDetectionModeActive()) {

            final Player player = (Player) inventoryOpenEvent.getPlayer();

            if (isPlayerPermittedToUseCleaningItem(player)
                && isPlayerHoldingCleaningItemInAHand(player)) {

                if (CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {
                    return;
                }

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

            if (playerConfig.getAutoSortChestConfigOfPlayer(player)) {

                if (CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {
                    return;
                }

                InventorySorter.sortInventoryOfPlayer(inventoryCloseEvent.getInventory(), player);
                InventorySorter.playSortingSound(player);
                InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                    InGameMessage.INVENTORY_SORTED);
            }
        }
    }

    //TODO refactor permission
    private boolean isPlayerPermittedToSortOwnInventory(final Player player) {
        final boolean isPlayerPermittedToSortOwnInventory = player
            .hasPermission("chestcleaner.cleaningItem.use.owninventory");

        PluginLoggerUtil.logPlayerInfo(logger, player,
            "isPlayerPermittedToSortOwnInventory: " + isPlayerPermittedToSortOwnInventory);
        return isPlayerPermittedToSortOwnInventory;
    }

    //TODO refactor permission
    private boolean isPlayerPermittedToUseCleaningItem(final Player player) {
        final boolean isPlayerPermittedToUseCleaningItem = player
            .hasPermission("chestcleaner.cleaningItem.use");

        PluginLoggerUtil.logPlayerInfo(logger, player,
            "isPlayerPermittedToUseCleaningItem: " + isPlayerPermittedToUseCleaningItem);
        return isPlayerPermittedToUseCleaningItem;
    }
}
