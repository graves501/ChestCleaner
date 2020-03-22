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
import lombok.EqualsAndHashCode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class includes all methods to save and read game data (variables for this plugin).
 *
 * @author tom2208
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PluginConfig extends ConfigManager {

    private final Logger logger = JavaPlugin.getPlugin(ChestCleanerX.class).getLogger();

    // Default settings for the plugin
    private ItemStack currentCleaningItem;
    private int cooldownTimeInSeconds = 5;

    private boolean isCleanInventoryActive = true;

    // TODO set default to false...
    private boolean isCooldownTimerActive = true;
    private boolean isDurabilityLossActive = true;
    private boolean isCleaningItemActive = true;
    private boolean isOpenInventoryEventDetectionModeActive = false;
    private boolean isBlockRefillActive = true;
    private boolean isConsumablesRefillActive = true;
    private boolean defaultAutoSortChestActive = false;
    private ItemEvaluatorType defaultItemEvaluatorType = ItemEvaluatorType.BACK_BEGIN_STRING;
    private SortingPattern defaultSortingPattern = SortingPattern.LEFT_TO_RIGHT_TOP_TO_BOTTOM;

    private static PluginConfig instance = new PluginConfig();

    private PluginConfig() {
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
        // TODO put this at end of the function?
        saveOrOverwriteConfigToFile();

        //TODO check if this is needed
        //loadDefaultAutoSorting();

        loadDefaultEvaluatorType();
        loadDefaultSortingPattern();
        loadCleaningItem();
        loadIsCleaningItemActive();
        loadIsCooldownTimerActive();
        loadIsDurabilityLossActive();
        loadIsOpenInventoryEventModeActive();
        loadIsConsumablesRefillActive();
        loadIsBlockRefillActive();
        loadIsCleanInventoryPermissionActive();
        loadSortingBlacklist();
        loadInventoryBlacklist();
    }

    private void loadDefaultAutoSorting() {
        if (configContainsProperty(Property.DEFAULT_AUTOSORT_CHEST_ACTIVE)) {
            this.defaultAutoSortChestActive = getBooleanProperty(
                Property.DEFAULT_AUTOSORT_CHEST_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.DEFAULT_AUTOSORT_CHEST_ACTIVE,
                defaultAutoSortChestActive);
        }
    }

    private void loadDefaultEvaluatorType() {
        if (configContainsProperty(Property.DEFAULT_ITEM_EVALUATOR)) {
            getStringProperty(Property.DEFAULT_ITEM_EVALUATOR);
            this.defaultItemEvaluatorType = getDefaultEvaluatorTypeFromConfig();
        } else {
            setAndSaveDefaultEvaluatorType(defaultItemEvaluatorType);
        }
    }

    private void loadDefaultSortingPattern() {
        if (configContainsProperty(Property.DEFAULT_SORTING_PATTERN)) {
            this.defaultSortingPattern = getDefaultSortingPatternFromConfig();
        } else {
            setAndSaveStringProperty(Property.DEFAULT_SORTING_PATTERN,
                defaultSortingPattern.name());
        }
    }

    private void loadCleaningItem() {
        if (configContainsProperty(Property.CLEANING_ITEM)
            && getCleaningItemFromConfig() != null) {
            this.currentCleaningItem = getCleaningItemFromConfig();
        } else {
            this.currentCleaningItem = new ItemStack(Material.IRON_HOE);
            this.setAndSaveConfigProperty(Property.CLEANING_ITEM, this.currentCleaningItem);
        }
    }

    private void loadIsCleaningItemActive() {
        if (configContainsProperty(Property.CLEANING_ITEM_ACTIVE)) {
            this.isCleaningItemActive = getBooleanProperty(Property.CLEANING_ITEM_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.CLEANING_ITEM_ACTIVE, true);
        }
    }

    private void loadIsCooldownTimerActive() {
        if (configContainsProperty(Property.COOLDOWN_TIMER_ACTIVE)) {
            this.isCooldownTimerActive = getBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE, true);
        }
    }

    private void loadIsDurabilityLossActive() {
        if (configContainsProperty(Property.DURABILITY_LOSS)) {
            this.isDurabilityLossActive = getBooleanProperty(Property.DURABILITY_LOSS);
        } else {
            setAndSaveBooleanProperty(Property.DURABILITY_LOSS, true);
        }
    }

    private void loadIsOpenInventoryEventModeActive() {
        if (configContainsProperty(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE)) {
            this.isOpenInventoryEventDetectionModeActive = getBooleanProperty(
                Property.OPEN_INVENTORY_EVENT_DETECTION_MODE);
        } else {
            setAndSaveBooleanProperty(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE, false);
        }
    }

    private void loadIsConsumablesRefillActive() {
        if (configContainsProperty(Property.CONSUMABLES_REFILL)) {
            this.isConsumablesRefillActive = getBooleanProperty(Property.CONSUMABLES_REFILL);
        } else {
            setAndSaveBooleanProperty(Property.CONSUMABLES_REFILL, true);
        }
    }

    private void loadIsBlockRefillActive() {
        if (configContainsProperty(Property.BLOCK_REFILL)) {
            this.isBlockRefillActive = getBooleanProperty(Property.BLOCK_REFILL);
        } else {
            setAndSaveBooleanProperty(Property.BLOCK_REFILL, true);
        }
    }

    private void loadIsCleanInventoryPermissionActive() {
        if (configContainsProperty(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE)) {
            this.isCleanInventoryActive = getBooleanProperty(
                Property.CLEAN_INVENTORY_PERMISSION_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE, true);
        }
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

    public void setAndSaveDefaultEvaluatorType(ItemEvaluatorType itemEvaluatorType) {
        yamlConfig
            .set(Property.DEFAULT_ITEM_EVALUATOR.getString(), itemEvaluatorType.name());
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

    public void setAndSaveCooldownTime(int timeInSeconds) {
        yamlConfig.set(Property.TIMER_TIME.getString(), timeInSeconds);
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveCleaningItem(ItemStack cleaningItem) {
        yamlConfig.set(Property.CLEANING_ITEM.getString(), cleaningItem);
        saveOrOverwriteConfigToFile();
    }

    public void setAndSaveMessageList(List<String> messageList) {
        yamlConfig.set(Property.MESSAGES.getString(), messageList);
        saveOrOverwriteConfigToFile();
    }

    private List<String> getMessagesFromConfig() {
        return yamlConfig.getStringList(Property.MESSAGES.getString());
    }

    private ItemStack getCleaningItemFromConfig() {
        return yamlConfig.getItemStack(Property.CLEANING_ITEM.getString());
    }

    public void setSortingBlacklist(final List<Material> blacklist) {
        List<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }
        setAndSaveSortingBlacklist(list);
    }

    private void setAndSaveSortingBlacklist(List<String> list) {
        yamlConfig.set(Property.SORTING_BLACKLIST.getString(), list);
        saveOrOverwriteConfigToFile();
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
        setAndSaveSortingInventoryBlackList(list);
    }

    private void setAndSaveSortingInventoryBlackList(final List<String> list) {
        yamlConfig.set(Property.INVENTORY_BLACKLIST.getString(), list);
        saveOrOverwriteConfigToFile();
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
}
