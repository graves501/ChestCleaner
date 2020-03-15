package io.github.graves501.chestcleanerx.listener;

import io.github.graves501.chestcleanerx.configuration.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
        PlayerConfiguration.getInstance().loadPlayerData(playerJoinEvent.getPlayer());
    }

    @EventHandler
    private void onPlayerLeave(final PlayerQuitEvent playerQuitEvent) {
        PlayerConfiguration.getInstance().removePlayerDataFromMemory(playerQuitEvent.getPlayer());
    }

}
