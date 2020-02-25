package io.github.graves501.chestcleanerx.sorting;

import io.github.graves501.chestcleanerx.sorting.evaluator.Evaluator;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class Quicksort {

    private List<ItemStack> items;

    //Default Evaluator
    public Evaluator evaluator = EvaluatorType.getEvaluator(EvaluatorType.DEFAULT);

    public Quicksort(List<ItemStack> items, Evaluator evaluator) {
        this.items = items;
        if (evaluator != null) {
            this.evaluator = evaluator;
        }
    }

    public List<ItemStack> sort(int l, int r) {
        int q;
        if (l < r) {
            q = partition(l, r);
            sort(l, q);
            sort(q + 1, r);
        }
        return items;
    }

    private int partition(int l, int r) {

        int i = l - 1;
        int j = r + 1;
        ItemStack item = items.get((l + r) / 2);
        while (true) {
            do {
                i++;
            } while (evaluator.isSmallerThan(items.get(i), item));

            do {
                j--;
            } while (evaluator.isGreaterThan(items.get(j), item));

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
