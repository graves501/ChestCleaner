package io.github.graves501.chestcleanerx.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryDetector {

    private InventoryDetector() {
    }

    private static int HOTBAR_SLOTS_LOWER_BOUND = 0;
    private static int HOTBAR_SLOTS_EXCLUSIVE_UPPER_BOUND = 9;

    private static int MAIN_INVENTORY_SLOTS_LOWER_BOUND = 9;
    private static int MAIN_INVENTORY_SLOTS_EXCLUSIVE_UPPER_BOUND = 36;

    private static int HOTBAR_SIZE = 9;
    private static int MAIN_INVENTORY_SIZE = 27;
    private static int HOTBAR_PLUS_MAIN_INVENTORY_SIZE = HOTBAR_SIZE + MAIN_INVENTORY_SIZE;

    /**
     * <b>Returns the inventory of the block {@code b}. If there is no inventory
     * it will return {@code null}.</b> This method checks if the object of the block is an instance
     * of the class org.bukkit.block.Container and returns its inventory.
     *
     * @param block The Block you want to get the inventory form.
     * @return Returns the inventory of the container of the block, if its has no container it
     * returns {@code null}.
     */
    public static Inventory getInventoryFromBlock(final Block block) {
        if (block.getState() instanceof InventoryHolder) {
            InventoryHolder inventoryHolder = (InventoryHolder) block.getState();
            return inventoryHolder.getInventory();
        }
        return null;
    }

    public static boolean hasInventoryHolder(final Block block) {
        return block.getState() instanceof InventoryHolder;
    }

    /**
     * <b>Returns the inventory of the block on the location {@code location} in
     * the world {@code world}. If the block has no inventory it will return {@code null}.</b> This
     * method checks if the object of the block is an instance of the class
     * org.bukkit.block.Container and returns its inventory.
     *
     * @param location The location of the block you want to get the inventory from.
     * @param world The world of the block you want to get the inventory form.
     * @return Returns the inventory of the container of the block, if its has no container it
     * returns {@code null}.
     */
    public static Inventory getInventoryFromLocation(Location location, World world) {
        return getInventoryFromBlock(world.getBlockAt(location));
    }

    /**
     * <b>Return the main part of the player inventory, that means a list of all
     * ItemStacks form the slots with the index 9 (including) to index 35 (including).</b>That means
     * the hotbar, armor slots or second hand slot are getting avoided.
     *
     * @param player The owner of the inventory.
     * @return A list of all items from the inventory of {@code p} (from index 9 (including) to
     * index 35 (including))
     * @throws IllegalArgumentException if {@code player} is null.
     */
    public static List<ItemStack> getPlayerMainInventoryList(Player player) {

        if (player == null) {
            throw new IllegalArgumentException();
        }

        final PlayerInventory playerInventory = player.getInventory();

        ArrayList<ItemStack> playerMainInventoryItems = new ArrayList<>();

        for (int inventoryIndex = MAIN_INVENTORY_SLOTS_LOWER_BOUND;
            inventoryIndex < MAIN_INVENTORY_SLOTS_EXCLUSIVE_UPPER_BOUND; inventoryIndex++) {
            if (playerInventory.getItem(inventoryIndex) != null) {
                playerMainInventoryItems.add(playerInventory.getItem(inventoryIndex).clone());
            }
        }

        return playerMainInventoryItems;
    }

    public static ItemStack[] getHotbarAndMainInventoryItems(final Inventory inventory) {

        if (inventory == null) {
            throw new IllegalArgumentException();
        }

        ItemStack[] hotbarAndMainInventoryItems = new ItemStack[HOTBAR_PLUS_MAIN_INVENTORY_SIZE];

        for (int i = 0; i < hotbarAndMainInventoryItems.length; i++) {
            hotbarAndMainInventoryItems[i] = inventory.getItem(i);
        }

        return hotbarAndMainInventoryItems;
    }

}
