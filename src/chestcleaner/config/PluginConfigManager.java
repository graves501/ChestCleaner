package chestcleaner.config;

import chestcleaner.config.serializable.Category;
import chestcleaner.config.serializable.ListCategory;
import chestcleaner.config.serializable.MasterCategory;
import chestcleaner.config.serializable.WordCategory;
import chestcleaner.sorting.SortingPattern;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PluginConfigManager {

	private static List<Material> blacklistStacking = null;
	private static List<Material> blacklistInventory = null;

	private PluginConfigManager() {}
	
	public static boolean isBreakableRefillActive() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.DEFAULT_BREAKABLE_ITEMS_REFILL.getPath());
	}

	public static void setBreakableRefillActive(boolean breakableRefill) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.DEFAULT_BREAKABLE_ITEMS_REFILL, breakableRefill);
	}
	
	public static void setDefaultSortingSoundBoolean(boolean bool) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.DEFAULT_SORTING_SOUND_BOOLEAN, bool);
	}
	
	public static boolean getDefaultSortingSoundBoolean() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.DEFAULT_SORTING_SOUND_BOOLEAN.getPath());
	}
	
	public static void setDefaultChatNotificationBoolean(boolean bool) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CHAT_NOTIFICATION_BOOLEAN, bool);
	}
	
	public static boolean getDefaultChatNotificationBoolean() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.CHAT_NOTIFICATION_BOOLEAN.getPath());
	}
	
	public static boolean isDurabilityLossActive() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.CLEANING_ITEM_DURABILITY.getPath());
	}

	public static void setDurabilityLossActive(boolean durabilityLossActive) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CLEANING_ITEM_DURABILITY, durabilityLossActive);
	}

	public static boolean isCleaningItemActive() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.CLEANING_ITEM_ACTIVE.getPath());
	}

	public static void setCleaningItemActive(boolean cleaningItemActive) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CLEANING_ITEM_ACTIVE, cleaningItemActive);
	}

	public static ItemStack getCleaningItem() {
		return PluginConfig.getConfig().getItemStack(PluginConfig.ConfigPath.CLEANING_ITEM.getPath());
	}

	public static void setCleaningItem(ItemStack item) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CLEANING_ITEM, item);
	}

	public static boolean isOpenEvent() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.CLEANING_ITEM_OPEN_EVENT.getPath());
	}

	public static void setOpenEvent(boolean openEvent) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CLEANING_ITEM_OPEN_EVENT, openEvent);
	}

	public static boolean isBlockRefillActive() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.REFILL_BLOCKS.getPath());
	}

	public static void setBlockRefillActive(boolean blockRefillActive) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.REFILL_BLOCKS, blockRefillActive);
	}

	public static boolean isConsumablesRefillActive() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.REFILL_CONSUMABLES.getPath());
	}

	public static void setConsumablesRefillActive(boolean consumablesRefillActive) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.REFILL_CONSUMABLES, consumablesRefillActive);
	}

	public static boolean isUpdateCheckerActive() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.UPDATE_CHECKER_ACTIVE.getPath());
	}

	public static void setUpdateCheckerActive(boolean updateCheckerActive) {
	 	PluginConfig.setIntoConfig(PluginConfig.ConfigPath.UPDATE_CHECKER_ACTIVE, updateCheckerActive);
	}

	public static boolean isCooldownActive() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.COOLDOWN_ACTIVE.getPath());
	}

	public static void setCooldownActive(boolean active) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.COOLDOWN_ACTIVE, active);
	}

	public static int getCooldown() {
		return PluginConfig.getConfig().getInt(PluginConfig.ConfigPath.COOLDOWN_TIME.getPath());
	}

	public static void setCooldown(int time) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.COOLDOWN_TIME, time);
	}

	public static List<String> getCategoryOrder() {
		return PluginConfig.getConfig().getStringList(PluginConfig.ConfigPath.DEFAULT_CATEGORIES.getPath());
	}

	public static void setCategoryOrder(List<String> categorizationOrder) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.DEFAULT_CATEGORIES, categorizationOrder);
	}

	public static Category getCategoryByName(String name) {
		for (WordCategory category : getWordCategories())
			if (category.getName().equalsIgnoreCase(name))
				return category;
		for (ListCategory category : getListCategories())
			if (category.getName().equalsIgnoreCase(name))
				return category;
		for (MasterCategory category : getMasterCategories())
			if (category.getName().equalsIgnoreCase(name))
				return category;
		return null;
	}

	public static List<Category> getAllCategories() {
		List<Category> list = new ArrayList<>();
		list.addAll(getWordCategories());
		list.addAll(getListCategories());
		list.addAll(getMasterCategories());
		return list;
	}

	public static List<WordCategory> getWordCategories() {
		return getCastList(PluginConfig.getConfig().getList(
				PluginConfig.ConfigPath.CATEGORIES_WORDS.getPath(), new ArrayList<WordCategory>()));
	}

	public static List<ListCategory> getListCategories() {
		return getCastList(PluginConfig.getConfig().getList(
				PluginConfig.ConfigPath.CATEGORIES_LISTS.getPath(), new ArrayList<ListCategory>()));
	}

	public static List<MasterCategory> getMasterCategories() {
		return getCastList(PluginConfig.getConfig().getList(
				PluginConfig.ConfigPath.CATEGORIES_MASTER.getPath(), new ArrayList<MasterCategory>()));
	}

	public static void addWordCategory(WordCategory category) {
		List<WordCategory> categories = addOrUpdateCategory(category, getWordCategories());
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CATEGORIES_WORDS, categories);
	}

	public static void addListCategory(ListCategory category) {
		List<ListCategory> categories = addOrUpdateCategory(category, getListCategories());
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CATEGORIES_LISTS, categories);
	}


	public static void addMasterCategory(MasterCategory category) {
		List<MasterCategory> categories = addOrUpdateCategory(category, getMasterCategories());
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.CATEGORIES_MASTER, categories);
	}

	private static <T extends Category> List<T> addOrUpdateCategory(T category, List<T> categories) {
		T existingCategory = categories.stream()
				.filter(cat -> cat.getName().equalsIgnoreCase(category.getName()))
				.findFirst().orElse(null);
		if (existingCategory != null) {
			existingCategory.setValue(category.getValue());
		} else {
			categories.add(category);
		}
		return categories;
	}

	public static void setDefaultPattern(SortingPattern pattern) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.DEFAULT_PATTERN, pattern);
	}

	public static SortingPattern getDefaultPattern() {
		return SortingPattern.getSortingPatternByName(PluginConfig.getConfig()
				.getString(PluginConfig.ConfigPath.DEFAULT_PATTERN.getPath()));
	}

	public static boolean getDefaultAutoSortBoolean() {
		return PluginConfig.getConfig().getBoolean(PluginConfig.ConfigPath.DEFAULT_AUTOSORT.getPath());
	}

	public static void setDefaultAutoSort(boolean defaultAutoSort) {
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.DEFAULT_AUTOSORT, defaultAutoSort);
	}
	
	public static List<Material> getBlacklistInventory() {
		if (blacklistInventory == null) {
			blacklistInventory = getMaterialList(PluginConfig.getConfig(), PluginConfig.ConfigPath.BLACKLIST_INVENTORY);
		}
		return blacklistInventory;
	}

	public static void setBlacklistInventory(List<Material> blacklistInventory) {
	    PluginConfigManager.blacklistInventory = blacklistInventory;
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.BLACKLIST_INVENTORY, getStringList(blacklistInventory));
	}

	public static List<Material> getBlacklistStacking() {
	    if (blacklistStacking == null) {
			blacklistStacking = getMaterialList(PluginConfig.getConfig(), PluginConfig.ConfigPath.BLACKLIST_STACKING);
		}
		return blacklistStacking;
	}

	public static void setBlacklistStacking(List<Material> blacklistStacking) {
	    PluginConfigManager.blacklistStacking = blacklistStacking;
		PluginConfig.setIntoConfig(PluginConfig.ConfigPath.BLACKLIST_STACKING, getStringList(blacklistStacking));
	}

	private static ArrayList<Material> getMaterialList(FileConfiguration config, PluginConfig.ConfigPath path) {
		List<String> list = config.getStringList(path.getPath());
		ArrayList<Material> materials = new ArrayList<>();

		for (String name : list) {
			materials.add(Material.getMaterial(name.toUpperCase()));
		}
		return materials;
	}

	private static List<String> getStringList(List<Material> materialList) {
		List<String> list = new ArrayList<>();

		for (Material material : materialList) {
			list.add(material.name().toLowerCase());
		}
		return list;
	}

	private static <T> List<T> getCastList(List<?> input) {
		if (input == null) {
			return new ArrayList<>();
		}
		return input.stream().map(o -> (T) o).collect(Collectors.toList());
	}
}
