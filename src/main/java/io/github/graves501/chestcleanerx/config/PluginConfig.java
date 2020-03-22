package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.command.BlacklistCommand;
import io.github.graves501.chestcleanerx.main.ChestCleanerX;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import io.github.graves501.chestcleanerx.util.constant.Property;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author tom2208
 * @author graves501
 */
@Data
public class PluginConfig extends ConfigManager {

    private final Logger logger = JavaPlugin.getPlugin(ChestCleanerX.class).getLogger();

    // Default settings for the plugin
    private ItemStack currentCleaningItem = new ItemStack(Material.IRON_HOE);
    private int cooldownInSeconds = 5;
    private boolean isCleanInventoryCommandActive = true;
    private boolean isCooldownActive = false;
    private boolean isDurabilityLossActive = true;
    private boolean isCleaningItemActive = true;
    private boolean isOpenInventoryEventDetectionModeActive = false;
    private boolean isBlockRefillActive = true;
    private boolean isConsumablesRefillActive = true;
    private boolean isAutoSortChestActive = false;
    private ItemEvaluatorType defaultItemEvaluatorType = ItemEvaluatorType.BACK_BEGIN_STRING;
    private SortingPattern defaultSortingPattern = SortingPattern.LEFT_TO_RIGHT_TOP_TO_BOTTOM;

    private static PluginConfig instance = new PluginConfig();

    private PluginConfig() {
        super();
        this.configFile = new File(
            Property.PLUGIN_FILE_PATH.getString(),
            Property.PLUGIN_YAML_CONFIG_FILE_NAME.getString());
        this.yamlConfig = YamlConfiguration.loadConfiguration(
            configFile);
    }

    public static PluginConfig getInstance() {
        return instance;
    }

    /**
     * Loads all variables out of the config, if the config does not exist it will generate one with
     * the default values for the variables.
     */
    public void loadConfig() {
        loadCleaningItem();
        loadIsAutoSortingActive();
        loadDefaultEvaluatorType();
        loadDefaultSortingPattern();
        loadIsCleaningItemActive();
        loadIsCooldownTimerActive();
        loadIsDurabilityLossActive();
        loadIsOpenInventoryEventDetectionModeActive();
        loadIsConsumablesRefillActive();
        loadIsBlockRefillActive();
        loadIsCleanInventoryCommandActive();

        // TODO fix blacklist functionality first
        // loadSortingBlacklist();
        // loadInventoryBlacklist();
    }

    private void loadIsAutoSortingActive() {
        if (configContainsProperty(Property.AUTOSORT_CHEST_ACTIVE)) {
            this.isAutoSortChestActive = getBooleanProperty(
                Property.AUTOSORT_CHEST_ACTIVE);
        }
        this.yamlConfig.set(Property.AUTOSORT_CHEST_ACTIVE.getString(), isAutoSortChestActive);
    }

    private void loadDefaultEvaluatorType() {
        if (configContainsProperty(Property.DEFAULT_ITEM_EVALUATOR)) {
            getStringProperty(Property.DEFAULT_ITEM_EVALUATOR);
            this.defaultItemEvaluatorType = getDefaultEvaluatorTypeFromConfig();
        }
        this.yamlConfig
            .set(Property.DEFAULT_ITEM_EVALUATOR.getString(), defaultItemEvaluatorType.name());
    }

    private void loadDefaultSortingPattern() {
        if (configContainsProperty(Property.DEFAULT_SORTING_PATTERN)) {
            this.defaultSortingPattern = getDefaultSortingPatternFromConfig();
        }
        this.yamlConfig
            .set(Property.DEFAULT_SORTING_PATTERN.getString(), defaultSortingPattern.name());
    }

    private void loadCleaningItem() {
        if (configContainsProperty(Property.CLEANING_ITEM)
            && getCleaningItemFromConfig() != null) {
            this.currentCleaningItem = getCleaningItemFromConfig();
        }
        this.yamlConfig.set(Property.CLEANING_ITEM.getString(), this.currentCleaningItem);
    }

