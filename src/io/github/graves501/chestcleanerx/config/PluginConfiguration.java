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

    private ItemStack currentCleaningItem = new ItemStack(Material.IRON_HOE);
    private int cooldownTimeInSeconds = 5;

    private boolean isCleanInventoryActive = true;
    private boolean isCooldownTimerActive = true;
    private boolean isDurabilityLossActive = true;
    private boolean isCleaningItemActive = true;
    private boolean isEventModeActive = false;
    private boolean isBlockRefillActive = true;
    private boolean isConsumablesRefillActive = true;

    private final static File pluginConfigurationFile = new File(
        Property.PLUGIN_FILE_PATH.getString(),
        Property.PLUGIN_YAML_CONFIG_FILE_NAME.getString());

    private final static FileConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(
        pluginConfigurationFile);


    private static PluginConfiguration instance = new PluginConfiguration();

    private EvaluatorType defaultEvaluatorType = EvaluatorType.BACK_BEGIN_STRING;

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
        saveConfigurationToFile();

        if (configurationContainsProperty(Property.DEFAULT_AUTOSORT)) {
            PlayerDataManager.defaultAutoSort = getDefaultAutoSort();
        } else {
            setDefaultAutoSort(PlayerDataManager.defaultAutoSort);
        }

        if (configurationContainsProperty(Property.DEFAULT_EVALUATOR)) {
            defaultEvaluatorType = getDefaultEvaluatorType();
        } else {
            setDefaultEvaluatorType(defaultEvaluatorType);
        }

        if (configurationContainsProperty(Property.DEFAULT_SORTING_PATTERN)) {
            SortingPattern.DEFAULT = getDefaultSortingPattern();
        } else {
            setDefaultSortingPattern(SortingPattern.DEFAULT);
        }

        if (configurationContainsProperty(Property.MESSAGES)) {
            Messages.setMessages(getMessages());
        } else {
            Messages.setMessages(null);
        }

        if (getCleaningItem() == null) {
            this.currentCleaningItem = new ItemStack(Material.IRON_HOE);
            this.setConfigurationProperty(Property.CLEANING_ITEM, this.currentCleaningItem);
        } else {
            currentCleaningItem = getCleaningItem();
        }

        if (configurationContainsProperty(Property.CLEANING_ITEM_ACTIVE)) {
            isCleaningItemActive = getCleaningItemActive();
        } else {
            setCleaningItemActive(true);
        }

        if (configurationContainsProperty(Property.DURABILITY_LOSS)) {
            isDurabilityLossActive = getDurabilityLossBoolean();
        } else {
            setDurabilityLossBoolean(true);
        }

        if (configurationContainsProperty(Property.OPEN_INVENTORY_EVENT_MODE)) {
            isEventModeActive = getOpenInventoryEventModeEnabled();
        } else {
            enableOpenInventoryEventMode(false);
        }

        if (configurationContainsProperty(Property.CONSUMABLES_REFILL)) {
            isConsumablesRefillActive = getConsumablesRefill();
        } else {
            setConsumablesRefill(true);
        }

        if (configurationContainsProperty(Property.BLOCK_REFILL)) {
            isBlockRefillActive = isBlockRefillActive();
        } else {
            enableBlockRefill(true);
        }

        if (configurationContainsProperty(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE)) {
            isCleanInventoryActive = getBooleanProperty(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE);
        } else {
            setCleanInventoryPermission(true);
        }

        if (configurationContainsProperty(Property.SORTING_BLACKLIST)) {
            InventorySorter.blacklist = getSortingBlackList();
        }

        if (configurationContainsProperty(Property.INVENTORY_BLACKLIST)) {
            BlacklistCommand.inventoryBlacklist = getInventoryBlackList();
        }

    }

    /**
     * Saves this {@code FileConfiguration} to the the io.github.graves501.chestcleaner folder. If
     * the file does not exist, it will be created. If already exists, it will be overwritten.
     *
     * This method will save using the system default encoding, or possibly using UTF8.
     */
    public void saveConfigurationToFile() {
        try {
            yamlConfiguration.save(pluginConfigurationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* DEFAULT AUTOSORT */

    public void setDefaultAutoSort(boolean defaultAutoSortEnabled) {
        yamlConfiguration.set(Property.DEFAULT_AUTOSORT.getString(),
            defaultAutoSortEnabled);
        saveConfigurationToFile();
    }

    public boolean getDefaultAutoSort() {
        return yamlConfiguration
            .getBoolean(Property.DEFAULT_AUTOSORT.getString());
    }

    /* DEFAULT EVALUATOR */

    public void setDefaultEvaluatorType(EvaluatorType evaluatorType) {
        yamlConfiguration
            .set(Property.DEFAULT_EVALUATOR.getString(), evaluatorType.name());
        saveConfigurationToFile();
    }

    public EvaluatorType getDefaultEvaluatorType() {
        return EvaluatorType.getEvaluatorTypeByName(yamlConfiguration.getString(
            Property.DEFAULT_EVALUATOR.getString()));
    }

    /* DEFAULT PATTERN*/

    public void setDefaultSortingPattern(SortingPattern sortingPattern) {
        yamlConfiguration.set(Property.DEFAULT_SORTING_PATTERN.getString(), sortingPattern.name());
        saveConfigurationToFile();
    }

    public SortingPattern getDefaultSortingPattern() {
        return SortingPattern
            .getSortingPatternByName(yamlConfiguration.getString("defaultsortingpattern"));
    }

    /* CLEANINVETORYPERMISSION */

    public void setCleanInventoryPermission(boolean b) {
        yamlConfiguration.set(Property.CLEAN_INVENTORY_PERMISSION_ACTIVE.getString(), b);
        saveConfigurationToFile();
    }

//    public boolean getCleanInvPermission() {
//        return yamlConfiguration.getBoolean("cleanInventorypermissionactive");
//    }

    public void setCooldownTimerActive(boolean enableCooldownTimer) {
        yamlConfiguration.set(Property.TIMER_ACTIVE.getString(),
            enableCooldownTimer);
        saveConfigurationToFile();
    }

    public boolean isCooldownTimerActive() {
        return yamlConfiguration.getBoolean(Property.TIMER_ACTIVE.getString());
    }

    public void setCooldownTime(int timeInSeconds) {
        yamlConfiguration.set(Property.TIMER_TIME.getString(), timeInSeconds);
        saveConfigurationToFile();
    }

    public int getCooldownTime() {
        return yamlConfiguration.getInt(Property.TIMER_TIME.getString());
    }

    /* STRINGS */

    public void setMessages(List<String> messages) {
        yamlConfiguration.set(Property.MESSAGES.getString(), messages);
        saveConfigurationToFile();
    }

    public List<String> getMessages() {
        return yamlConfiguration.getStringList(Property.MESSAGES.getString());
    }

    /* ITEM */

    public ItemStack getCleaningItem() {
        return yamlConfiguration.getItemStack(Property.CLEANING_ITEM.getString());
    }

    public void setCleaningItemActive(boolean isCleaningItemActive) {
        yamlConfiguration.set(Property.CLEANING_ITEM_ACTIVE.getString(), isCleaningItemActive);
        saveConfigurationToFile();
    }

    public boolean getCleaningItemActive() {
        return yamlConfiguration.getBoolean(Property.CLEANING_ITEM_ACTIVE.getString());
    }

    /* DURABILITYLOSS */

    public void setDurabilityLossBoolean(boolean enableDurabilityLoss) {
        yamlConfiguration.set(Property.DURABILITY_LOSS.getString(), enableDurabilityLoss);
        saveConfigurationToFile();
    }

    public boolean getDurabilityLossBoolean() {
        return yamlConfiguration.getBoolean(Property.DURABILITY_LOSS.getString());
    }

    /* MODE */

    public void enableOpenInventoryEventMode(boolean enableOpenInventoryEventMode) {
        yamlConfiguration
            .set(Property.OPEN_INVENTORY_EVENT_MODE.getString(), enableOpenInventoryEventMode);
        saveConfigurationToFile();
    }

    public boolean getOpenInventoryEventModeEnabled() {
        return yamlConfiguration.getBoolean(Property.OPEN_INVENTORY_EVENT_MODE.getString());
    }

    /* CONSUMABLES */

    public void setConsumablesRefill(boolean enableConsumablesRefill) {
        yamlConfiguration.set(Property.CONSUMABLES_REFILL.getString(), enableConsumablesRefill);
        saveConfigurationToFile();
    }

    public boolean getConsumablesRefill() {
        return yamlConfiguration.getBoolean(Property.CONSUMABLES_REFILL.getString());
    }

    /* BLOCKREFILL */

    public void enableBlockRefill(boolean enableBlockRefill) {
        yamlConfiguration
            .set(Property.BLOCK_REFILL.getString(), enableBlockRefill);
        saveConfigurationToFile();
    }

    public boolean isBlockRefillActive() {
        return yamlConfiguration.getBoolean(Property.BLOCK_REFILL.getString());
    }

    /* BLACKLISTS */

    /**
     * SortingBlacklist
     */
    private void setStringSortingBlackList(List<String> list) {
        yamlConfiguration.set(Property.SORTING_BLACKLIST.getString(), list);
        saveConfigurationToFile();
    }

    public void setSortingBlackList(final List<Material> blacklist) {

        List<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }
        setStringSortingBlackList(list);
    }

    public List<Material> getSortingBlackList() {

        List<String> list = yamlConfiguration.getStringList(Property.SORTING_BLACKLIST.getString());
        List<Material> materials = new ArrayList<>();

        for (String name : list) {
            materials.add(Material.getMaterial(name));
        }

        return materials;
    }

    /**
     * SortingBlacklist
     */
    private void setStringInventoryBlackList(final List<String> list) {
        yamlConfiguration.set(Property.INVENTORY_BLACKLIST.getString(), list);
        saveConfigurationToFile();
    }

    public void setInventoryBlackList(final List<Material> blacklist) {

        ArrayList<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }
        setStringInventoryBlackList(list);
    }

    public List<Material> getInventoryBlackList() {

        List<String> inventoryBlacklist = yamlConfiguration
            .getStringList(Property.INVENTORY_BLACKLIST.getString());
        ArrayList<Material> materials = new ArrayList<>();

        for (String blacklistItemName : inventoryBlacklist) {
            materials.add(Material.getMaterial(blacklistItemName));
        }

        return materials;
    }

    public boolean setFallbackValueIfPropertyIsNotSet(final Property property,
        final Object fallbackValue) {
        if (!configurationContainsProperty(property)) {
            yamlConfiguration.set(property.getString(), fallbackValue);
            return true;
        }

        return false;
    }

    private boolean configurationContainsProperty(final Property property) {
        return yamlConfiguration.contains(property.getString());
    }

    private void setBooleanProperty(final Property property, boolean value) {
        yamlConfiguration.set(property.getString(), value);
    }

    private boolean getBooleanProperty(final Property property) {
        return yamlConfiguration.getBoolean(property.getString());
    }

    private String getStringProperty(final Property property) {
        return yamlConfiguration.getString(property.getString());
    }

    private void setConfigurationProperty(final Property property, final Object propertyValue) {
        yamlConfiguration.set(property.getString(), propertyValue);
    }

    private Object getConfigurationProperty(final Property property) {
        return yamlConfiguration.get(property.getString());
    }
}
