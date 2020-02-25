package io.github.graves501.chestcleanerx.timer;

import io.github.graves501.chestcleanerx.main.Main;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;
import java.util.ArrayList;
import org.bukkit.entity.Player;

public class Timer {

    private Player player;
    private int timeInSeconds;

    public Timer(Player player, int timeInSeconds) {
        this.player = player;
        this.timeInSeconds = timeInSeconds;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    static ArrayList<Timer> times = new ArrayList<>();

    public static void update() {
        if (Main.timer) {
            for (Timer timer : times) {
                timer.setTimeInSeconds(timer.getTimeInSeconds() - 1);
            }

            ArrayList<Integer> remove = new ArrayList<>();

            for (int i = 0; i < times.size(); i++) {
                if (times.get(i).getTimeInSeconds() <= 0) {
                    remove.add(i);
                }
            }
            for (int i : remove) {
                times.remove(i);
            }
        }
    }

    public static boolean isPlayerOnList(final Player player) {
        for (Timer t : times) {
            if (t.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public static int getPlayerTime(final Player player) {
        for (Timer t : times) {
            if (player.equals(player)) {
                return t.getTimeInSeconds();
            }
        }
        return 0;
    }

    public static void setPlayerOnList(final Player player) {
        times.add(new Timer(player, Main.time));
    }

    /**
     * Checks if the player is has sorting cooldown and if sorting is available of it. If sorting
     * isn't on cooldown it sets a cooldown for the player and returns true. If the sorting is on
     * cooldown it sends a message with the remaining time and returns false.
     *
     * @param player The player who you want to check if it can sort.
     * @return Returns true if the player is allowed to sort and false if it is not.
     */
    public static boolean playerCheck(final Player player) {
        if (Main.timer && !player.hasPermission("chestcleaner.timer.noeffect")) {
            if (isPlayerOnList(player)) {
                MessageSystem.sendMessageToPlayer(MessageType.ERROR, Messages
                    .getMessage(MessageID.SORTING_ON_COOLDOWN, "%time",
                        String.valueOf(getPlayerTime(player))), player);
                return false;
            }
            setPlayerOnList(player);
            return true;
        }
        return true;
    }

}