    private void loadIsCleaningItemActive() {
        if (configContainsProperty(Property.CLEANING_ITEM_ACTIVE)) {
            this.isCleaningItemActive = getBooleanProperty(Property.CLEANING_ITEM_ACTIVE);
        }
        this.yamlConfig.set(Property.CLEANING_ITEM_ACTIVE.getString(), this.isCleaningItemActive);
    }

    private void loadIsCooldownTimerActive() {
        if (configContainsProperty(Property.COOLDOWN_ACTIVE)) {
            this.isCooldownActive = getBooleanProperty(Property.COOLDOWN_ACTIVE);
        }
        this.yamlConfig.set(Property.COOLDOWN_ACTIVE.getString(), this.isCooldownActive);
    }

    private void loadIsDurabilityLossActive() {
        if (configContainsProperty(Property.DURABILITY_LOSS)) {
            this.isDurabilityLossActive = getBooleanProperty(Property.DURABILITY_LOSS);
        }
        this.yamlConfig.set(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE.getString(),
            this.isOpenInventoryEventDetectionModeActive);
    }

    private void loadIsOpenInventoryEventDetectionModeActive() {
        if (configContainsProperty(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE)) {
            this.isOpenInventoryEventDetectionModeActive = getBooleanProperty(
                Property.OPEN_INVENTORY_EVENT_DETECTION_MODE);
        }
        this.yamlConfig.set(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE.getString(),
            this.isOpenInventoryEventDetectionModeActive);
    }

    private void loadIsConsumablesRefillActive() {
        if (configContainsProperty(Property.CONSUMABLES_REFILL)) {
            this.isConsumablesRefillActive = getBooleanProperty(Property.CONSUMABLES_REFILL);
        }
        this.yamlConfig
            .set(Property.CONSUMABLES_REFILL.getString(), this.isConsumablesRefillActive);
    }

    private void loadIsBlockRefillActive() {
        if (configContainsProperty(Property.BLOCK_REFILL)) {
            this.isBlockRefillActive = getBooleanProperty(Property.BLOCK_REFILL);
        }
        this.yamlConfig.set(Property.BLOCK_REFILL.getString(), this.isBlockRefillActive);
    }

    private void loadIsCleanInventoryCommandActive() {
        if (configContainsProperty(Property.CLEAN_INVENTORY_COMMAND_ACTIVE)) {
            this.isCleanInventoryCommandActive = getBooleanProperty(
                Property.CLEAN_INVENTORY_COMMAND_ACTIVE);
        }
        this.yamlConfig.set(Property.CLEAN_INVENTORY_COMMAND_ACTIVE.getString(),
            this.isCleanInventoryCommandActive);
    }

    private void loadSortingBlacklist() {
        if (configContainsProperty(Property.SORTING_BLACKLIST)) {
            //TODO make list non-static
            InventorySorter.blacklist = getSortingBlackListFromConfig();
        }
    }

    private void loadInventoryBlacklist() {
        if (configContainsProperty(Property.INVENTORY_BLACKLIST)) {
            //TODO make list non-static
            BlacklistCommand.inventoryBlacklist = getInventoryBlackListFromConfig();
        }
    }

