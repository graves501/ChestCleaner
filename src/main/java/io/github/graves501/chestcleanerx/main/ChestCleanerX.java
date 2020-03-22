package io.github.graves501.chestcleanerx.main;

import io.github.graves501.chestcleanerx.command.BlacklistCommand;
import io.github.graves501.chestcleanerx.command.CleanInventoryCommand;
import io.github.graves501.chestcleanerx.command.CleaningItemCommand;
import io.github.graves501.chestcleanerx.command.CooldownCommand;
import io.github.graves501.chestcleanerx.command.SortingConfigCommand;
import io.github.graves501.chestcleanerx.config.PluginConfig;
import io.github.graves501.chestcleanerx.listener.PlayerEventListener;
import io.github.graves501.chestcleanerx.listener.RefillListener;
import io.github.graves501.chestcleanerx.listener.SortingListener;
import io.github.graves501.chestcleanerx.timer.CooldownThread;
import io.github.graves501.chestcleanerx.util.constant.PlayerCommand;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestCleanerX extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginConfig.getInstance().loadConfig();
        enablePluginCommands();
        registerEventListener();
        startCooldownThread();

        //TODO not needed for now
        // checkForUpdates();
    }

    private void enablePluginCommands() {
        Objects.requireNonNull(getCommand(PlayerCommand.CLEAN_INVENTORY.getString()))
            .setExecutor(new CleanInventoryCommand());

        Objects.requireNonNull(getCommand(PlayerCommand.TIMER.getString()))
            .setExecutor(new CooldownCommand());

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

    private void startCooldownThread() {
        CooldownThread cooldownThread = new CooldownThread();
        cooldownThread.start();
    }

    private void checkForUpdates() {
        new UpdateCheckerThread(this).checkForUpdate();
    }
}
