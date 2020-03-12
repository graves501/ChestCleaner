package io.github.graves501.chestcleanerx.listeners;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.playerdata.PlayerDataManager;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.CooldownTimer;
import io.github.graves501.chestcleanerx.utils.BlockDetector;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Tom2208
 */
public class SortingListener implements org.bukkit.event.Listener {


    @EventHandler
    private void onRightClick(PlayerInteractEvent playerInteractEvent) {

        final Player player = playerInteractEvent.getPlayer();
        ItemStack itemMainHand = player.getInventory().getItemInMainHand().clone();
        itemMainHand.setDurability((short) 0);
        itemMainHand.setAmount(1);

        ItemStack itemOffHand = player.getInventory().getItemInOffHand().clone();
        itemOffHand.setDurability((short) 0);
        itemOffHand.setAmount(1);

        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();
        final ItemStack currentCleaningItem = pluginConfiguration.getCurrentCleaningItem();

        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR
            || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {

            boolean isMainHand = itemMainHand.equals(currentCleaningItem);
            boolean isOffHand = itemOffHand.equals(currentCleaningItem);

            // TODO clean up
            // TODO RIGHTCLICK WIRD WOHL ZWEI MAL AUFGERUFEN, WENN MAN IN BIEDEN
            // SLOTS DAS ITEM H�LT

            if ((isMainHand || isOffHand) && (isMainHand != isOffHand)) {

                if (player.isSneaking()) {

                    if (player.hasPermission("chestcleaner.cleaningItem.use.owninventory")) {
                        if (!CooldownTimer.isPlayerAllowedToUseSort(player)) {
                            return;
                        }

                        damageItem(player, isMainHand);
                        InventorySorter.sortPlayerInv(
                            player, PlayerDataManager.getSortingPatternOfPlayer(player),
                            PlayerDataManager.getEvaluatorTypOfPlayer(
                                player));
                        InventorySorter.playSortingSound(player);

                        MessageSystem
                            .sendMessageToPlayer(MessageType.SUCCESS, MessageID.INVENTORY_SORTED,
                                player);

                        playerInteractEvent.setCancelled(true);
                    }

                } else if (!pluginConfiguration.isOpenInventoryEventDetectionModeActive()) {

                    if (player.hasPermission("chestcleaner.cleaningItem.use")) {

                        Block block = BlockDetector.getTargetBlock(player);

                        if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
                            return;
                        }

                        if (!CooldownTimer.isPlayerAllowedToUseSort(player)) {
                            return;
                        }

                        if (InventorySorter.sortPlayerBlock(block,
                            player, PlayerDataManager.getSortingPatternOfPlayer(player),
                            PlayerDataManager.getEvaluatorTypOfPlayer(
                                player))) {

                            damageItem(player, isMainHand);

                            MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                                Messages.getMessage(MessageID.INVENTORY_SORTED), player);
                            playerInteractEvent.setCancelled(true);
                        }

                    }

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
    private void damageItem(final Player player, final boolean isHoldingItemInMainHand) {

        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();

        if (pluginConfiguration.isDurabilityLossActive()) {

            ItemStack item;
            if (isHoldingItemInMainHand) {
                item = player.getInventory().getItemInMainHand();
            } else {
                item = player.getInventory().getItemInOffHand();
            }

            if (item.getMaxStackSize() == 1) {
                item.setDurability((short) (item.getDurability() + 1));
            }

            if (item.getDurability() >= item.getType().getMaxDurability()) {
                item.setAmount(item.getAmount() - 1);
            }
        }

    }

    @EventHandler
    private void onOpenInventory(InventoryOpenEvent inventoryOpenEvent) {

        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();
        final ItemStack currentCleaningItem = pluginConfiguration.getCurrentCleaningItem();

        if (pluginConfiguration.isOpenInventoryEventDetectionModeActive()) {
            Player player = (Player) inventoryOpenEvent.getPlayer();

            if (player.hasPermission("chestcleaner.cleaningItem.use")) {

                ItemStack itemMainHand = player.getInventory().getItemInMainHand().clone();
                itemMainHand.setDurability((short) 0);
                itemMainHand.setAmount(1);

                ItemStack itemOffHand = player.getInventory().getItemInOffHand().clone();
                itemOffHand.setDurability((short) 0);
                itemOffHand.setAmount(1);

                boolean isMainHand = itemMainHand.equals(currentCleaningItem);
                boolean isOffHand = itemOffHand.equals(currentCleaningItem);

                if (isMainHand || isOffHand) {

                    if (!CooldownTimer.isPlayerAllowedToUseSort(player)) {
                        return;
                    }

                    InventorySorter.sortInventory(inventoryOpenEvent.getInventory(),
                        PlayerDataManager.getSortingPatternOfPlayer(player),
                        PlayerDataManager.getEvaluatorTypOfPlayer(player));
                    InventorySorter.playSortingSound(player);

                    damageItem(player, isMainHand);

                    inventoryOpenEvent.setCancelled(true);
                    MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
                        Messages.getMessage(MessageID.INVENTORY_SORTED), player);
                }

            }

        }

    }

    @EventHandler
    private void onCloseInventory(final InventoryCloseEvent inventoryCloseEvent) {

        if (isInventoryCloseEventCausedByChest(inventoryCloseEvent)) {
            final Player player = (Player) inventoryCloseEvent.getPlayer();

            if (PlayerDataManager.getAutoSortConfigurationOfPlayer(player)) {

                if (!CooldownTimer.isPlayerAllowedToUseSort(player)) {
                    return;
                }

                InventorySorter.sortInventoryByPlayer(inventoryCloseEvent.getInventory(), player);
                InventorySorter.playSortingSound(player);
                MessageSystem
                    .sendMessageToPlayer(MessageType.SUCCESS, MessageID.INVENTORY_SORTED, player);
            }
        }
    }

    private boolean isInventoryCloseEventCausedByChest(InventoryCloseEvent inventoryCloseEvent) {
        return inventoryCloseEvent.getInventory().getHolder() instanceof Chest;
    }

}
