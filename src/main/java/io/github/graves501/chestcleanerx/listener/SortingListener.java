package io.github.graves501.chestcleanerx.listener;

import io.github.graves501.chestcleanerx.command.BlacklistCommand;
import io.github.graves501.chestcleanerx.configuration.PlayerConfiguration;
import io.github.graves501.chestcleanerx.configuration.PluginConfiguration;
import io.github.graves501.chestcleanerx.main.PluginMain;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.CooldownTimerHandler;
import io.github.graves501.chestcleanerx.util.BlockDetector;
import io.github.graves501.chestcleanerx.util.logging.PluginLoggerUtil;
import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
import java.util.logging.Logger;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Tom2208
 * @author graves501
 */
public class SortingListener implements org.bukkit.event.Listener {

    final Logger logger = JavaPlugin.getPlugin(PluginMain.class).getLogger();
    final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();
    final PlayerConfiguration playerConfiguration = PlayerConfiguration.getInstance();

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
                    player, playerConfiguration.getSortingPatternOfPlayer(
                        player),
                    playerConfiguration.getEvaluatorTypOfPlayer(
                        player));

                InventorySorter.playSortingSound(player);

                InGameMessageHandler
                    .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.INVENTORY_SORTED
                    );

            } else if (!pluginConfiguration.isOpenInventoryEventDetectionModeActive()
                && isPlayerPermittedToUseCleaningItem(player)) {

                final Block block = BlockDetector.getTargetBlock(player);

                if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                    return;
                }

                if (CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {
                    return;
                }

                if (InventorySorter.sortBlockSelectedByPlayer(player, block,
                    playerConfiguration.getSortingPatternOfPlayer(
                        player),
                    playerConfiguration.getEvaluatorTypOfPlayer(
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

    private void preventConsumptionOfCleaningItem(Cancellable cancellableEvent) {
        cancellableEvent.setCancelled(true);
    }

    /**
     * Damages the item in the Hand of the {@code player} (using player.getItemInHand()), if the
     * {@code durability} (in class Main) is true. Damaging means, that stackable items
     * (maxStackSize > 1) get reduced in amount by one, not stackable items get damaged and removed,
     * if they reach the highest durability .
     *
     * @param player the player who is holding the item, that you want to get damaged, in hand.
     */
    private void damageCleaningItemOfPlayer(final Player player) {
        if (pluginConfiguration.isDurabilityLossActive()) {
            final ItemStack cleaningItem;

            if (isCleaningItemInMainHand(player)) {
                cleaningItem = getItemInMainHand(player);
            } else {
                cleaningItem = getItemInOffHand(player);
            }

            //TODO refactor the deprecated method calls
            if (cleaningItem.getMaxStackSize() == 1) {
                cleaningItem.setDurability((short) (cleaningItem.getDurability() + 1));
            }

            if (cleaningItem.getDurability() >= cleaningItem.getType().getMaxDurability()) {
                cleaningItem.setAmount(cleaningItem.getAmount() - 1);
            }
        }
    }

    @EventHandler
    private void onOpenInventory(final InventoryOpenEvent inventoryOpenEvent) {

        if (pluginConfiguration.isOpenInventoryEventDetectionModeActive()) {

            final Player player = (Player) inventoryOpenEvent.getPlayer();

            if (isPlayerPermittedToUseCleaningItem(player)
                && isPlayerHoldingCleaningItemInAHand(player)) {

                if (CooldownTimerHandler.isSortingOnCooldownForPlayer(player)) {
                    return;
                }

                InventorySorter.sortInventory(inventoryOpenEvent.getInventory(),
                    playerConfiguration.getSortingPatternOfPlayer(player),
                    playerConfiguration.getEvaluatorTypOfPlayer(player));

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

            if (playerConfiguration.getAutoSortChestConfigurationOfPlayer(player)) {

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


    private boolean isInventoryCloseEventCausedByChest(InventoryCloseEvent inventoryCloseEvent) {
        return inventoryCloseEvent.getInventory().getHolder() instanceof Chest;
    }

    private boolean isPlayerRightClickingAirOrBlock(
        final PlayerInteractEvent playerRightClickEvent) {

        boolean isPlayerRightClickingAir =
            playerRightClickEvent.getAction() == Action.RIGHT_CLICK_AIR;

        boolean isPlayerRightClickingBlock =
            playerRightClickEvent.getAction() == Action.RIGHT_CLICK_BLOCK;

        return isPlayerRightClickingAir || isPlayerRightClickingBlock;
    }

    private boolean isPlayerHoldingCleaningItemInAHand(final Player player) {
        return isCleaningItemInMainHand(player) || isCleaningItemInOffHand(player);
    }

    private boolean isPlayerHoldingCleaningItemInBothHands(final Player player) {
        return isCleaningItemInMainHand(player) && isCleaningItemInOffHand(player);
    }

    private boolean isCleaningItemInMainHand(final Player player) {
        final ItemStack currentCleaningItem = pluginConfiguration.getCurrentCleaningItem();
        final ItemStack itemInMainHand = getItemInMainHand(player);

        final boolean isCleaningItemInMainHand = itemInMainHand.equals(currentCleaningItem);

        return isCleaningItemInMainHand;
    }

    private boolean isCleaningItemInOffHand(final Player player) {
        final ItemStack currentCleaningItem = pluginConfiguration.getCurrentCleaningItem();
        final ItemStack itemInOffHand = getItemInOffHand(player);

        final boolean isCleaningItemInOffHand = itemInOffHand.equals(currentCleaningItem);

        return isCleaningItemInOffHand;
    }

    private ItemStack getItemInMainHand(final Player player) {
        final ItemStack itemInMainHand = player.getInventory().getItemInMainHand().clone();
        //TODO fix this deprecated line
        itemInMainHand.setDurability((short) 0);
        itemInMainHand.setAmount(1);

        return itemInMainHand;
    }

    private ItemStack getItemInOffHand(final Player player) {
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand().clone();
        //TODO fix this deprecated line
        itemInOffHand.setDurability((short) 0);
        itemInOffHand.setAmount(1);

        return itemInOffHand;
    }

}
