package io.github.graves501.chestcleanerx.listeners;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.config.PlayerConfiguration;
import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.main.PluginMain;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.CooldownTimer;
import io.github.graves501.chestcleanerx.utils.BlockDetector;
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

    @EventHandler
    private void onRightClick(PlayerInteractEvent playerInteractEvent) {

        final PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();
        final PlayerConfiguration playerConfiguration = PlayerConfiguration.getInstance();

        final ItemStack currentCleaningItem = pluginConfiguration.getCurrentCleaningItem();

        final Player player = playerInteractEvent.getPlayer();
        ItemStack itemMainHand = player.getInventory().getItemInMainHand().clone();
        itemMainHand.setDurability((short) 0);
        itemMainHand.setAmount(1);

        ItemStack itemOffHand = player.getInventory().getItemInOffHand().clone();
        itemOffHand.setDurability((short) 0);
        itemOffHand.setAmount(1);

        boolean isCleaningItemInMainHand = itemMainHand.equals(currentCleaningItem);
        logger.info("isCleaningItemInMainHand: " + isCleaningItemInMainHand);

        boolean isCleaningItemInOffHand = itemOffHand.equals(currentCleaningItem);
        logger.info("isCleaningItemInOffHand: " + isCleaningItemInOffHand);

        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR
            || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {

//            boolean isCleaningItemInMainHand = itemMainHand.equals(currentCleaningItem);
//            boolean isCleaningItemInOffHand = itemOffHand.equals(currentCleaningItem);

            // TODO clean up
            // TODO RIGHTCLICK WIRD WOHL ZWEI MAL AUFGERUFEN, WENN MAN IN BIEDEN
            // SLOTS DAS ITEM H�LT

            if ((isCleaningItemInMainHand || isCleaningItemInOffHand) && (isCleaningItemInMainHand
                != isCleaningItemInOffHand)) {

                if (player.isSneaking()) {

                    if (player.hasPermission("chestcleaner.cleaningItem.use.owninventory")) {
                        if (!CooldownTimer.isPlayerAllowedToUseSort(player)) {
                            return;
                        }

                        damageItem(player, isCleaningItemInMainHand);
                        InventorySorter.sortPlayerInventory(
                            player, playerConfiguration.getSortingPatternOfPlayer(player),
                            playerConfiguration.getEvaluatorTypOfPlayer(
                                player));
                        InventorySorter.playSortingSound(player);

                        InGameMessageHandler
                            .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                InGameMessage.INVENTORY_SORTED
                            );

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
                            player, playerConfiguration.getSortingPatternOfPlayer(player),
                            playerConfiguration.getEvaluatorTypOfPlayer(
                                player))) {

                            damageItem(player, isCleaningItemInMainHand);

                            InGameMessageHandler
                                .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                                    InGameMessage.INVENTORY_SORTED);

                            // TODO why setCancelled?
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
        final PlayerConfiguration playerConfiguration = PlayerConfiguration.getInstance();
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
                        playerConfiguration.getSortingPatternOfPlayer(player),
                        playerConfiguration.getEvaluatorTypOfPlayer(player));

                    InventorySorter.playSortingSound(player);

                    damageItem(player, isMainHand);

                    //TODO why set cancelled?
                    inventoryOpenEvent.setCancelled(true);
                    InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.INVENTORY_SORTED);
                }

            }

        }

    }

    @EventHandler
    private void onCloseInventory(final InventoryCloseEvent inventoryCloseEvent) {

        final PlayerConfiguration playerConfiguration = PlayerConfiguration.getInstance();

        if (isInventoryCloseEventCausedByChest(inventoryCloseEvent)) {
            final Player player = (Player) inventoryCloseEvent.getPlayer();

            if (playerConfiguration.getAutoSortChestConfigurationOfPlayer(player)) {

                if (!CooldownTimer.isPlayerAllowedToUseSort(player)) {
                    return;
                }

                InventorySorter.sortInventoryOfPlayer(inventoryCloseEvent.getInventory(), player);
                InventorySorter.playSortingSound(player);
                InGameMessageHandler
                    .sendMessageToPlayer(player, InGameMessageType.SUCCESS,
                        InGameMessage.INVENTORY_SORTED);
            }
        }
    }

    private boolean isInventoryCloseEventCausedByChest(InventoryCloseEvent inventoryCloseEvent) {
        return inventoryCloseEvent.getInventory().getHolder() instanceof Chest;
    }

}
