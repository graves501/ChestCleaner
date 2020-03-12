package io.github.graves501.chestcleanerx.playerdata;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerDataManager {


    private static boolean defaultAutoSorting = false;

    private PlayerDataManager() {
    }

    public static boolean isDefaultAutoSorting() {
        return defaultAutoSorting;
    }

    public static void setDefaultAutoSorting(boolean defaultAutoSorting) {
        PlayerDataManager.defaultAutoSorting = defaultAutoSorting;
    }

    private static HashMap<UUID, EvaluatorType> playerEvaluatorMap = new HashMap<>();
    private static HashMap<UUID, SortingPattern> playerPatternMap = new HashMap<>();
    private static HashMap<UUID, Boolean> playerAutoSortMap = new HashMap<>();

    public static void loadPlayerData(Player player) {
        SortingPattern pattern = PlayerData.getSortingPattern(player);
        EvaluatorType evaluator = PlayerData.getEvaluatorType(player);
        boolean autosort = PlayerData.getAutoSort(player);

        if (pattern != null) {
            playerPatternMap.put(player.getUniqueId(), pattern);
        }

        if (evaluator != null) {
            playerEvaluatorMap.put(player.getUniqueId(), evaluator);
        }

        if (!PlayerData.containsAutoSort(player)) {
            autosort = defaultAutoSorting;
        }

        playerAutoSortMap.put(player.getUniqueId(), autosort);

    }

    public static void removePlayerDataFromMemory(Player player) {
        playerEvaluatorMap.remove(player);
        playerPatternMap.remove(player);
        playerAutoSortMap.remove(player);
    }

    public static EvaluatorType getEvaluatorTypOfPlayer(Player player) {
        return playerEvaluatorMap.get(player) == null ? playerEvaluatorMap.get(player.getUniqueId())
            : PluginConfiguration.getInstance().getDefaultEvaluatorType();
    }

    public static SortingPattern getSortingPatternOfPlayer(Player player) {
        return playerPatternMap.get(player) == null ? playerPatternMap.get(player.getUniqueId())
            : SortingPattern.defaultSortingPattern;
    }

    public static boolean getAutoSortConfigurationOfPlayer(Player player) {
        return playerAutoSortMap.containsKey(player.getUniqueId()) ? playerAutoSortMap
            .get(player.getUniqueId())
            : defaultAutoSorting;
    }

}
