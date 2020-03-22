package io.github.graves501.chestcleanerx.listener;

import io.github.graves501.chestcleanerx.configuration.PluginConfig;
import io.github.graves501.chestcleanerx.util.InventoryDetector;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RefillListener implements org.bukkit.event.Listener {

    @EventHandler
    private void onPlacingBlock(BlockPlaceEvent blockPlaceEvent) {
        final PluginConfig pluginConfiguration = PluginConfig.getInstance();

        if (pluginConfiguration.isBlockRefillActive() && !blockPlaceEvent.isCancelled()) {
            final Player player = blockPlaceEvent.getPlayer();

            if (player.getGameMode().equals(GameMode.SURVIVAL)) {

                ItemStack item = blockPlaceEvent.getItemInHand();
                if (item.getAmount() == 1) {

                    if (player.hasPermission("chestcleaner.autorefill.blocks")) {

                        Material material = blockPlaceEvent.getBlockPlaced().getType();

                        if (material.toString().contains("STRIPPED")) {
                            if (player.getInventory().getItemInMainHand().getType().toString()
                                .contains("_AXE")
                                || player.getInventory().getItemInOffHand().getType().toString()
                                .contains("_AXE")) {
                                return;
                            }
                        }

                        ItemStack[] items = InventoryDetector
                            .getHotbarAndMainInventoryItems(player.getInventory());

                        for (int i = 9; i < 36; i++) {

                            if (items[i] != null) {

                                if (items[i].getType().equals(material)) {

                                    if (blockPlaceEvent.getHand().equals(EquipmentSlot.HAND)) {
                                        player.getInventory().setItemInMainHand(items[i]);
                                        player.getInventory().setItem(i, null);
                                        break;
                                    } else if (blockPlaceEvent.getHand()
                                        .equals(EquipmentSlot.OFF_HAND)) {
                                        player.getInventory().setItemInOffHand(items[i]);
                                        player.getInventory().setItem(i, null);
                                        break;
                                    }

                                }

                            }

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onConsuming(PlayerItemConsumeEvent playerItemConsumeEvent) {

        final PluginConfig pluginConfiguration = PluginConfig.getInstance();

        if (pluginConfiguration.isConsumablesRefillActive() && !playerItemConsumeEvent
            .isCancelled()) {

            if (!Bukkit.getVersion().contains("1.8")) {

                Player player = playerItemConsumeEvent.getPlayer();
                if (player.getGameMode().equals(GameMode.SURVIVAL)) {

                    ItemStack item = playerItemConsumeEvent.getItem();
                    if (item.getAmount() == 1) {

                        if (player.hasPermission("chestcleaner.autorefill.consumables")) {

                            if (item.getMaxStackSize() > 1) {
                                int hand = -1; // -1 = nothing, 0 = hand, 1 =
                                // offhand

                                if (player.getInventory()
                                    .getItem(player.getInventory().getHeldItemSlot()) != null) {
                                    if (player.getInventory()
                                        .getItem(player.getInventory().getHeldItemSlot()).getType()
                                        .equals(item.getType())) {
                                        hand = 0;
                                    } else if (player.getInventory().getItemInOffHand() != null) {
                                        if (player.getInventory().getItemInOffHand().getType()
                                            .equals(item.getType())) {
                                            hand = 1;
                                        }
                                    }
                                } else if (player.getInventory().getItemInOffHand() != null) {
                                    if (player.getInventory().getItemInOffHand().getType()
                                        .equals(item.getType())) {
                                        hand = 1;
                                    }

                                }

                                if (hand > -1) {

                                    ItemStack[] items = InventoryDetector
                                        .getHotbarAndMainInventoryItems(player.getInventory());
                                    for (int i = 9; i < 36; i++) {
                                        if (items[i] != null) {
                                            if (items[i].getType().equals(item.getType())
                                                && items[i].getAmount() > 1) {
                                                items[i].setAmount(items[i].getAmount() + 1);
                                                if (hand == 0) {
                                                    playerItemConsumeEvent.setItem(items[i]);
                                                    player.getInventory().setItem(i, null);
                                                    break;
                                                } else if (hand == 1) {
                                                    playerItemConsumeEvent.setItem(items[i]);
                                                    player.getInventory().setItem(i, null);
                                                    break;
                                                }

                                            }

                                        }

                                    }

                                }

                            }
                        }
                    }
                }
            }
        }

    }

}
