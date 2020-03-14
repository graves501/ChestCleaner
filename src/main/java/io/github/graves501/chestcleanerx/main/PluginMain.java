package io.github.graves501.chestcleanerx.main;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.commands.CleanInventoryCommand;
import io.github.graves501.chestcleanerx.commands.CleaningItemCommand;
import io.github.graves501.chestcleanerx.commands.CooldownTimerCommand;
import io.github.graves501.chestcleanerx.commands.SortingConfigCommand;
import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.listeners.PlayerEventListener;
import io.github.graves501.chestcleanerx.listeners.RefillListener;
import io.github.graves501.chestcleanerx.listeners.SortingListener;
import io.github.graves501.chestcleanerx.timer.CooldownTimerThread;
import io.github.graves501.chestcleanerx.utils.enums.PlayerCommand;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {

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
        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(), this);
    }

    private void startCooldownTimerThread() {
        CooldownTimerThread cooldownTimerThread = new CooldownTimerThread();
        cooldownTimerThread.start();
    }

    private void checkForUpdates() {
        new UpdateCheckerThread(this).checkForUpdate();
    }
}
