package io.github.graves501.chestcleanerx.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.github.graves501.chestcleanerx.commands.BlacklistCommand;
import io.github.graves501.chestcleanerx.main.Main;
import io.github.graves501.chestcleanerx.playerdata.PlayerDataManager;
import io.github.graves501.chestcleanerx.sorting.InventorySorter;
import io.github.graves501.chestcleanerx.timer.Timer;
import io.github.graves501.chestcleanerx.utils.BlockDetector;
import io.github.graves501.chestcleanerx.utils.messages.MessageID;
import io.github.graves501.chestcleanerx.utils.messages.MessageSystem;
import io.github.graves501.chestcleanerx.utils.messages.MessageType;
import io.github.graves501.chestcleanerx.utils.messages.Messages;

/**
 * @author Tom2208
 */
public class SortingListener implements org.bukkit.event.Listener {



	@EventHandler
	private void onRightClick(PlayerInteractEvent playerInteractEvent) {

		final Player player = playerInteractEvent.getPlayer();
		ItemStack itemMainHand = player.getInventory().getItemInMainHand().clone();
		itemMainHand.setDurability((short) 0);
		itemMainHand.setAmount(1);

		ItemStack itemOffHand = player.getInventory().getItemInOffHand().clone();
		itemOffHand.setDurability((short) 0);
		itemOffHand.setAmount(1);

		if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {

			boolean isMainHand = itemMainHand.equals(Main.cleaningItem);
			boolean isOffHand = itemOffHand.equals(Main.cleaningItem);

			// TODO clean up
			// TODO RIGHTCLICK WIRD WOHL ZWEI MAL AUFGERUFEN, WENN MAN IN BIEDEN
			// SLOTS DAS ITEM H�LT

			if ((isMainHand || isOffHand) && (isMainHand != isOffHand)) {

				if (player.isSneaking()) {

					if (player.hasPermission("chestcleaner.cleaningItem.use.owninventory")) {
						if (!Timer.playerCheck(player))
							return;

						damageItem(player, isMainHand);
						InventorySorter.sortPlayerInv(
							player, PlayerDataManager.getSortingPatternOfPlayer(player), PlayerDataManager.getEvaluatorTypOfPlayer(
								player));
						InventorySorter.playSortingSound(player);

						MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.INVENTORY_SORTED,
							player);

						playerInteractEvent.setCancelled(true);
					}

				} else if (!Main.eventmode) {

					if (player.hasPermission("chestcleaner.cleaningItem.use")) {

						Block block = BlockDetector.getTargetBlock(player);

						if (BlacklistCommand.inventoryBlacklist.contains(block.getType())) {
							return;
						}

						if (!Timer.playerCheck(player)) {
							return;
						}

						if (InventorySorter.sortPlayerBlock(block,
							player, PlayerDataManager.getSortingPatternOfPlayer(player), PlayerDataManager.getEvaluatorTypOfPlayer(
								player))) {

							damageItem(player, isMainHand);

							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
									Messages.getMessage(MessageID.INVENTORY_SORTED), player);
							playerInteractEvent.setCancelled(true);
						}

					}

				}
			}

		}

	}

	/**
	 * Damages the item in the Hand of the {@code player} (using
	 * player.getItemInHand()), if the {@code durability} (in class Main) is
	 * true. Damaging means, that stackable items (maxStackSize > 1) get reduced
	 * in amount by one, not stackable items get damaged and removed, if they
	 * reach the highest durability .
	 *
	 * @param player
	 *            the player who is holding the item, that you want to get
	 *            damaged, in hand.
	 */
	private void damageItem(Player player, boolean isHoldingInMainHand) {

		if (Main.durability) {

			ItemStack item;
			if (isHoldingInMainHand) {
				item = player.getInventory().getItemInMainHand();
			} else {
				item = player.getInventory().getItemInOffHand();
			}

			if (item.getMaxStackSize() == 1)
				item.setDurability((short) (item.getDurability() + 1));

			if (item.getDurability() >= item.getType().getMaxDurability()) {
				item.setAmount(item.getAmount() - 1);
			}
		}

	}

	@EventHandler
	private void onOpenInventory(InventoryOpenEvent e) {

		if (Main.eventmode) {

			Player p = (Player) e.getPlayer();

			if (p.hasPermission("chestcleaner.cleaningItem.use")) {

				ItemStack itemMainHand = p.getInventory().getItemInMainHand().clone();
				itemMainHand.setDurability((short) 0);
				itemMainHand.setAmount(1);

				ItemStack itemOffHand = p.getInventory().getItemInOffHand().clone();
				itemOffHand.setDurability((short) 0);
				itemOffHand.setAmount(1);

				boolean isMainHand = itemMainHand.equals(Main.cleaningItem);
				boolean isOffHand = itemOffHand.equals(Main.cleaningItem);

				if (isMainHand || isOffHand) {

					if (!Timer.playerCheck(p))
						return;

					InventorySorter.sortInventory(e.getInventory(), PlayerDataManager.getSortingPatternOfPlayer(p), PlayerDataManager.getEvaluatorTypOfPlayer(p));
					InventorySorter.playSortingSound(p);

					damageItem(p, isMainHand);

					e.setCancelled(true);
					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
							Messages.getMessage(MessageID.INVENTORY_SORTED), p);
				}

			}

		}

	}

	@EventHandler
	private void onCloseInventory(InventoryCloseEvent inventoryCloseEvent){

		if(inventoryCloseEvent.getInventory().getHolder() instanceof Chest){

			Player player = (Player) inventoryCloseEvent.getPlayer();

			if(PlayerDataManager.getAutoSortOfPlayer(player)){

				if(!Timer.playerCheck(player)){
					return;
				}

				InventorySorter.sortInventoryByPlayer(inventoryCloseEvent.getInventory(), player);
				InventorySorter.playSortingSound(player);
				MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.INVENTORY_SORTED, player);

			}

		}

	}

}
