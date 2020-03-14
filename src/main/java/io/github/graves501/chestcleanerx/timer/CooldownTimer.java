package io.github.graves501.chestcleanerx.timer;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.utils.enums.PluginPermission;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessage;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessageHandler;
import io.github.graves501.chestcleanerx.utils.messages.InGameMessageType;
import java.util.ArrayList;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class CooldownTimer {

    private Player player;
    private int cooldownTimeInSeconds;
    private static ArrayList<CooldownTimer> cooldownTimersList = new ArrayList<>();

    public CooldownTimer(Player player, int cooldownTimeInSeconds) {
        this.player = player;
        this.cooldownTimeInSeconds = cooldownTimeInSeconds;
    }

    public Player getPlayer() {
        return player;
    }


    public static void update() {
        if (PluginConfiguration.getInstance().isCooldownTimerActive()) {
            for (CooldownTimer cooldownTimer : cooldownTimersList) {
                countDownOneSecond(cooldownTimer);
            }

            cooldownTimersList
                .removeIf(cooldownTimer -> cooldownTimer.getCooldownTimeInSeconds() <= 0);
        }
    }

    public static boolean isPlayerOnCooldownTimersList(final Player player) {
        for (CooldownTimer cooldownTimer : cooldownTimersList) {
            if (cooldownTimer.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public static int getCooldownTimeForPlayer(final Player player) {
        for (CooldownTimer cooldownTimer : cooldownTimersList) {
            if (cooldownTimer.getPlayer().equals(player)) {
                return cooldownTimer.getCooldownTimeInSeconds();
            }
        }
        return 0;
    }

    public static void addPlayerToCooldownTimers(final Player player) {
        cooldownTimersList.add(new CooldownTimer(player,
            PluginConfiguration.getInstance().getCooldownTimeInSeconds()));
    }

    public static boolean isPlayerAllowedToUseSort(final Player player) {
        if (isCoolDownTimerActive() && !playerHasTimerNoEffectPermission(player)) {

            if (isPlayerOnCooldownTimersList(player)) {
                InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.ERROR,
                    InGameMessage.SORTING_ON_COOLDOWN,
                    String.valueOf(getCooldownTimeForPlayer(player)));
                return false;
            }

            addPlayerToCooldownTimers(player);
        }

        return true;
    }

    private static boolean isCoolDownTimerActive() {
        return PluginConfiguration.getInstance().isCooldownTimerActive();
    }

    private static boolean playerHasTimerNoEffectPermission(final Player player) {
        return player.hasPermission(PluginPermission.TIMER_NO_EFFECT.getString());
    }

    private static void countDownOneSecond(CooldownTimer cooldownTimer) {
        cooldownTimer.setCooldownTimeInSeconds(cooldownTimer.getCooldownTimeInSeconds() - 1);
    }

}
