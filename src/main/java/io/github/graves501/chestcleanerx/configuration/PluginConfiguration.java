package io.github.graves501.chestcleanerx.configuration;

import io.github.graves501.chestcleanerx.command.BlacklistCommand;
import io.github.graves501.chestcleanerx.main.PluginMain;
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
public class PluginConfiguration extends ConfigurationManager {

    private final Logger logger = JavaPlugin.getPlugin(PluginMain.class).getLogger();

    // Default settings for the plugin
    private ItemStack currentCleaningItem;
    private int cooldownTimeInSeconds = 5;

    private boolean isCleanInventoryActive = true;
    private boolean isCooldownTimerActive = true;
    private boolean isDurabilityLossActive = true;
    private boolean isCleaningItemActive = true;
    private boolean isOpenInventoryEventDetectionModeActive = false;
    private boolean isBlockRefillActive = true;
    private boolean isConsumablesRefillActive = true;
    private boolean defaultAutoSortChestActive = false;
    private ItemEvaluatorType defaultItemEvaluatorType = ItemEvaluatorType.BACK_BEGIN_STRING;
    private SortingPattern defaultSortingPattern = SortingPattern.LEFT_TO_RIGHT_TOP_TO_BOTTOM;

    private static PluginConfiguration instance = new PluginConfiguration();

    private PluginConfiguration() {
        this.configurationFile = new File(
            Property.PLUGIN_FILE_PATH.getString(),
            Property.PLUGIN_YAML_CONFIG_FILE_NAME.getString());

        this.yamlConfiguration = YamlConfiguration.loadConfiguration(
            configurationFile);
    }


    public static PluginConfiguration getInstance() {
        return instance;
    }

    /**
     * Loads all variables out of the config, if the config does not exist it will generate one with
     * the default values for the variables.
     */
    public void loadConfiguration() {
        // TODO put this at end of the function?
        saveOrOverwriteConfigurationToFile();

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
        if (configurationContainsProperty(Property.DEFAULT_AUTOSORT_CHEST_ACTIVE)) {
            this.defaultAutoSortChestActive = getBooleanProperty(
                Property.DEFAULT_AUTOSORT_CHEST_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.DEFAULT_AUTOSORT_CHEST_ACTIVE,
                defaultAutoSortChestActive);
        }
    }

    private void loadDefaultEvaluatorType() {
        if (configurationContainsProperty(Property.DEFAULT_ITEM_EVALUATOR)) {
            getStringProperty(Property.DEFAULT_ITEM_EVALUATOR);
            this.defaultItemEvaluatorType = getDefaultEvaluatorTypeFromConfiguration();
        } else {
            setAndSaveDefaultEvaluatorType(defaultItemEvaluatorType);
        }
    }

    private void loadDefaultSortingPattern() {
        if (configurationContainsProperty(Property.DEFAULT_SORTING_PATTERN)) {
            this.defaultSortingPattern = getDefaultSortingPatternFromConfiguration();
        } else {
            setAndSaveStringProperty(Property.DEFAULT_SORTING_PATTERN,
                defaultSortingPattern.name());
        }
    }

    private void loadCleaningItem() {
        if (configurationContainsProperty(Property.CLEANING_ITEM)
            && getCleaningItemFromConfiguration() != null) {
            this.currentCleaningItem = getCleaningItemFromConfiguration();
        } else {
            this.currentCleaningItem = new ItemStack(Material.IRON_HOE);
            this.setAndSaveConfigurationProperty(Property.CLEANING_ITEM, this.currentCleaningItem);
        }
    }

    private void loadIsCleaningItemActive() {
        if (configurationContainsProperty(Property.CLEANING_ITEM_ACTIVE)) {
            this.isCleaningItemActive = getBooleanProperty(Property.CLEANING_ITEM_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.CLEANING_ITEM_ACTIVE, true);
        }
    }

