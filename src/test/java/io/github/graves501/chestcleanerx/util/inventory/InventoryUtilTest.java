package io.github.graves501.chestcleanerx.util.inventory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.graves501.chestcleanerx.config.PluginConfig;
import io.github.graves501.chestcleanerx.main.ChestCleanerX;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

@PrepareForTest({Bukkit.class, ChestCleanerX.class})
public class InventoryUtilTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private ItemStack cleaningItem;
    private ItemStack nonCleaningItem;
    private Player playerMock;
    private PlayerInventory playerInventoryMock;

    @Before
    public void setUp() {
        //Setup for all Bukkit static calls
        PowerMockito.mockStatic(Bukkit.class);
        ItemFactory itemFactoryMock = PowerMockito.mock(ItemFactory.class);
        Mockito.when(Bukkit.getItemFactory()).thenReturn(itemFactoryMock);

        cleaningItem = new ItemStack(Material.IRON_HOE);
        nonCleaningItem = new ItemStack(Material.STONE_HOE);
        playerMock = mock(Player.class);
        playerInventoryMock = mock(PlayerInventory.class);
        when(playerMock.getInventory()).thenReturn(playerInventoryMock);
    }

    @Test
    public void testGetItemInMainHand() {
        when(playerInventoryMock.getItemInMainHand()).thenReturn(cleaningItem);

        ItemStack itemInMainHand = InventoryUtil.getItemInMainHand(playerMock);
        assertTrue(itemInMainHand.isSimilar(cleaningItem));

        when(playerInventoryMock.getItemInMainHand()).thenReturn(nonCleaningItem);
        itemInMainHand = InventoryUtil.getItemInMainHand(playerMock);
        assertFalse(itemInMainHand.isSimilar(cleaningItem));
    }

    @Test
    public void testGetItemInOffHand() {
        when(playerInventoryMock.getItemInOffHand()).thenReturn(cleaningItem);

        ItemStack itemInMainHand = InventoryUtil.getItemInOffHand(playerMock);
        assertTrue(itemInMainHand.isSimilar(cleaningItem));

        when(playerInventoryMock.getItemInOffHand()).thenReturn(nonCleaningItem);
        itemInMainHand = InventoryUtil.getItemInOffHand(playerMock);
        assertFalse(itemInMainHand.isSimilar(cleaningItem));
    }
}
