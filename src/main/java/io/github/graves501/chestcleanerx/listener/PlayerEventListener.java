package io.github.graves501.chestcleanerx.listener;

import io.github.graves501.chestcleanerx.config.PlayerConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
        PlayerConfig.getInstance().loadPlayerData(playerJoinEvent.getPlayer());
    }

    @EventHandler
    private void onPlayerLeave(final PlayerQuitEvent playerQuitEvent) {
        PlayerConfig.getInstance().removePlayerDataFromMemory(playerQuitEvent.getPlayer());
    }

}
