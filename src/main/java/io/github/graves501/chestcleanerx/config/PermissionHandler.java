package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.util.constant.PluginPermission;
import io.github.graves501.chestcleanerx.util.logging.PluginLoggerUtil;
import org.bukkit.entity.Player;

public class PermissionHandler {

    private PermissionHandler(){}

    public static boolean playerHasPermissionToSortOwnInventory(final Player player) {
        final boolean playerHasPermissionToSortOwnInventory = player
            .hasPermission(PluginPermission.SORT_OWN_INVENTORY.getString());

        PluginLoggerUtil.logPlayerInfo(player,
            "playerHasPermissionToSortOwnInventory: " + playerHasPermissionToSortOwnInventory);
        return playerHasPermissionToSortOwnInventory;
    }

    public static boolean playerHasPermissionToUseCleaningItem(final Player player) {
        final boolean playerHasPermissionToUseCleaningItem = player
            .hasPermission(PluginPermission.USE_CLEANING_ITEM.getString());

        PluginLoggerUtil.logPlayerInfo(player,
            "playerHasPermissionToUseCleaningItem: " + playerHasPermissionToUseCleaningItem);
        return playerHasPermissionToUseCleaningItem;
    }

    public static boolean playerHasPermissionToAutoRefillBlocks(final Player player) {
        final boolean playerHasAutoRefillBlocksPermission = player
            .hasPermission(PluginPermission.AUTO_REFILL_BLOCKS.getString());

        PluginLoggerUtil.logPlayerInfo(player,
            "playerHasAutoRefillBlocksPermission: " + playerHasAutoRefillBlocksPermission);
        return playerHasAutoRefillBlocksPermission;
    }

    public static boolean playerHasPermissionToAutoRefillConsumables(final Player player) {
        final boolean playerHasAutoRefillConsumablesPermission = player
            .hasPermission(PluginPermission.AUTO_REFILL_CONSUMABLES.getString());

        PluginLoggerUtil.logPlayerInfo(player,
            "playerHasAutoRefillConsumablesPermission: "
                + playerHasAutoRefillConsumablesPermission);
        return playerHasAutoRefillConsumablesPermission;
    }

}
