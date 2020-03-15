package io.github.graves501.chestcleanerx.utils.logging;

import java.util.logging.Logger;
import org.bukkit.entity.Player;

public class PluginLoggerUtil {

    private PluginLoggerUtil() {
    }

    public static void logPlayerInfo(final Logger logger, final Player player,
        final String message) {
        final String playerInfoLogMessage = String
            .format("Player: %s Message: %s", player.getDisplayName(), message);
        logger.info(playerInfoLogMessage);
    }
}
