package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import io.github.graves501.chestcleanerx.utils.enums.PlayerProperty;
import io.github.graves501.chestcleanerx.utils.enums.Property;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import lombok.Data;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

@Data
public class PlayerConfiguration extends ConfigurationManager {

    private static PlayerConfiguration instance = new PlayerConfiguration();

    private HashMap<UUID, ItemEvaluatorType> playerItemEvaluatorTypeMap = new HashMap<>();
    private HashMap<UUID, SortingPattern> playerSortingPatternMap = new HashMap<>();
    private HashMap<UUID, Boolean> playerAutoSortChestMap = new HashMap<>();

    private PlayerConfiguration() {
        this.configurationFile = new File(
            Property.PLUGIN_FILE_PATH.getString(),
            Property.PLAYER_DATA_YAML_CONFIG_FILE_NAME.getString());

        this.yamlConfiguration = YamlConfiguration
            .loadConfiguration(this.configurationFile);
    }

    public static PlayerConfiguration getInstance() {
        return instance;
    }

    public void loadPlayerData(Player player) {
        final SortingPattern pattern = getSortingPatternFromConfiguration(player);
        final ItemEvaluatorType evaluator = getEvaluatorTypeFromConfiguration(player);
        boolean isAutoSortChestOnClosingActive = getIsAutoSortChestOnClosingActiveFromConfiguration(
            player);

        if (pattern != null) {
            playerSortingPatternMap.put(player.getUniqueId(), pattern);
        }

        if (evaluator != null) {
            playerItemEvaluatorTypeMap.put(player.getUniqueId(), evaluator);
        }

        if (!containsIsAutoSortChestOnClosingActive(player)) {
            isAutoSortChestOnClosingActive = PluginConfiguration.getInstance()
                .isDefaultAutoSortChestActive();
        }

        playerAutoSortChestMap.put(player.getUniqueId(), isAutoSortChestOnClosingActive);
    }


    /* SORTINGPATTERN */
    public void setAndSaveSortingPattern(SortingPattern pattern, Player player) {
        yamlConfiguration
            .set(player.getUniqueId() + PlayerProperty.SORTING_PATTERN.getString(), pattern.name());
        saveOrOverwriteConfigurationToFile();
    }

    public SortingPattern getSortingPatternFromConfiguration(Player player) {
        return SortingPattern
            .getSortingPatternByName(
                yamlConfiguration
                    .getString(player.getUniqueId() + PlayerProperty.SORTING_PATTERN.getString()));
    }

    public void setAndSaveEvaluatorType(ItemEvaluatorType pattern, Player player) {
        yamlConfiguration
            .set(player.getUniqueId() + PlayerProperty.EVALUATOR_TYPE.getString(), pattern.name());
        saveOrOverwriteConfigurationToFile();
    }

    public ItemEvaluatorType getEvaluatorTypeFromConfiguration(Player player) {
        return ItemEvaluatorType
            .getEvaluatorTypeByName(
                yamlConfiguration
                    .getString(player.getUniqueId() + PlayerProperty.EVALUATOR_TYPE.getString()));
    }

    public void setAndSaveIsAutoSortChestOnClosingActive(final Player player,
        final boolean isActive) {
        yamlConfiguration
            .set(player.getUniqueId() + PlayerProperty.AUTO_SORT_CHEST.getString(), isActive);
        saveOrOverwriteConfigurationToFile();
    }

    public boolean containsIsAutoSortChestOnClosingActive(Player player) {
        return yamlConfiguration
            .contains(player.getUniqueId() + PlayerProperty.AUTO_SORT_CHEST.getString());
    }

    public boolean getIsAutoSortChestOnClosingActiveFromConfiguration(Player player) {
        return yamlConfiguration
            .getBoolean(player.getUniqueId() + PlayerProperty.AUTO_SORT_CHEST.getString());
    }

    public void removePlayerDataFromMemory(Player player) {
        playerItemEvaluatorTypeMap.remove(player);
        playerSortingPatternMap.remove(player);
        playerAutoSortChestMap.remove(player);
    }

    public ItemEvaluatorType getEvaluatorTypOfPlayer(Player player) {
        return playerItemEvaluatorTypeMap.get(player) == null ? playerItemEvaluatorTypeMap
            .get(player.getUniqueId())
            : PluginConfiguration.getInstance().getDefaultItemEvaluatorType();
    }

    public SortingPattern getSortingPatternOfPlayer(Player player) {
        return playerSortingPatternMap.get(player) == null ? playerSortingPatternMap
            .get(player.getUniqueId())
            : PluginConfiguration.getInstance().getDefaultSortingPattern();
    }

    public boolean getAutoSortChestConfigurationOfPlayer(Player player) {
        return playerAutoSortChestMap.containsKey(player.getUniqueId()) ? playerAutoSortChestMap
            .get(player.getUniqueId())
            : PluginConfiguration.getInstance().isDefaultAutoSortChestActive();
    }

}
