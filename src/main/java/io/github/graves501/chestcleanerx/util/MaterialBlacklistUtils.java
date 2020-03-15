package io.github.graves501.chestcleanerx.util;

import io.github.graves501.chestcleanerx.util.message.InGameMessage;
import io.github.graves501.chestcleanerx.util.message.InGameMessageHandler;
import io.github.graves501.chestcleanerx.util.message.InGameMessageType;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MaterialBlacklistUtils {

    private MaterialBlacklistUtils() {
    }

    public static void sendListPageToPlayer(List<Material> list, Player player, int page,
        int maxPageLines, int pages) {

        InGameMessageHandler.sendMessageToPlayer(player, InGameMessageType.SUCCESS,
            InGameMessage.BLACKLIST_PAGE_INDEX, page + " / " + pages);

        for (int i = (page - 1) * maxPageLines; i < page * maxPageLines; i++) {
            if (list.size() == i) {
                break;
            } else {
                InGameMessageHandler
                    .sendMessageToPlayer(player,
                        InGameMessageType.UNHEADED_INFORMATION,
                        (i + 1) + ". " + list.get(i).name());
            }
        }

        if (pages > page) {
            InGameMessageHandler.sendMessageToPlayer(player,
                InGameMessageType.SUCCESS, InGameMessage.NEXT_ENTRIES,
                String.valueOf(page + 1));
        }

    }

}
