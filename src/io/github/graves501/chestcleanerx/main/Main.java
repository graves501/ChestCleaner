package io.github.graves501.chestcleanerx.main;

import io.github.graves501.chestcleanerx.utils.stringconstants.PluginCommands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.commands.CleanInventoryCommand;
import io.github.graves501.chestcleanerx.commands.CleaningItemCommand;
import io.github.graves501.chestcleanerx.commands.SortingConfigCommand;
import io.github.graves501.chestcleanerx.commands.TimerCommand;
import io.github.graves501.chestcleanerx.config.Config;
import io.github.graves501.chestcleanerx.listeners.SortingListener;
import io.github.graves501.chestcleanerx.playerdata.PlayerDataManager;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.EvaluatorType;
import io.github.graves501.chestcleanerx.listeners.DataLoadingListener;
import io.github.graves501.chestcleanerx.listeners.RefillListener;
import io.github.graves501.chestcleanerx.timer.Counter;
import io.github.graves501.chestcleanerx.utils.messages.Messages;

public class Main extends JavaPlugin {

	public static boolean cleanInvPermission = true;
	public static boolean timer = true;
	public static int time = 5;
	public static ItemStack cleaningItem = new ItemStack(Material.IRON_HOE);
	public static boolean durability = true;
	public static boolean itemBoolean = true;
	public static boolean eventmode = false;
	public static boolean blockRefill = true;
	public static boolean consumablesRefill = true;

	public static Main main;

	private Counter counter = new Counter();

	@Override
	public void onEnable() {
		main = this;
		loadConfig();

		getCommand(PluginCommands.CLEAN_INVENTORY).setExecutor(new CleanInventoryCommand());
		getCommand(PluginCommands.TIMER).setExecutor(new TimerCommand());
		getCommand(PluginCommands.CLEANING_ITEM).setExecutor(new CleaningItemCommand());
		getCommand(PluginCommands.BLACKLIST).setExecutor(new BlacklistCommand());
		getCommand(PluginCommands.SORTING_CONFIG).setExecutor(new SortingConfigCommand());

		Bukkit.getPluginManager().registerEvents(new SortingListener(), this);
		Bukkit.getPluginManager().registerEvents(new RefillListener(), this);
		Bukkit.getPluginManager().registerEvents(new DataLoadingListener(), this);
		counter.start();

		new UpdateChecker(this).checkForUpdate();
	}

	/**
	 * Loads all variables out of the config, if the config does not exist it
	 * will generate one with the default values for the variables.
	 */
	private void loadConfig() {

		Config.save();

		if(Config.containsDefaultAutoSort()){
			PlayerDataManager.defaultAutoSort = Config.getDefaultAutoSort();
		}else{
			Config.setDefaultAutoSort(PlayerDataManager.defaultAutoSort);
		}

		if(Config.containsDefaultEvaluator()){
			EvaluatorType.DEFAULT = Config.getDefaultEvaluator();
		}else{
			Config.setDefaultEvaluator(EvaluatorType.DEFAULT);
		}

		if(Config.containsDefaultSortingPattern()){
			SortingPattern.DEFAULT = Config.getDefaultSortingPattern();
		}else{
			Config.setDefaultSortingPattern(SortingPattern.DEFAULT);
		}

		if (Config.containsStrings()) {
			Messages.setMessages(Config.getStrings());
		} else {
			Messages.setMessages(null);
		}

		if (Config.getCleaningItem() == null) {
			cleaningItem = new ItemStack(Material.IRON_HOE);
			Config.setCleaningItem(cleaningItem);
		} else {
			cleaningItem = Config.getCleaningItem();
		}

		if (Config.containsItemBoolean()) {
			itemBoolean = Config.getItemBoolean();
		} else {
			Config.setItemBoolean(true);
		}

		if (Config.containsDurabilityLossBoolean()) {
			durability = Config.getDurabilityLossBoolean();
		} else {
			Config.setDurabilityLossBoolean(true);
		}

		if (Config.containsMode()) {
			eventmode = Config.getMode();
		} else {
			Config.setMode(false);
		}

		if (Config.containsConsumablesRefill()) {
			consumablesRefill = Config.getConsumablesRefill();
		} else {
			Config.setConsumablesRefill(true);
		}

		if (Config.containsBlockRefill()) {
			blockRefill = Config.getBlockRefill();
		} else {
			Config.setBlockRefill(true);
			;
		}

		if (Config.containsCleanInvPermission()) {
			cleanInvPermission = Config.getCleanInvPermission();
		} else {
			Config.setCleanInvPermission(true);
		}

		if(Config.containsSortingBlackList()){
			InventorySorter.blacklist = Config.getSortingBlackList();
		}

		if (Config.containsSortingBlackList()) {
			BlacklistCommand.inventoryBlacklist = Config.getInventoryBlackList();
		}

	}

}
