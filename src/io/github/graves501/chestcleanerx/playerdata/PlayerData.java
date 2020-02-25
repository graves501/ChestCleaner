package io.github.graves501.chestcleanerx.playerdata;

import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

    public static File ConfigFile = new File("plugins/ChestCleanerX", "playerdata.yml");
    public static FileConfiguration Config = YamlConfiguration.loadConfiguration(ConfigFile);

    /**
     * Saves this {@code FileConfiguration} to the the io.github.graves501.chestcleaner folder. If
     * the file does not exist, it will be created. If already exists, it will be overwritten.
     *
     * This method will save using the system default encoding, or possibly using UTF8.
     */
    public static void save() {

        try {
            Config.save(ConfigFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    /* SORTINGPATTERN */
    public static void setSortingPattern(SortingPattern pattern, Player player) {
        Config.set(player.getUniqueId() + ".sortingpattern", pattern.name());
        save();
    }

    public static SortingPattern getSortingPattern(Player player) {
        return SortingPattern
            .getSortingPatternByName(Config.getString(player.getUniqueId() + ".sortingpattern"));
    }

    public static void setEvaluatorTyp(EvaluatorType pattern, Player player) {
        Config.set(player.getUniqueId() + ".evaluatortyp", pattern.name());
        save();
    }

    public static EvaluatorType getEvaluatorType(Player player) {
        return EvaluatorType
            .getEvaluatorTypByName(Config.getString(player.getUniqueId() + ".evaluatortyp"));
    }

    public static void setAutoSort(boolean enableAutoSort, Player player) {
        Config.set(player.getUniqueId() + ".autosort", enableAutoSort);
        save();
    }

    public static boolean containsAutoSort(Player player) {
        return Config.contains(player.getUniqueId() + ".autosort");
    }

    public static boolean getAutoSort(Player player) {
        return Config.getBoolean(player.getUniqueId() + ".autosort");
    }

}
