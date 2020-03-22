package io.github.graves501.chestcleanerx.sorting;

import io.github.graves501.chestcleanerx.config.PluginConfig;
import io.github.graves501.chestcleanerx.sorting.evaluator.Evaluator;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class Quicksort {

    private List<ItemStack> items;

    private Evaluator defaultEvaluator = ItemEvaluatorType
        .getEvaluator(PluginConfig.getInstance().getDefaultItemEvaluatorType());

    public Quicksort(List<ItemStack> items, Evaluator evaluator) {
        this.items = items;
        if (evaluator != null) {
            this.defaultEvaluator = evaluator;
        }
    }

    public List<ItemStack> sort(int left, int right) {
        int q;
        if (left < right) {
            q = partition(left, right);
            sort(left, q);
            sort(q + 1, right);
        }
        return items;
    }

    private int partition(int left, int right) {

        int i = left - 1;
        int j = right + 1;
        ItemStack item = items.get((left + right) / 2);
        while (true) {
            do {
                i++;
            } while (defaultEvaluator.isSmallerThan(items.get(i), item));

            do {
                j--;
            } while (defaultEvaluator.isGreaterThan(items.get(j), item));

            if (i < j) {
                ItemStack k = items.get(i);
                items.set(i, items.get(j));
                items.set(j, k);
            } else {
                return j;
            }
        }
    }

}
