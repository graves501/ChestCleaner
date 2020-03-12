package io.github.graves501.chestcleanerx.main;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.commands.CleanInventoryCommand;
import io.github.graves501.chestcleanerx.commands.CleaningItemCommand;
import io.github.graves501.chestcleanerx.commands.CooldownTimerCommand;
import io.github.graves501.chestcleanerx.commands.SortingConfigCommand;
import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.listeners.DataLoadingListener;
import io.github.graves501.chestcleanerx.listeners.RefillListener;
import io.github.graves501.chestcleanerx.listeners.SortingListener;
import io.github.graves501.chestcleanerx.timer.CooldownTimerThread;
import io.github.graves501.chestcleanerx.utils.enums.PlayerCommand;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class PluginMain extends JavaPlugin {

    private static PluginMain instance = new PluginMain();

    private PluginMain() {
    }

    public static PluginMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        PluginConfiguration.getInstance().loadConfiguration();
        enablePluginCommands();
        registerEventListener();
        startCooldownTimerThread();
        checkForUpdates();
    }

    private void enablePluginCommands() {
        Objects.requireNonNull(getCommand(PlayerCommand.CLEAN_INVENTORY.getString()))
            .setExecutor(new CleanInventoryCommand());

        Objects.requireNonNull(getCommand(PlayerCommand.TIMER.getString()))
            .setExecutor(new CooldownTimerCommand());

        Objects.requireNonNull(getCommand(PlayerCommand.CLEANING_ITEM.getString()))
            .setExecutor(new CleaningItemCommand());

        Objects.requireNonNull(getCommand(PlayerCommand.BLACKLIST.getString()))
            .setExecutor(new BlacklistCommand());

        Objects.requireNonNull(getCommand(PlayerCommand.SORTING_CONFIG.getString()))
            .setExecutor(new SortingConfigCommand());
    }

    private void registerEventListener() {
        Bukkit.getPluginManager().registerEvents(new SortingListener(), this);
        Bukkit.getPluginManager().registerEvents(new RefillListener(), this);
        Bukkit.getPluginManager().registerEvents(new DataLoadingListener(), this);
    }

    private void startCooldownTimerThread() {
        CooldownTimerThread cooldownTimerThread = new CooldownTimerThread();
        cooldownTimerThread.start();
    }

    private void checkForUpdates() {
        new UpdateCheckerThread(this).checkForUpdate();
    }

// TODO delete this method

//    /**
//     * Loads all variables out of the config, if the config does not exist it will generate one with
//     * the default values for the variables.
//     */
//    private void loadConfiguration() {
//
//        PluginConfiguration pluginConfiguration = PluginConfiguration.getInstance();
//
//        pluginConfiguration.saveConfigurationToFile();
//
//        if (pluginConfiguration.containsDefaultAutoSort()) { PlayerDataManager.defaultAutoSort = pluginConfiguration.getDefaultAutoSort();
//        } else {
//            pluginConfiguration.setDefaultAutoSort(PlayerDataManager.defaultAutoSort);
//        }
//
//        if (pluginConfiguration.containsDefaultEvaluator()) {
//            EvaluatorType.DEFAULT = pluginConfiguration.getDefaultEvaluator();
//        } else {
//            pluginConfiguration.setDefaultEvaluator(EvaluatorType.DEFAULT);
//        }
//
//        if (pluginConfiguration.containsDefaultSortingPattern()) {
//            SortingPattern.DEFAULT = pluginConfiguration.getDefaultSortingPattern();
//        } else {
//            pluginConfiguration.setDefaultSortingPattern(SortingPattern.DEFAULT);
//        }
//
//        if (pluginConfiguration.containsStrings()) {
//            Messages.setMessages(pluginConfiguration.getStrings());
//        } else {
//            Messages.setMessages(null);
//        }
//
//        if (pluginConfiguration.getCleaningItem() == null) {
//            currentCleaningItem = new ItemStack(Material.IRON_HOE);
//            pluginConfiguration.setCleaningItem(currentCleaningItem);
//        } else {
//            currentCleaningItem = pluginConfiguration.getCleaningItem();
//        }
//
//        if (pluginConfiguration.containsItemBoolean()) {
//            isCleaningItemActive = pluginConfiguration.getItemBoolean();
//        } else {
//            pluginConfiguration.setCleaningItemActive(true);
//        }
//
//        if (pluginConfiguration.containsDurabilityLossBoolean()) {
//            isDurabilityLossEnabled = pluginConfiguration.getDurabilityLossBoolean();
//        } else {
//            pluginConfiguration.setDurabilityLossBoolean(true);
//        }
//
//        if (pluginConfiguration.containsOpenInventoryEventModeEnabled()) {
//            isEventModeEnabled = pluginConfiguration.getOpenInventoryEventModeEnabled();
//        } else {
//            pluginConfiguration.enableOpenInventoryEventMode(false);
//        }
//
//        if (pluginConfiguration.containsConsumablesRefill()) {
//            consumablesRefillEnabled = pluginConfiguration.getConsumablesRefill();
//        } else {
//            pluginConfiguration.setConsumablesRefill(true);
//        }
//
//        if (pluginConfiguration.containsBlockRefillEnabled()) {
//            blockRefillEnabled = pluginConfiguration.isBlockRefillEnabled();
//        } else {
//            pluginConfiguration.enableBlockRefill(true);
//        }
//
//        if (pluginConfiguration.containsCleanInvPermission()) {
//            isCleanInventoryEnabled = pluginConfiguration.getCleanInvPermission();
//        } else {
//            pluginConfiguration.setCleanInvPermission(true);
//        }
//
//        if (pluginConfiguration.containsSortingBlackList()) {
//            InventorySorter.blacklist = pluginConfiguration.getSortingBlackList();
//        }
//
//        if (pluginConfiguration.containsSortingBlackList()) {
//            BlacklistCommand.inventoryBlacklist = pluginConfiguration.getInventoryBlackList();
//        }
//
//    }

}