    public void setAndSaveCurrentCleaningItem(ItemStack currentCleaningItem) {
        this.currentCleaningItem = currentCleaningItem;
        yamlConfig.set(Property.CLEANING_ITEM.getString(), this.currentCleaningItem);
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveDefaultItemEvaluatorType(ItemEvaluatorType itemEvaluatorType) {
        this.defaultItemEvaluatorType = itemEvaluatorType;
        yamlConfig.set(Property.DEFAULT_ITEM_EVALUATOR.getString(), itemEvaluatorType.name());
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveDefaultSortingPattern(SortingPattern defaultSortingPattern) {
        this.defaultSortingPattern = defaultSortingPattern;
        yamlConfig
            .set(Property.DEFAULT_SORTING_PATTERN.getString(), this.defaultSortingPattern.name());
        saveOrOverwriteConfigToFile();
    }

    public ItemEvaluatorType getDefaultEvaluatorTypeFromConfig() {
        return ItemEvaluatorType
            .getEvaluatorTypeByName(getStringProperty(Property.DEFAULT_ITEM_EVALUATOR));
    }

    public SortingPattern getDefaultSortingPatternFromConfig() {
        return SortingPattern
            .getSortingPatternByName(getStringProperty(Property.DEFAULT_SORTING_PATTERN));
    }

    public void setAndSaveDurabilityLossActive(boolean isDurabilityLossActive) {
        this.isDurabilityLossActive = isDurabilityLossActive;
        yamlConfig.set(Property.DURABILITY_LOSS.getString(), this.isDurabilityLossActive);
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveOpenInventoryEventDetectionModeActive(
        boolean isOpenInventoryEventDetectionModeActive) {

        this.isOpenInventoryEventDetectionModeActive = isOpenInventoryEventDetectionModeActive;
        yamlConfig.set(Property.DURABILITY_LOSS.getString(),
            this.isOpenInventoryEventDetectionModeActive);
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveIsAutoSortChestActive(boolean isAutoSortChestActive) {
        this.isAutoSortChestActive = isAutoSortChestActive;
        yamlConfig.set(Property.AUTOSORT_CHEST_ACTIVE.getString(), this.isAutoSortChestActive);
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveCooldownInSeconds(int cooldownInSeconds) {
        this.cooldownInSeconds = cooldownInSeconds;
        yamlConfig.set(Property.COOLDOWN_IN_SECONDS.getString(), this.cooldownInSeconds);
        saveOrOverwriteConfigToFile();
    }

    private ItemStack getCleaningItemFromConfig() {
        return yamlConfig.getItemStack(Property.CLEANING_ITEM.getString());
    }

    public void setSortingBlacklist(final List<Material> blacklist) {
        List<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }

        yamlConfig.set(Property.SORTING_BLACKLIST.getString(), list);
    }

    public List<Material> getSortingBlackListFromConfig() {
        List<String> list = yamlConfig.getStringList(Property.SORTING_BLACKLIST.getString());
        List<Material> materials = new ArrayList<>();

        for (String name : list) {
            materials.add(Material.getMaterial(name));
        }

        return materials;
    }

    public void setInventoryBlackList(final List<Material> blacklist) {
        ArrayList<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }

        yamlConfig.set(Property.INVENTORY_BLACKLIST.getString(), list);
    }

    public List<Material> getInventoryBlackListFromConfig() {
        List<String> inventoryBlacklist = yamlConfig
            .getStringList(Property.INVENTORY_BLACKLIST.getString());
        ArrayList<Material> materials = new ArrayList<>();

        for (String blacklistItemName : inventoryBlacklist) {
            materials.add(Material.getMaterial(blacklistItemName));
        }

        return materials;
    }

    public void setAndSaveIsCleaningItemActive(boolean isCleaningItemActive) {
        this.isCleaningItemActive = isCleaningItemActive;
        yamlConfig.set(Property.CLEANING_ITEM_ACTIVE.getString(), this.isCleaningItemActive);
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveIsCooldownActive(boolean isCooldownActive) {
        this.isCooldownActive = isCooldownActive;
        yamlConfig.set(Property.COOLDOWN_ACTIVE.getString(), this.isCooldownActive);
    }

    public void setAndSaveIsCleanInventoryCommandActive(boolean isCleanInventoryCommandActive) {
        this.isCleanInventoryCommandActive = isCleanInventoryCommandActive;
        yamlConfig.set(Property.CLEAN_INVENTORY_COMMAND_ACTIVE.getString(), this.isCleanInventoryCommandActive);
    }

    public void setAndSaveIsBlockRefillActive(boolean isBlockRefillActive) {
        this.isBlockRefillActive = isBlockRefillActive;
        yamlConfig.set(Property.BLOCK_REFILL.getString(), this.isBlockRefillActive);
    }

    public void setAndSaveConsumablesRefillActive(boolean isConsumablesRefillActive) {
        this.isConsumablesRefillActive = isConsumablesRefillActive;
        yamlConfig.set(Property.CONSUMABLES_REFILL.getString(), this.isBlockRefillActive);
    }
}
