package io.github.graves501.chestcleanerx.timer;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class Cooldown {

    private final Player player;
    private int cooldownInSeconds;

    public Cooldown(Player player, int cooldownInSeconds) {
        this.player = player;
        this.cooldownInSeconds = cooldownInSeconds;
    }
}
