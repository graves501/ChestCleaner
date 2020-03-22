package io.github.graves501.chestcleanerx.sorting;

import io.github.graves501.chestcleanerx.config.PlayerConfig;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import io.github.graves501.chestcleanerx.util.InventoryConverter;
import io.github.graves501.chestcleanerx.util.InventoryDetector;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySorter {

    public static List<Material> blacklist = new ArrayList<>();

    /**
     * Returns {@code list} with full stacked items.
     *
     * @return full stacked {@code list};
     */
    private static List<ItemStack> getFullStacks(List<ItemStack> list) {

        List<ItemStack> items = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();

        boolean blackListedItemUsed = false;

        for (int i = 0; i < list.size(); i++) {

            ItemStack item = list.get(i);
            int amount = item.getAmount();

            item.setAmount(1);

            if (blacklist.contains(list.get(i).getType())) {
                items.add(item);
                amounts.add(amount);
                blackListedItemUsed = true;
            } else {

                int index = -1;
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).isSimilar(list.get(i))) {
                        index = j;
                        break;
                    }
                }

                if (index >= 0) {
                    amounts.set(index, amounts.get(index) + amount);
                } else {
                    items.add(item);
                    amounts.add(amount);
                }
            }

        }

        ArrayList<ItemStack> out = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            int stacks = (amounts.get(i) / items.get(i).getType().getMaxStackSize());
            for (int j = 0; j < stacks; j++) {
                ItemStack item = items.get(i).clone();
                item.setAmount(items.get(i).getMaxStackSize());
                out.add(item);
            }

            int remainingItems = amounts.get(i) % items.get(i).getMaxStackSize();
            if (remainingItems > 0) {
                ItemStack item = items.get(i).clone();
                item.setAmount(remainingItems);
                out.add(item);
            }

        }

        if (blackListedItemUsed) {
            AmountSorter sorter = new AmountSorter(out);
            return sorter.sortArray();
        }

        return out;

    }

    /**
     * Sorts any kind of inventory.
     *
     * @param inventory the inventory you want to sort.
     */
    public static void sortInventory(Inventory inventory, SortingPattern pattern,
        ItemEvaluatorType evaluator) {

        List<ItemStack> list = InventoryConverter.getArrayListFormInventory(inventory);
        List<ItemStack> temp = new ArrayList<ItemStack>();

        if (list.size() <= 1) {
            InventoryConverter.setItemsOfInventory(inventory, list, false, pattern);
        }

        Quicksort sorter = new Quicksort(list, ItemEvaluatorType.getEvaluator(evaluator));
        temp = sorter.sort(0, list.size() - 1);
        List<ItemStack> out = getFullStacks(temp);

        InventoryConverter.setItemsOfInventory(inventory, out, false, pattern);

    }

    /**
     * Sorts a part of the inventory of a player. It sorts the slots with the index 9 to 35, that
     * means the hotbar, armor slot and the extra item slot are not effected.
     *
     * @param player The player whose inventory you want to sort.
     */
    public static void sortPlayerInventory(Player player, SortingPattern sortingPattern,
        ItemEvaluatorType itemEvaluatorType) {

        List<ItemStack> playerMainInventoryList = InventoryDetector
            .getPlayerMainInventoryList(player);
        List<ItemStack> temporaryItemList;

        if (playerMainInventoryList.size() <= 1) {
            InventoryConverter.setPlayerInventory(playerMainInventoryList, player, sortingPattern);
        }

        Quicksort inventorySorter = new Quicksort(playerMainInventoryList,
            ItemEvaluatorType.getEvaluator(
                itemEvaluatorType));
        temporaryItemList = inventorySorter.sort(0, playerMainInventoryList.size() - 1);
        final List<ItemStack> sortedPlayerInventory = getFullStacks(temporaryItemList);

        InventoryConverter.setPlayerInventory(sortedPlayerInventory, player, sortingPattern);
    }

    /**
     * Checks if the block has an inventory or if it is an enderchest and sorts it.
     *
     * @param player the player or owner of an enderchest inventory.
     * @param blockSelectedByPlayer Block you want to get sorted.
     * @return returns true if an inventory got sorted, otherwise false.
     */
    public static boolean sortBlockSelectedByPlayer(final Player player,
        final Block blockSelectedByPlayer,
        final SortingPattern pattern,
        final ItemEvaluatorType evaluator) {

        Inventory inventory = InventoryDetector.getInventoryOfBlock(blockSelectedByPlayer);

        if (inventory != null) {
            if (player != null) {
                playSortingSound(player);
            }
            sortInventory(inventory, pattern, evaluator);
            return true;
        }

        //TODO Should shulker boxes also be sorted?
        if (player != null) {
            if (blockSelectedByPlayer.getBlockData().getMaterial() == Material.ENDER_CHEST) {
                playSortingSound(player);
                sortInventory(player.getEnderChest(), pattern, evaluator);
                return true;
            }
        }

        return false;
    }

    /**
     * Sorts an inventory with the saved patterns of the player selected pattern and evaluator, if
     * nothing was selected it takes the default pattern and evaluator.
     *
     * @param inventory The inventory you want to sort.
     * @param player the player who is the owner of the sorting pattern and evaluator.
     */
    public static void sortInventoryOfPlayer(Inventory inventory, Player player) {

        final PlayerConfig playerConfig = PlayerConfig.getInstance();

        final SortingPattern pattern = playerConfig.getSortingPatternOfPlayer(player);
        final ItemEvaluatorType evaluator = playerConfig.getEvaluatorTypOfPlayer(player);

        sortInventory(inventory, pattern, evaluator);

    }

    public static void playSortingSound(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIG_SADDLE, 2F, 2F);
    }

}
