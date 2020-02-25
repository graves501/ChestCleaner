package io.github.graves501.chestcleanerx.sorting.evaluator;

import org.bukkit.inventory.ItemStack;

public class BeginBackStringEvaluator implements Evaluator {

    @Override
    public boolean isGreaterThan(ItemStack item1, ItemStack item2) {
        if (item1.getType().equals(item2.getType())) {
            return false;
        }

        String itemName1 = item1.getType().name();
        String itemName2 = item2.getType().name();

        for (int i = 0; i < Math.min(itemName1.length(), itemName2.length()); i++) {

            if (itemName1.charAt(i) > itemName2.charAt(i)) {
                return true;
            } else if (itemName1.charAt(i) != itemName2.charAt(i)) {
                return false;
            }

        }

        if (itemName1.length() > itemName2.length()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSmallerThan(ItemStack item1, ItemStack item2) {

        if (item1.getType().equals(item2.getType())) {
            return false;
        }

        String itemName1 = item1.getType().name();
        String itemName2 = item2.getType().name();

        for (int i = 0; i < Math.min(itemName1.length(), itemName2.length()); i++) {

            if (itemName1.charAt(i) < itemName2.charAt(i)) {
                return true;
            } else if (itemName1.charAt(i) != itemName2.charAt(i)) {
                return false;
            }

        }

        if (itemName1.length() < itemName2.length()) {
            return true;
        }
        return false;

    }

}
