package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.playerdata.PlayerDataManager;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import io.github.graves501.chestcleanerx.utils.enums.Property;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * This class includes all methods to save and read game data (variables for this plugin).
 *
 * @author tom2208
 */
@Data
public class PluginConfiguration {

    private ItemStack currentCleaningItem;
    private int cooldownTimeInSeconds = 5;

    private boolean isCleanInventoryActive = true;
    private boolean isCooldownTimerActive = true;
    private boolean isDurabilityLossActive = true;
    private boolean isCleaningItemActive = true;
    private boolean isOpenInventoryEventDetectionModeActive = false;
    private boolean isBlockRefillActive = true;
    private boolean isConsumablesRefillActive = true;
    private EvaluatorType defaultEvaluatorType = EvaluatorType.BACK_BEGIN_STRING;

    private static final File pluginConfigurationFile = new File(
        Property.PLUGIN_FILE_PATH.getString(),
        Property.PLUGIN_YAML_CONFIG_FILE_NAME.getString());

    private static final FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(
        pluginConfigurationFile);

    private static PluginConfiguration instance = new PluginConfiguration();

    private PluginConfiguration() {
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

        loadDefaultAutoSorting();
        loadDefaultEvaluatorType();
        loadDefaultSortingPattern();
        loadMessages();
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
        if (configurationContainsProperty(Property.DEFAULT_AUTOSORTING)) {
            PlayerDataManager
                .setDefaultAutoSorting(getBooleanProperty(Property.DEFAULT_AUTOSORTING));
        } else {
            setAndSaveBooleanProperty(Property.DEFAULT_AUTOSORTING,
                PlayerDataManager.isDefaultAutoSorting());
        }
    }

    private void loadDefaultEvaluatorType() {
        if (configurationContainsProperty(Property.DEFAULT_EVALUATOR)) {
            defaultEvaluatorType = getDefaultEvaluatorTypeFromConfiguration();
        } else {
            setAndSaveDefaultEvaluatorType(defaultEvaluatorType);
        }
    }

    private void loadDefaultSortingPattern() {
        if (configurationContainsProperty(Property.DEFAULT_SORTING_PATTERN)) {
            SortingPattern.setDefaultSortingPattern(getDefaultSortingPatternFromConfiguration());
        } else {
            setAndSaveStringProperty(Property.DEFAULT_SORTING_PATTERN,
                SortingPattern.defaultSortingPattern.name());
        }
    }

    private void loadMessages() {
        if (configurationContainsProperty(Property.MESSAGES)) {
            Messages.setMessageList(getMessagesFromConfiguration());
        } else {
            Messages.setMessageList(null);
        }
    }

    private void loadCleaningItem() {
        if (configurationContainsProperty(Property.CLEANING_ITEM)) {
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

    /**
     * This method will save using the system default encoding, or possibly using UTF8.
     */
    public void saveOrOverwriteConfigurationToFile() {
        try {
            yamlConfiguration.save(pluginConfigurationFile);
        } catch (IOException e) {
            //TODO use logger
            e.printStackTrace();
        }
    }

    public void setAndSaveDefaultEvaluatorType(EvaluatorType evaluatorType) {
        yamlConfiguration
            .set(Property.DEFAULT_EVALUATOR.getString(), evaluatorType.name());
        saveOrOverwriteConfigurationToFile();
    }

    public EvaluatorType getDefaultEvaluatorTypeFromConfiguration() {
        return EvaluatorType.getEvaluatorTypeByName(yamlConfiguration.getString(
            Property.DEFAULT_EVALUATOR.getString()));
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

    // Helper functions

    public boolean setFallbackValueIfPropertyIsNotSet(final Property property,
        final Object fallbackValue) {
        if (!configurationContainsProperty(property)) {
            yamlConfiguration.set(property.getString(), fallbackValue);
            return true;
        }

        return false;
    }

    public boolean setFallbackValueIfPropertyIsNotSet(final Property property,
        final boolean fallbackValue) {
        if (!configurationContainsProperty(property)) {
            yamlConfiguration.set(property.getString(), fallbackValue);
            return true;
        }

        return false;
    }

    public boolean configurationContainsProperty(final Property property) {
        return yamlConfiguration.contains(property.getString());
    }

    public void setAndSaveBooleanProperty(final Property property, boolean value) {
        yamlConfiguration.set(property.getString(), value);
        saveOrOverwriteConfigurationToFile();
    }

    public boolean getBooleanProperty(final Property property) {
        return yamlConfiguration.getBoolean(property.getString());
    }

    public void setAndSaveStringProperty(final Property property, String value) {
        yamlConfiguration.set(property.getString(), value);
        saveOrOverwriteConfigurationToFile();
    }

    public String getStringProperty(final Property property) {
        return yamlConfiguration.getString(property.getString());
    }

    public void setConfigurationProperty(final Property property, final Object propertyValue) {
        yamlConfiguration.set(property.getString(), propertyValue);
    }

    public void setAndSaveConfigurationProperty(final Property property,
        final Object propertyValue) {
        yamlConfiguration.set(property.getString(), propertyValue);
        saveOrOverwriteConfigurationToFile();
    }

    public Object getConfigurationProperty(final Property property) {
        return yamlConfiguration.get(property.getString());
    }
}
