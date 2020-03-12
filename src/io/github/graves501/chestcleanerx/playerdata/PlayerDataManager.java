package io.github.graves501.chestcleanerx.playerdata;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerDataManager {

    public static boolean defaultAutoSort = false;

    private static HashMap<UUID, EvaluatorType> playerEvaluator = new HashMap<>();
    private static HashMap<UUID, SortingPattern> playerPattern = new HashMap<>();
    private static HashMap<UUID, Boolean> playerAutoSort = new HashMap<>();

    public static void loadPlayerData(Player player) {
        SortingPattern pattern = PlayerData.getSortingPattern(player);
        EvaluatorType evaluator = PlayerData.getEvaluatorType(player);
        boolean autosort = PlayerData.getAutoSort(player);

        if (pattern != null) {
            playerPattern.put(player.getUniqueId(), pattern);
        }

        if (evaluator != null) {
            playerEvaluator.put(player.getUniqueId(), evaluator);
        }

        if (!PlayerData.containsAutoSort(player)) {
            autosort = defaultAutoSort;
        }

        playerAutoSort.put(player.getUniqueId(), autosort);

    }

    public static void removePlayerDataFromMemory(Player player) {
        playerEvaluator.remove(player);
        playerPattern.remove(player);
        playerAutoSort.remove(player);
    }

    public static EvaluatorType getEvaluatorTypOfPlayer(Player player) {
        return playerEvaluator.get(player) == null ? playerEvaluator.get(player.getUniqueId())
            : PluginConfiguration.getInstance().getDefaultEvaluatorType();
    }

    public static SortingPattern getSortingPatternOfPlayer(Player player) {
        return playerPattern.get(player) == null ? playerPattern.get(player.getUniqueId())
            : SortingPattern.DEFAULT;
    }

    public static boolean getAutoSortOfPlayer(Player player) {
        return playerAutoSort.containsKey(player.getUniqueId()) ? playerAutoSort
            .get(player.getUniqueId())
            : defaultAutoSort;
    }

}
