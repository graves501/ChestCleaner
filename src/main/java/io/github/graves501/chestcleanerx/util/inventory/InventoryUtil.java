package io.github.graves501.chestcleanerx.util.inventory;

import io.github.graves501.chestcleanerx.configuration.PluginConfiguration;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author graves501
 */
public class InventoryUtil {

    private InventoryUtil() {
    }

    public static ItemStack getItemInMainHand(final Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public static ItemStack getItemInOffHand(final Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public static boolean isPlayerHoldingCleaningItemInAHand(final Player player) {
        return isCleaningItemInMainHand(player) || isCleaningItemInOffHand(player);
    }

    public static boolean isPlayerHoldingCleaningItemInBothHands(final Player player) {
        return isCleaningItemInMainHand(player) && isCleaningItemInOffHand(player);
    }

    public static ItemStack getComparableItemInMainHand(final Player player) {
        return getComparableItem(player.getInventory().getItemInMainHand());
    }

    public static ItemStack getComparableItemInOffHand(final Player player) {
        return getComparableItem(player.getInventory().getItemInOffHand());
    }

    public static boolean isCleaningItemInMainHand(final Player player) {
        final ItemStack itemInMainHand = getComparableItemInMainHand(player);
        return isItemSimilarToCurrentCleaningItem(itemInMainHand);
    }

    public static boolean isCleaningItemInOffHand(final Player player) {
        final ItemStack itemInOffHand = getComparableItemInOffHand(player);
        return isItemSimilarToCurrentCleaningItem(itemInOffHand);
    }

    private static boolean isItemSimilarToCurrentCleaningItem(final ItemStack item) {
        final ItemStack currentCleaningItem = PluginConfiguration.getInstance()
            .getCurrentCleaningItem();
        return item.isSimilar(currentCleaningItem);
    }

    private static ItemStack getComparableItem(final ItemStack item) {
        final ItemStack comparableItem = item.clone();

        if (comparableItem.hasItemMeta()) {
            final Damageable damageOfComparableItem = (Damageable) comparableItem.getItemMeta();
            if (damageOfComparableItem != null) {
                damageOfComparableItem.setDamage(0);
                comparableItem.setItemMeta((ItemMeta) damageOfComparableItem);
            }
        }

        return comparableItem;
    }

    public static boolean isPlayerRightClickingAirOrBlock(
        final PlayerInteractEvent playerRightClickEvent) {

        boolean isPlayerRightClickingAir =
            playerRightClickEvent.getAction() == Action.RIGHT_CLICK_AIR;

        boolean isPlayerRightClickingBlock =
            playerRightClickEvent.getAction() == Action.RIGHT_CLICK_BLOCK;

        return isPlayerRightClickingAir || isPlayerRightClickingBlock;
    }

    public static boolean isInventoryCloseEventCausedByChest(InventoryCloseEvent inventoryCloseEvent) {
        return inventoryCloseEvent.getInventory().getHolder() instanceof Chest;
    }

    /**
     * Damages the item in the Hand of the {@code player} (using player.getItemInHand()), if the
     * {@code durability} (in class Main) is true. Damaging means, that stackable items
     * (maxStackSize > 1) get reduced in amount by one, not stackable items get damaged and removed,
     * if they reach the highest durability .
     *
     * @param player the player who is holding the item, that you want to get damaged, in hand.
     */
    public static void damageCleaningItemOfPlayer(final Player player) {
        if (PluginConfiguration.getInstance().isDurabilityLossActive()) {
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

    public static void preventConsumptionOfCleaningItem(Cancellable cancellableEvent) {
        cancellableEvent.setCancelled(true);
    }

}
