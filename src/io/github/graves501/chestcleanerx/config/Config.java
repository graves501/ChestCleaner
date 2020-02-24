package io.github.graves501.chestcleanerx.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;

/**
 * This class includes all methods to save and read game data (variables for this plugin).
 *
 * @author tom2208
 */
public class Config {

    public static final String INVENTORY_BLACKLIST = "inventoryBlacklist";
    public static final String DEFAULT_AUTOSORT = "defaultautosort";
    public static final String DEFAULT_EVALUATOR = "defaultevaluator";
    public static final String MESSAGES = "messages";

    public static File ConfigFile = new File("plugins/ChestCleanerX", "config.yml");
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* DEFAULT AUTOSORT */

    public static void setDefaultAutoSort(boolean defaultAutoSortEnabled) {
        Config.set(DEFAULT_AUTOSORT, defaultAutoSortEnabled);
        save();
    }

    public static boolean getDefaultAutoSort() {
        return Config.getBoolean(DEFAULT_AUTOSORT);
    }

    public static boolean containsDefaultAutoSort() {
        return Config.contains(DEFAULT_AUTOSORT);
    }

    /* DEFAULT EVALUATOR */

    public static void setDefaultEvaluator(EvaluatorType evaluatorType) {
        Config.set(DEFAULT_EVALUATOR, evaluatorType.name());
        save();
    }

    public static EvaluatorType getDefaultEvaluator() {
        return EvaluatorType.getEvaluatorTypByName(Config.getString(DEFAULT_EVALUATOR));
    }

    public static boolean containsDefaultEvaluator() {
        return Config.contains(DEFAULT_EVALUATOR);
    }

    /* DEFAULT PATTERN*/

    public static void setDefaultSortingPattern(SortingPattern p) {
        Config.set("defaultsortingpattern", p.name());
        save();
    }

    public static SortingPattern getDefaultSortingPattern() {
        return SortingPattern.getSortingPatternByName(Config.getString("defaultsortingpattern"));
    }

    public static boolean containsDefaultSortingPattern() {
        return Config.contains("defaultsortingpattern");
    }

    /* CLEANINVETORYPERMISSION */

    public static void setCleanInvPermission(boolean b) {
        Config.set("cleanInventorypermissionactive", b);
        save();
    }

    public static boolean getCleanInvPermission() {
        return Config.getBoolean("cleanInventorypermissionactive");
    }

    public static boolean containsCleanInvPermission() {
        return Config.contains("cleanInventorypermissionactive");
    }

    /* TIMER */

    public static void setTimerPermission(boolean b) {
        Config.set("timer.active", b);
        save();
    }

    public static boolean getTimerPermission() {
        return Config.getBoolean("timer.active");
    }

    public static boolean containsTimerPermission() {
        return Config.contains("timer.active");
    }

    /* TIMER.TIME */

    public static void setTime(int t) {
        Config.set("timer.time", t);
        save();
    }

    public static int getTime() {
        return Config.getInt("timer.time");
    }

    public static boolean containsTime() {
        return Config.contains("timer.time");
    }

    /* STRINGS */

    public static void setMessages(List<String> messages) {
        Config.set(MESSAGES, messages);
        save();
    }

    public static List<String> getStrings() {
        return Config.getStringList(MESSAGES);
    }

    public static boolean containsStrings() {
        return Config.contains("Strings");
    }

    /* ITEM */

    public static void setCleaningItem(ItemStack is) {
        Config.set("cleaningItem", is);
        save();
    }

    public static ItemStack getCleaningItem() {
        return Config.getItemStack("cleaningItem");
    }

    /* ITEM */

    public static void setItemBoolean(boolean b) {
        Config.set("active", b);
        save();
    }

    public static boolean getItemBoolean() {
        return Config.getBoolean("active");
    }

    public static boolean containsItemBoolean() {
			if (Config.contains("active")) {
				return true;
			}
        return false;
    }

    /* DURABILITYLOSS */

    public static void setDurabilityLossBoolean(boolean b) {
        Config.set("durability", b);
        save();
    }

    public static boolean getDurabilityLossBoolean() {
        return Config.getBoolean("durability");
    }

    public static boolean containsDurabilityLossBoolean() {
			if (Config.contains("durability")) {
				return true;
			}
        return false;
    }

    /* MODE */

    public static void setMode(boolean b) {
        Config.set("openinventoryeventmode", b);
        save();
    }

    public static boolean getMode() {
        return Config.getBoolean("openinventoryeventmode");
    }

    public static boolean containsMode() {
			if (Config.contains("openinventoryeventmode")) {
				return true;
			}
        return false;
    }

    /* CONSUMABLES */

    public static void setConsumablesRefill(boolean b) {
        Config.set("consumablesrefill", b);
        save();
    }

    public static boolean getConsumablesRefill() {
        return Config.getBoolean("consumablesrefill");
    }

    public static boolean containsConsumablesRefill() {
			if (Config.contains("consumablesrefill")) {
				return true;
			}
        return false;
    }

    /* BLOCKREFILL */

    public static void setBlockRefill(boolean b) {
        Config.set("blockrefill", b);
        save();
    }

    public static boolean getBlockRefill() {
        return Config.getBoolean("blockrefill");
    }

    public static boolean containsBlockRefill() {
			if (Config.contains("blockrefill")) {
				return true;
			}
        return false;
    }

    /* BLACKLISTS */

    /**
     * SortingBlacklist
     */
    private static void setStringSortingBlackList(ArrayList<String> list) {
        Config.set("blacklist", list);
        save();
    }

    public static boolean containsSortingBlackList() {
			if (Config.contains("blacklist")) {
				return true;
			}
        return false;
    }

    public static void setSortingBlackList(ArrayList<Material> blacklist) {

        ArrayList<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }
        setStringSortingBlackList(list);
    }

    public static ArrayList<Material> getSortingBlackList() {

        List<String> list = Config.getStringList("blacklist");
        ArrayList<Material> materials = new ArrayList<>();

        for (String name : list) {
            materials.add(Material.getMaterial(name));
        }

        return materials;
    }

    /**
     * SortingBlacklist
     */
    private static void setStringInventoryBlackList(ArrayList<String> list) {
        Config.set(INVENTORY_BLACKLIST, list);
        save();
    }

    public static boolean containsInventoryBlackList() {
        return Config.contains(INVENTORY_BLACKLIST);
    }

    public static void setInventoryBlackList(ArrayList<Material> blacklist) {

        ArrayList<String> list = new ArrayList<>();

        for (Material material : blacklist) {
            list.add(material.name());
        }
        setStringInventoryBlackList(list);
    }

    public static ArrayList<Material> getInventoryBlackList() {

        List<String> list = Config.getStringList(INVENTORY_BLACKLIST);
        ArrayList<Material> materials = new ArrayList<>();

        for (String name : list) {
            materials.add(Material.getMaterial(name));
        }

        return materials;
    }
}