    private void loadIsCooldownTimerActive() {
        if (configurationContainsProperty(Property.COOLDOWN_TIMER_ACTIVE)) {
            this.isCooldownTimerActive = getBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.COOLDOWN_TIMER_ACTIVE, true);
        }
    }

    private void loadIsDurabilityLossActive() {
        if (configurationContainsProperty(Property.DURABILITY_LOSS)) {
            this.isDurabilityLossActive = getBooleanProperty(Property.DURABILITY_LOSS);
        } else {
            setAndSaveBooleanProperty(Property.DURABILITY_LOSS, true);
        }
    }

    private void loadIsOpenInventoryEventModeActive() {
        if (configurationContainsProperty(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE)) {
            this.isOpenInventoryEventDetectionModeActive = getBooleanProperty(
                Property.OPEN_INVENTORY_EVENT_DETECTION_MODE);
        } else {
            setAndSaveBooleanProperty(Property.OPEN_INVENTORY_EVENT_DETECTION_MODE, false);
        }
    }

    private void loadIsConsumablesRefillActive() {
        if (configurationContainsProperty(Property.CONSUMABLES_REFILL)) {
            this.isConsumablesRefillActive = getBooleanProperty(Property.CONSUMABLES_REFILL);
        } else {
            setAndSaveBooleanProperty(Property.CONSUMABLES_REFILL, true);
        }
    }

    private void loadIsBlockRefillActive() {
        if (configurationContainsProperty(Property.BLOCK_REFILL)) {
            this.isBlockRefillActive = getBooleanProperty(Property.BLOCK_REFILL);
        } else {
            setAndSaveBooleanProperty(Property.BLOCK_REFILL, true);
        }
    }

    private void loadIsCleanInventoryPermissionActive() {
        if (configurationContainsProperty(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE)) {
            this.isCleanInventoryActive = getBooleanProperty(
                Property.CLEAN_INVENTORY_PERMISSION_ACTIVE);
        } else {
            setAndSaveBooleanProperty(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE, true);
        }
    }

    private void loadSortingBlacklist() {
        if (configurationContainsProperty(Property.SORTING_BLACKLIST)) {
            //TODO make list non-static
            InventorySorter.blacklist = getSortingBlackListFromConfiguration();
        }
    }

    private void loadInventoryBlacklist() {
        if (configurationContainsProperty(Property.INVENTORY_BLACKLIST)) {
            //TODO make list non-static
            BlacklistCommand.inventoryBlacklist = getInventoryBlackListFromConfiguration();
        }
    }

    public void setAndSaveDefaultEvaluatorType(ItemEvaluatorType itemEvaluatorType) {
        yamlConfiguration
            .set(Property.DEFAULT_ITEM_EVALUATOR.getString(), itemEvaluatorType.name());
        saveOrOverwriteConfigurationToFile();
    }

    public ItemEvaluatorType getDefaultEvaluatorTypeFromConfiguration() {
        return ItemEvaluatorType
            .getEvaluatorTypeByName(getStringProperty(Property.DEFAULT_ITEM_EVALUATOR));
    }

    public SortingPattern getDefaultSortingPatternFromConfiguration() {
        return SortingPattern
            .getSortingPatternByName(getStringProperty(Property.DEFAULT_SORTING_PATTERN));
    }

    public void setAndSaveCooldownTime(int timeInSeconds) {
        yamlConfiguration.set(Property.TIMER_TIME.getString(), timeInSeconds);
        saveOrOverwriteConfigurationToFile();
    }

    public void setAndSaveCleaningItem(ItemStack cleaningItem) {
        yamlConfiguration.set(Property.CLEANING_ITEM.getString(), cleaningItem);
        saveOrOverwriteConfigurationToFile();
    }

    public void setAndSaveMessageList(List<String> messageList) {
        yamlConfiguration.set(Property.MESSAGES.getString(), messageList);
        saveOrOverwriteConfigurationToFile();
    }

    private List<String> getMessagesFromConfiguration() {
        return yamlConfiguration.getStringList(Property.MESSAGES.getString());
    }

    private ItemStack getCleaningItemFromConfiguration() {
        return yamlConfiguration.getItemStack(Property.CLEANING_ITEM.getString());
    }

    public void setSortingBlacklist(final List<Material> blacklist) {
        List<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }
        setAndSaveSortingBlacklist(list);
    }

    private void setAndSaveSortingBlacklist(List<String> list) {
        yamlConfiguration.set(Property.SORTING_BLACKLIST.getString(), list);
        saveOrOverwriteConfigurationToFile();
    }

    public List<Material> getSortingBlackListFromConfiguration() {
        List<String> list = yamlConfiguration.getStringList(Property.SORTING_BLACKLIST.getString());
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
        yamlConfiguration.set(Property.INVENTORY_BLACKLIST.getString(), list);
        saveOrOverwriteConfigurationToFile();
    }

    public List<Material> getInventoryBlackListFromConfiguration() {

        List<String> inventoryBlacklist = yamlConfiguration
            .getStringList(Property.INVENTORY_BLACKLIST.getString());
        ArrayList<Material> materials = new ArrayList<>();

        for (String blacklistItemName : inventoryBlacklist) {
            materials.add(Material.getMaterial(blacklistItemName));
        }

        return materials;
    }
}
