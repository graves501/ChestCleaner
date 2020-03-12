package io.github.graves501.chestcleanerx.timer;

import io.github.graves501.chestcleanerx.config.PluginConfiguration;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import java.util.ArrayList;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class CooldownTimer {

    private Player player;
    private int timeInSeconds;
    private static ArrayList<CooldownTimer> cooldownTimersList = new ArrayList<>();

    public CooldownTimer(Player player, int timeInSeconds) {
        this.player = player;
        this.timeInSeconds = timeInSeconds;
    }

    public Player getPlayer() {
        return player;
    }


    public static void update() {
        if (PluginConfiguration.getInstance().isCooldownTimerActive()) {
            for (CooldownTimer cooldownTimer : cooldownTimersList) {
                cooldownTimer.setTimeInSeconds(cooldownTimer.getTimeInSeconds() - 1);
            }

            ArrayList<Integer> remove = new ArrayList<>();

            for (int i = 0; i < cooldownTimersList.size(); i++) {
                if (cooldownTimersList.get(i).getTimeInSeconds() <= 0) {
                    remove.add(i);
                }
            }
            for (int i : remove) {
                cooldownTimersList.remove(i);
            }
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
                return cooldownTimer.getTimeInSeconds();
            }
        }
        return 0;
    }

    public static void addPlayerToCooldownTimers(final Player player) {
        cooldownTimersList
            .add(new CooldownTimer(player,
                PluginConfiguration.getInstance().getCooldownTimeInSeconds()));
    }

    /**
     * Checks if the player is has sorting cooldown and if sorting is available of it. If sorting
     * isn't on cooldown it sets a cooldown for the player and returns true. If the sorting is on
     * cooldown it sends a message with the remaining time and returns false.
     *
     * @param player The player who you want to check if it can sort.
     * @return Returns true if the player is allowed to sort and false if it is not.
     */
    public static boolean checkPlayerAndPlayerPermissions(final Player player) {
        if (PluginConfiguration.getInstance().isCooldownTimerActive() && !player
            .hasPermission("chestcleaner.timer.noeffect")) {
            if (isPlayerOnCooldownTimersList(player)) {
                MessageSystem.sendMessageToPlayer(MessageType.ERROR, Messages
                    .getMessage(MessageID.SORTING_ON_COOLDOWN, "%time",
                        String.valueOf(getCooldownTimeForPlayer(player))), player);
                return false;
            }
            addPlayerToCooldownTimers(player);
            return true;
        }
        return true;
    }

}
