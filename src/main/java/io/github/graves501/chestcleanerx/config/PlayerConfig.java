package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import io.github.graves501.chestcleanerx.util.constant.PlayerProperty;
import io.github.graves501.chestcleanerx.util.constant.Property;
import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import lombok.Data;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

@Data
public class PlayerConfig extends ConfigManager {

    private static PlayerConfig instance = new PlayerConfig();

    private HashMap<UUID, ItemEvaluatorType> playerItemEvaluatorTypeMap = new HashMap<>();
    private HashMap<UUID, SortingPattern> playerSortingPatternMap = new HashMap<>();
    private HashMap<UUID, Boolean> playerAutoSortChestMap = new HashMap<>();

    private PlayerConfig() {
        this.configFile = new File(
            Property.PLUGIN_FILE_PATH.getString(),
            Property.PLAYER_DATA_YAML_CONFIG_FILE_NAME.getString());

        this.yamlConfig = YamlConfiguration
            .loadConfiguration(this.configFile);
    }

    public static PlayerConfig getInstance() {
        return instance;
    }

    public void loadPlayerData(Player player) {
        final SortingPattern pattern = getSortingPatternFromConfig(player);
        final ItemEvaluatorType evaluator = getEvaluatorTypeFromConfig(player);
        boolean isAutoSortChestOnClosingActive = getIsAutoSortChestOnClosingActiveFromConfig(
            player);

        if (pattern != null) {
            playerSortingPatternMap.put(player.getUniqueId(), pattern);
        }

        if (evaluator != null) {
            playerItemEvaluatorTypeMap.put(player.getUniqueId(), evaluator);
        }

        if (!containsIsAutoSortChestOnClosingActive(player)) {
            isAutoSortChestOnClosingActive = PluginConfig.getInstance()
                .isAutoSortChestActive();
        }

        playerAutoSortChestMap.put(player.getUniqueId(), isAutoSortChestOnClosingActive);
    }


    public void setAndSaveSortingPattern(SortingPattern pattern, Player player) {
        yamlConfig
            .set(player.getUniqueId() + PlayerProperty.SORTING_PATTERN.getString(), pattern.name());
        saveOrOverwriteConfigToFile();
    }

    public SortingPattern getSortingPatternFromConfig(Player player) {
        return SortingPattern
            .getSortingPatternByName(
                yamlConfig
                    .getString(player.getUniqueId() + PlayerProperty.SORTING_PATTERN.getString()));
    }

    public void setAndSaveEvaluatorType(ItemEvaluatorType pattern, Player player) {
        yamlConfig
            .set(player.getUniqueId() + PlayerProperty.EVALUATOR_TYPE.getString(), pattern.name());
        saveOrOverwriteConfigToFile();
    }

    public ItemEvaluatorType getEvaluatorTypeFromConfig(Player player) {
        return ItemEvaluatorType
            .getEvaluatorTypeByName(
                yamlConfig
                    .getString(player.getUniqueId() + PlayerProperty.EVALUATOR_TYPE.getString()));
    }

    public void setAndSaveIsAutoSortChestOnClosingActive(final Player player,
        final boolean isActive) {
        yamlConfig
            .set(player.getUniqueId() + PlayerProperty.AUTO_SORT_CHEST.getString(), isActive);
        saveOrOverwriteConfigToFile();
    }

    public boolean containsIsAutoSortChestOnClosingActive(Player player) {
        return yamlConfig
            .contains(player.getUniqueId() + PlayerProperty.AUTO_SORT_CHEST.getString());
    }

    public boolean getIsAutoSortChestOnClosingActiveFromConfig(Player player) {
        return yamlConfig
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
            : PluginConfig.getInstance().getDefaultItemEvaluatorType();
    }

    public SortingPattern getSortingPatternOfPlayer(Player player) {
        return playerSortingPatternMap.get(player) == null ? playerSortingPatternMap
            .get(player.getUniqueId())
            : PluginConfig.getInstance().getDefaultSortingPattern();
    }

    public boolean getAutoSortChestConfigOfPlayer(Player player) {
        return playerAutoSortChestMap.containsKey(player.getUniqueId()) ? playerAutoSortChestMap
            .get(player.getUniqueId())
            : PluginConfig.getInstance().isAutoSortChestActive();
    }

}
