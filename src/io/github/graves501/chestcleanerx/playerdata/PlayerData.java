package io.github.graves501.chestcleanerx.playerdata;

import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import io.github.graves501.chestcleanerx.utils.enums.Property;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {

    public static File playerConfigurationFile = new File(Property.PLUGIN_FILE_PATH.getString(),
        Property.PLAYER_DATA_YAML_CONFIG_FILE_NAME.getString());

    public static FileConfiguration playerDataYamlConfiguration = YamlConfiguration
        .loadConfiguration(playerConfigurationFile);

    /**
     * Saves this {@code FileConfiguration} to the the io.github.graves501.chestcleaner folder. If
     * the file does not exist, it will be created. If already exists, it will be overwritten.
     *
     * This method will save using the system default encoding, or possibly using UTF8.
     */
    public static void save() {

        try {
            playerDataYamlConfiguration.save(playerConfigurationFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    /* SORTINGPATTERN */
    public static void setSortingPattern(SortingPattern pattern, Player player) {
        playerDataYamlConfiguration.set(player.getUniqueId() + ".sortingpattern", pattern.name());
        save();
    }

    public static SortingPattern getSortingPattern(Player player) {
        return SortingPattern
            .getSortingPatternByName(
                playerDataYamlConfiguration.getString(player.getUniqueId() + ".sortingpattern"));
    }

    public static void setEvaluatorTyp(EvaluatorType pattern, Player player) {
        playerDataYamlConfiguration.set(player.getUniqueId() + ".evaluatortyp", pattern.name());
        save();
    }

    public static EvaluatorType getEvaluatorType(Player player) {
        return EvaluatorType
            .getEvaluatorTypeByName(
                playerDataYamlConfiguration.getString(player.getUniqueId() + ".evaluatortyp"));
    }

    public static void setAutoSort(boolean enableAutoSort, Player player) {
        playerDataYamlConfiguration.set(player.getUniqueId() + ".autosort", enableAutoSort);
        save();
    }

    public static boolean containsAutoSort(Player player) {
        return playerDataYamlConfiguration.contains(player.getUniqueId() + ".autosort");
    }

    public static boolean getAutoSort(Player player) {
        return playerDataYamlConfiguration.getBoolean(player.getUniqueId() + ".autosort");
    }

}
