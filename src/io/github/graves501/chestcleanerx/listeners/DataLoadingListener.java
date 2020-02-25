package io.github.graves501.chestcleanerx.listeners;

import io.github.graves501.chestcleanerx.playerdata.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataLoadingListener implements Listener {

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
        PlayerDataManager.loadPlayerData(playerJoinEvent.getPlayer());
    }

    @EventHandler
    private void onPlayerLeave(final PlayerQuitEvent playerQuitEvent) {
        PlayerDataManager.removePlayerDataFromMemory(playerQuitEvent.getPlayer());
    }

}
