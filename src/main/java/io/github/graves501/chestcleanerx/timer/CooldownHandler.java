package io.github.graves501.chestcleanerx.timer;

import io.github.graves501.chestcleanerx.config.PluginConfig;
import io.github.graves501.chestcleanerx.main.ChestCleanerX;
import io.github.graves501.chestcleanerx.util.constant.PluginPermission;
import io.github.graves501.chestcleanerx.util.logging.PluginLoggerUtil;
import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class CooldownHandler {

    private CooldownHandler() {
    }

    private static final Logger logger = JavaPlugin.getPlugin(ChestCleanerX.class).getLogger();

    private static ArrayList<Cooldown> cooldownOfPlayers = new ArrayList<>();

    public static void update() {
        if (PluginConfig.getInstance().isCooldownActive()) {
            for (Cooldown cooldown : cooldownOfPlayers) {
                countdownOneSecond(cooldown);
            }

            cooldownOfPlayers.removeIf(
                cooldownOfPlayer -> cooldownOfPlayer.getCooldownInSeconds() <= 0);
        }
    }

    public static boolean isPlayerOnCooldownList(final Player player) {
        //TODO use streams
        for (Cooldown cooldown : cooldownOfPlayers) {
            if (cooldown.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public static int getCooldownForPlayer(final Player player) {
        //TODO use streams
        for (Cooldown cooldown : cooldownOfPlayers) {
            if (cooldown.getPlayer().equals(player)) {
                return cooldown.getCooldownInSeconds();
            }
        }
        return 0;
    }

    private static void addPlayerToCooldownList(final Player player) {
        cooldownOfPlayers.add(new Cooldown(player,
            PluginConfig.getInstance().getCooldownInSeconds()));
    }

    public static boolean isSortingOnCooldownForPlayer(final Player player) {

        if (isCoolDownTimerActive()) {
            PluginLogger.getGlobal().info("isCoolDownTimerActive: " + isCoolDownTimerActive());

            if (playerHasCooldownNoEffectPermission(player)) {
                return false;
            }

            if (isPlayerOnCooldownList(player)) {
                logErrorAndSendSortingCooldownMessageToPlayer(player);
                return true;
            }
            addPlayerToCooldownList(player);
        }

        return false;
    }

    private static void logErrorAndSendSortingCooldownMessageToPlayer(final Player player) {
        PluginLoggerUtil
            .logPlayerInfo(player, "Sorting not allowed due to cooldown.");

        InGameMessageHandler.sendMessageToPlayer(player,
            InGameMessageType.ERROR,
            InGameMessage.SORTING_ON_COOLDOWN,
            String.valueOf(getCooldownForPlayer(player)));
    }

    private static boolean isCoolDownTimerActive() {
        return PluginConfig.getInstance().isCooldownActive();
    }

    private static boolean playerHasCooldownNoEffectPermission(final Player player) {
        return player.hasPermission(PluginPermission.TIMER_NO_EFFECT.getString());
    }

    private static void countdownOneSecond(final Cooldown cooldown) {
        cooldown.setCooldownInSeconds(cooldown.getCooldownInSeconds() - 1);
    }

}
