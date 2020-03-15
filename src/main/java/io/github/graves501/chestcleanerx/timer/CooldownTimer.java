package io.github.graves501.chestcleanerx.timer;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class CooldownTimer {

    private final Player player;
    private int cooldownTimeInSeconds;

    public CooldownTimer(Player player, int cooldownTimeInSeconds) {
        this.player = player;
        this.cooldownTimeInSeconds = cooldownTimeInSeconds;
    }
}
