package io.github.graves501.chestcleanerx.listeners;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.config.PlayerConfiguration;
import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.main.PluginMain;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.CooldownTimerHandler;
import io.github.graves501.chestcleanerx.utils.BlockDetector;
import io.github.graves501.chestcleanerx.utils.logging.PluginLoggerUtil;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessage;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessageHandler;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessageType;
import java.util.logging.Logger;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
    private void onRightClick(PlayerInteractEvent onRightClickEvent) {
        final Player player = onRightClickEvent.getPlayer();

        //FIXME check if sorting gets called twice when holding cleaning item in both hands
        if (isPlayerHoldingCleaningItemInAHand(player)
            && isPlayerRightClickingAirOrBlock(onRightClickEvent)) {

            if (isPlayerPermittedToSortOwnInventory(player) && player.isSneaking()) {

                if (isSortingForPlayerOnCooldown(player)) {
                    return;
                }

                damageCleaningItemOfPlayer(player);

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

                // TODO why setCancelled?
                onRightClickEvent.setCancelled(true);

            } else if (!pluginConfiguration.isOpenInventoryEventDetectionModeActive()
                && isPlayerPermittedToUseCleaningItem(player)) {

                final Block block = BlockDetector.getTargetBlock(player);

                if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                    return;
                }

                if (!isSortingForPlayerOnCooldown(player)) {
                    return;
                }

                if (InventorySorter.sortPlayerBlock(block,
                    player, playerConfiguration.getSortingPatternOfPlayer(
                        player),
                    playerConfiguration.getEvaluatorTypOfPlayer(
                        player))) {

                    damageCleaningItemOfPlayer(player);

                    InGameMessageHandler
                        .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                            InGameMessage.INVENTORY_SORTED);

                    // TODO why setCancelled?
                    onRightClickEvent.setCancelled(true);
                }
            }
        }
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

                if (isSortingForPlayerOnCooldown(player)) {
                    return;
                }

                InventorySorter.sortInventory(inventoryOpenEvent.getInventory(),
                    playerConfiguration.getSortingPatternOfPlayer(player),
                    playerConfiguration.getEvaluatorTypOfPlayer(player));

                InventorySorter.playSortingSound(player);
                damageCleaningItemOfPlayer(player);

                //TODO why set cancelled?
                inventoryOpenEvent.setCancelled(true);

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

                if (isSortingForPlayerOnCooldown(player)) {
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

    private boolean isSortingForPlayerOnCooldown(final Player player) {
        return !CooldownTimerHandler.isPlayerAllowedToUseSort(player);
    }

    private boolean isInventoryCloseEventCausedByChest(InventoryCloseEvent inventoryCloseEvent) {
        return inventoryCloseEvent.getInventory().getHolder() instanceof Chest;
    }

    private boolean isPlayerRightClickingAirOrBlock(final PlayerInteractEvent playerInteractEvent) {
        return playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR
            || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK;
    }

    private boolean isPlayerHoldingCleaningItemInAHand(final Player player) {
        return isCleaningItemInMainHand(player) || isCleaningItemInOffHand(player);
    }

    private boolean isCleaningItemInMainHand(final Player player) {
        final ItemStack currentCleaningItem = pluginConfiguration.getCurrentCleaningItem();
        final ItemStack itemInMainHand = getItemInMainHand(player);

        final boolean isCleaningItemInMainHand = itemInMainHand.equals(currentCleaningItem);
        PluginLoggerUtil
            .logPlayerInfo(logger, player, "isCleaningItemInMainHand: " + isCleaningItemInMainHand);

        return isCleaningItemInMainHand;
    }

    private boolean isCleaningItemInOffHand(final Player player) {
        final ItemStack currentCleaningItem = pluginConfiguration.getCurrentCleaningItem();
        final ItemStack itemInOffHand = getItemInOffHand(player);

        final boolean isCleaningItemInOffHand = itemInOffHand.equals(currentCleaningItem);
        PluginLoggerUtil
            .logPlayerInfo(logger, player, "isCleaningItemInOffHand: " + isCleaningItemInOffHand);

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
