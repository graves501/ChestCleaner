package chestcleaner.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import chestcleaner.config.Config;
import chestcleaner.main.Main;
import chestcleaner.utils.messages.MessageID;
import chestcleaner.utils.messages.MessageSystem;
import chestcleaner.utils.messages.MessageType;
import chestcleaner.utils.messages.StringTable;

/**
 * @author tom2208
 */
public class CleaningItemCommand implements CommandExecutor, TabCompleter {

	private final List<String> cleaningItemCommands = new ArrayList<>();

	public CleaningItemCommand() {

		// possible commands for the first argument of this command
		cleaningItemCommands.add("rename");
		cleaningItemCommands.add("setLore");
		cleaningItemCommands.add("setItem");
		cleaningItemCommands.add("get");
		cleaningItemCommands.add("setActive");
		cleaningItemCommands.add("setDurabilityLoss");
		cleaningItemCommands.add("give");
		cleaningItemCommands.add("setEventDetectionMode");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		boolean isPlayer = sender instanceof Player;
		Player player = (Player) sender;

		if (args.length > 1) {

			/* RENAME SUBCOMMAND */
			if (args[0].equalsIgnoreCase(cleaningItemCommands.get(0))) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.rename") || !isPlayer) {

					String newname = new String();
					for (int i = 1; i < args.length; i++) {

						if (i == 1)
							newname = args[1];
						else
							newname = newname + " " + args[i];

					}

					newname = newname.replace("&", "�");
					if (isPlayer)
						MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
								StringTable.getMessage(MessageID.NEW_ITEM_NAME, "%itemname", newname), player);
					else
						MessageSystem.sendConsoleMessage(MessageType.SUCCESS,
								StringTable.getMessage(MessageID.NEW_ITEM_NAME, "%itemname", newname));

					ItemStack is = Main.item;
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(newname);
					Main.item.setItemMeta(im);
					Config.setItem(Main.item);
					if (args.length == 1)
						return true;

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							"chestcleaner.cmd.cleaningitem.rename", player);
					return true;
				}
				return true;

				/* SETLORE SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(cleaningItemCommands.get(1))) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.setlore") || !isPlayer) {

					String lore = args[1];
					for (int i = 2; i < args.length; i++) {
						lore = lore + " " + args[i];
					}

					String[] lorearray = lore.split("/n");

					ArrayList<String> lorelist = new ArrayList<>();

					for (String obj : lorearray) {
						obj = obj.replace("&", "�");
						lorelist.add(obj);

					}

					ItemMeta im = Main.item.getItemMeta();
					im.setLore(lorelist);
					Main.item.setItemMeta(im);
					Config.setItem(Main.item);
					Config.save();

					if (isPlayer)
						MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.NEW_ITEM_LORE, player);
					else
						MessageSystem.sendConsoleMessage(MessageType.SUCCESS, MessageID.NEW_ITEM_LORE);
					return true;

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							"chestcleaner.cmd.cleaningitem.setlore", player);
					return true;
				}

			}

		}

		if (args.length == 1) {

			/* RENAME SUBCOMMAND ERRORS */
			if (args[0].equalsIgnoreCase(cleaningItemCommands.get(0))) {
				if (isPlayer)
					MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR, "/cleaningitem rename <name>", player);
				else
					MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR, "/cleaningitem rename <name>");
				return true;
			}

			/* SETITEM SUBCOMMAND */
			else if (args[0].equalsIgnoreCase(cleaningItemCommands.get(2)) && isPlayer) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.setitem")) {

					ItemStack item = player.getInventory().getItemInMainHand().clone();
					if (item != null) {
						item.setDurability((short) 0);
						item.setAmount(1);
						Config.setItem(item);
						Main.item = item;
						MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
								StringTable.getMessage(MessageID.NEW_ITEM, "%newitem", item.toString()), player);
						return true;

					} else {
						MessageSystem.sendMessageToPlayer(MessageType.ERROR, MessageID.HOLD_AN_ITEM, player);
						return true;
					}
				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							"chestcleaner.cmd.cleaningitem.setitem", player);
					return true;
				}

				/* GET SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(cleaningItemCommands.get(3)) && isPlayer) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.get")) {

					player.getInventory().addItem(Main.item);
					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.GOT_ITEM, player);
					return true;

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							"chestcleaner.cmd.cleaningitem.get", player);
					return true;
				}

			}

		} else if (args.length == 2) {

			/* SETACTIVE SUBCOMMAND */
			if (args[0].equalsIgnoreCase(cleaningItemCommands.get(4))) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.setactive") || !isPlayer) {

					if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {

						boolean b = false;
						if (args[1].equalsIgnoreCase("true"))
							b = true;

						Config.setItemBoolean(b);
						Main.itemBoolean = b;

						if (b) {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,MessageID.ITEM_ACTIVATED, player);
						} else {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.ITEM_DEACTIVATED, player);
						}
						return true;

					} else {
						if (isPlayer)
							MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
									"/cleaningitem setactive <true/false>", player);
						else
							MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR,
									"/cleaningitem setactive <true/false>");
						return true;
					}

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							"chestcleaner.cmd.cleaningitem.setactive", player);
					return true;
				}

				/* SETDURIBILITYLOSS SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(cleaningItemCommands.get(5))) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.setdurabilityloss") || !isPlayer) {

					if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {

						boolean b = false;
						if (args[1].equalsIgnoreCase("true"))
							b = true;

						Config.setDurabilityLossBoolean(b);
						Main.durability = b;
						if (Main.durability) {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.DURABILITYLOSS_ACTIVATED, player);
						} else {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.DURABILITYLOSS_DEACTIVATED, player);
						}
						return true;

					} else {
						MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
								"/cleaningitem setactive <true/false>", player);
						return true;
					}

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							"chestcleaner.cmd.cleaningitem.durabilityloss", player);
					return true;
				}

				/* GIVE SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(cleaningItemCommands.get(6))) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.give") || !isPlayer) {

					Player p2 = Bukkit.getPlayer(args[1]);

					if (p2 != null) {

						p2.getInventory().addItem(Main.item);
						if (isPlayer)
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
									StringTable.getMessage(MessageID.PLAYER_GOT_ITEM, "%playername", player.getName()),
									player);
						else
							MessageSystem.sendConsoleMessage(MessageType.SUCCESS,
									StringTable.getMessage(MessageID.PLAYER_GOT_ITEM, "%playername", player.getName()));
						return true;

					} else {
						if (isPlayer)
							MessageSystem.sendMessageToPlayer(MessageType.ERROR,
									StringTable.getMessage(MessageID.PLAYER_IS_NOT_ONLINE, "%playername", args[1]), p2);
						else
							MessageSystem.sendConsoleMessage(MessageType.ERROR,
									StringTable.getMessage(MessageID.PLAYER_IS_NOT_ONLINE, "%playername", args[1]));
						return true;
					}

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							"chestcleaner.cmd.cleaningitem.give", player);
					return true;
				}

				/* SETEVENTDETECTIONMODE SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(cleaningItemCommands.get(7))) {

				if (player.hasPermission("chestcleaner.cmd.cleaningitem.seteventdetectionmode")) {

					boolean b = Boolean.parseBoolean(args[1]);
					Main.eventmode = b;
					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, StringTable.getMessage(
							MessageID.SET_INVENTORY_DETECTION_MODE, "%modeBoolean", String.valueOf(b)), player);
					Config.setMode(b);
					return true;

				} else {
					if (isPlayer)
						MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
								"chestcleaner.cmd.cleaningitem.seteventdetectionmode", player);
					return true;
				}

			} else {

				if (isPlayer)
					MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
							"/cleaningitem <setitem/setactive/setdurabilityLoss/get/give/rename/setlore/seteventdetectionmode>",
							player);
				else
					MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR,
							"/cleaningitem <setactive/setdurabilityloss/give/rename/setlore>");
				return true;
			}

		} else {
			if (isPlayer)
				MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR,
						"/cleaningitem <setitem/setactive/setdurabilityLoss/get/give/rename/setlore/seteventdetectionmode>",
						player);
			else
				MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR,
						"/cleaningitem <setactive/setdurabilityloss/give/rename/setlore>");
			return true;
		}

		return true;

	}
		
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		final List<String> completions = new ArrayList<>();
		switch (args.length) {
		case 0:
			StringUtil.copyPartialMatches(args[0], cleaningItemCommands, completions);
			break;
		case 1:
			StringUtil.copyPartialMatches(args[0], cleaningItemCommands, completions);
			break;
		case 2:
			/* SETACTIVE || SETDURABILITYLOSS || SETEVENTDETECTIONMODE */
			if (args[0].equalsIgnoreCase(cleaningItemCommands.get(4))
					|| args[0].equalsIgnoreCase(cleaningItemCommands.get(5))
					|| args[0].equalsIgnoreCase(cleaningItemCommands.get(7))) {

				ArrayList<String> commands = new ArrayList<>();
				commands.add("true");
				commands.add("false");

				StringUtil.copyPartialMatches(args[1], commands, completions);
			}

		}

		Collections.sort(completions);
		return completions;
	}

}
