package io.github.graves501.chestcleanerx.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;

public class MaterialListUtils {

	private MaterialListUtils(){}

	public static void sendListPageToPlayer(List<Material> list, Player player, int page, int maxPageLines, int pages){

		MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
				Messages.getMessage(MessageID.BLACKLIST_TITLE, "%page", page + " / " + pages), player);

		for (int i = (page - 1) * maxPageLines; i < page * maxPageLines; i++) {
			if (list.size() == i) {
				break;
			} else {
				MessageSystem.sendMessageToPlayer(MessageType.UNHEADED_INFORMATION,
						(i + 1) + ". " + list.get(i).name(), player);
			}
		}

		if (pages > page) {
			MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, Messages
					.getMessage(MessageID.NEXT_ENTRIES, "%nextpage", String.valueOf(page + 1)), player);
		}

	}

}
