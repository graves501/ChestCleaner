package io.github.graves501.chestcleanerx.commands;

import io.github.graves501.chestcleanerx.utils.stringconstants.PlayerMessages;
import io.github.graves501.chestcleanerx.utils.stringconstants.PluginPermissions;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.StringUtil;

import io.github.graves501.chestcleanerx.config.Config;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.utils.MaterialListUtils;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.StringTable;

public class BlacklistCommand implements CommandExecutor, TabCompleter {

	private static class BlackListCommandString {
		static final String SORTING = "sorting";
		static final String INVENTORIES = "inventories";

		static final String ADD_MATERIAL = "addMaterial";
		static final String REMOVE_MATERIAL = "removeMaterial";
		static final String LIST = "list";
		static final String CLEAR = "clear";
	}

	private final List<String> commandList = new ArrayList<>();
	private final List<String> lists = new ArrayList<>();
	private final int LIST_LENGTH = 8;
	public static ArrayList<Material> inventoryBlacklist = new ArrayList<>();

	public BlacklistCommand() {
		lists.add(BlackListCommandString.SORTING);
		lists.add(BlackListCommandString.INVENTORIES);

		commandList.add(BlackListCommandString.ADD_MATERIAL);
		commandList.add(BlackListCommandString.REMOVE_MATERIAL);
		commandList.add(BlackListCommandString.LIST);
		commandList.add(BlackListCommandString.CLEAR);
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {

		if (!(commandSender instanceof Player)) {
			MessageSystem.sendConsoleMessage(MessageType.ERROR, MessageID.NOT_A_PLAYER);
			return true;
		}

		Player player = (Player) commandSender;

		if (player.hasPermission(PluginPermissions.BLACKLIST_PERMISSION)) {

			if (args.length <= 1) {
				sendSyntaxError(player);
				return true;
			}

			if (args.length >= 2 && args.length <= 3) {

				/* initialize list */
				ArrayList<Material> list;
				int listNumber = -1;
				if (args[0].equalsIgnoreCase(lists.get(0))) {
					list = InventorySorter.blacklist;
					listNumber = 0;
				} else if (args[0].equalsIgnoreCase(lists.get(1))) {
					list = inventoryBlacklist;
					listNumber = 1;
				} else {
					sendSyntaxError(player);
					return true;
				}

				/** subCommands */

				/*----- addMaterial -----*/
				if (args[1].equalsIgnoreCase(commandList.get(0))) {

					Material material;

					// addMaterial with name
					if (args.length == 3) {

						material = Material.getMaterial(args[2]);

						if (material == null) {
							MessageSystem.sendMessageToPlayer(MessageType.ERROR,
									StringTable.getMessage(MessageID.NO_MATERIAL_FOUND, "%material", args[2]), player);
							return true;
						}

						// addMaterial with item in hand
					} else {
						material = getMaterialFromPlayerHand(player);

						if (material.equals(Material.AIR)) {
							MessageSystem.sendMessageToPlayer(MessageType.ERROR, MessageID.HOLD_AN_ITEM, player);
							return true;
						}

					}

					if (list.contains(material)) {
						MessageSystem.sendMessageToPlayer(MessageType.ERROR,
								StringTable.getMessage(MessageID.IS_ALREADY_ON_BLACKLIST, "%material", material.name()),
							player);
						return true;
					}

					list.add(material);
					safeList(listNumber);

					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
							StringTable.getMessage(MessageID.SET_TO_BLACKLIST, "%material", material.name()),
						player);
					return true;

					/*----- removeMaterial -----*/
				} else if (args[1].equalsIgnoreCase(commandList.get(1))) {

					Material material;

					// removeMaterial with name
					if (args.length == 3) {

						material = Material.getMaterial(args[2]);

						if (material == null) {

							try {

								int index = Integer.valueOf(args[2]);

								if (index - 1 >= 0 && index - 1 < list.size()) {

									material = list.get(index - 1);

								} else {
									MessageSystem.sendMessageToPlayer(MessageType.ERROR, StringTable.getMessage(
											MessageID.INDEX_OUT_OF_BOUNDS, "%biggestindex", String.valueOf(index)),
										player);
									return true;
								}

							} catch (NumberFormatException ex) {

								MessageSystem.sendMessageToPlayer(MessageType.ERROR,
										StringTable.getMessage(MessageID.NO_MATERIAL_FOUND, "%material", args[2]),
									player);
								return true;

							}

						}

						// removeMaterial with item in hand
					} else {
						material = getMaterialFromPlayerHand(player);

						if (material.equals(Material.AIR)) {
							MessageSystem.sendMessageToPlayer(MessageType.ERROR, MessageID.HOLD_AN_ITEM, player);
							return true;
						}

					}

					if (!list.contains(material)) {
						MessageSystem.sendMessageToPlayer(MessageType.ERROR, StringTable
								.getMessage(MessageID.BLACKLIST_DOESNT_CONTAINS, "%material", material.name()),
							player);
						return true;
					}

					list.remove(material);
					safeList(listNumber);

					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
							StringTable.getMessage(MessageID.REMOVED_FORM_BLACKLIST, "%material", material.name()),
						player);
					return true;

					/*----- list -----*/
				} else if (args[1].equalsIgnoreCase(commandList.get(2))) {

					if (list.isEmpty()) {
						MessageSystem.sendMessageToPlayer(MessageType.ERROR, MessageID.BLACKLIST_IS_EMPTY,
							player);
						return true;
					}

					int page = 1;
					int pages = list.size() / LIST_LENGTH + 1;

					if (args.length == 3) {

						try {

							page = Integer.valueOf(args[2]);

						} catch (NumberFormatException ex) {

							MessageSystem.sendMessageToPlayer(MessageType.ERROR,
									StringTable.getMessage(MessageID.INVALID_INPUT_FOR_INTEGER, "%index", args[1]),
								player);
							return true;

						}

						if (!(page > 0 && page <= pages)) {
							MessageSystem.sendMessageToPlayer(MessageType.ERROR,
									StringTable.getMessage(MessageID.INVALID_PAGE_NUMBER, "%range", "1 - " + pages),
								player);
							return true;
						}

					}

					MaterialListUtils.sendListPageToPlayer(list, player, page, LIST_LENGTH, pages);
					return true;

					/*----- clear -----*/
				} else if (args[1].equalsIgnoreCase(commandList.get(3))) {

					list.clear();
					safeList(listNumber);
					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.BLACKLIST_CLEARED,
						player);
					return true;

				} else {
					sendSyntaxError(player);
					return true;
				}

			} else {
				sendSyntaxError(player);
				return true;
			}

		} else {
			MessageSystem
				.sendMessageToPlayer(MessageType.MISSING_PERMISSION, PluginPermissions.BLACKLIST_PERMISSION,
					player);
			return true;
		}

	}

	private Material getMaterialFromPlayerHand(Player player) {
		final PlayerInventory playerInventory = player.getInventory();

		final Material materialInMainHand = playerInventory.getItemInMainHand().getType();

		if (isHandEquippedWithMaterial(materialInMainHand)) {
		    return materialInMainHand;
		}

		final Material materialInOffHand = playerInventory.getItemInOffHand().getType();

		if (isHandEquippedWithMaterial(materialInOffHand)) {
			return materialInOffHand;
		}

		return null;
	}

	private boolean isHandEquippedWithMaterial(Material materialInHand){
		return !materialInHand.equals(Material.AIR);
	}

	/**
	 * Send the correct syntax the Player {@code p}.
	 * @param player the player how receives the message.
	 */
	private void sendSyntaxError(Player player) {
		MessageSystem
			.sendMessageToPlayer(MessageType.SYNTAX_ERROR, PlayerMessages.BLACKLIST_SYNTAX_ERROR, player);
	}

	/**
	 * Saves the list to the config.
	 * @param list 0 meaning sortingBlacklist, 1 inventoryBlacklist.
	 */
	private void safeList(int list) {

		if (list == 0) {
			Config.setSortingBlackList(InventorySorter.blacklist);
		} else if (list == 1) {
			Config.setInventoryBlackList(inventoryBlacklist);
		}

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		final List<String> completions = new ArrayList<>();

		if (args.length <= 1) {

			StringUtil.copyPartialMatches(args[0], lists, completions);

		} else if (args.length == 2) {

			StringUtil.copyPartialMatches(args[1], commandList, completions);

		}

		return completions;
	}

}
