package io.github.graves501.chestcleanerx.config;

import io.github.graves501.chestcleanerx.main.ChestCleanerX;
import io.github.graves501.chestcleanerx.sorting.SortingPattern;
import io.github.graves501.chestcleanerx.sorting.evaluator.ItemEvaluatorType;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

@PrepareForTest({ Bukkit.class, ChestCleanerX.class })
public class PluginConfigTest {

    private PluginConfig pluginConfig;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() {
        //Setup for all JavaPlugin static calls
        PowerMockito.mockStatic(JavaPlugin.class);
        ChestCleanerX pluginMock = PowerMockito.mock(ChestCleanerX.class);
        Mockito.when(JavaPlugin.getPlugin(ChestCleanerX.class)).thenReturn(pluginMock);

        final Logger loggerMock = PowerMockito.mock(Logger.class);
        Mockito.when(JavaPlugin.getPlugin(ChestCleanerX.class).getLogger()).thenReturn(loggerMock);

        //Setup for all Bukkit static calls
        PowerMockito.mockStatic(Bukkit.class);
        final ItemFactory itemFactoryMock = PowerMockito.mock(ItemFactory.class);
        Mockito.when(Bukkit.getItemFactory()).thenReturn(itemFactoryMock);

        this.pluginConfig = PluginConfig.getInstance();
        //TODO test saving to real test file
        this.pluginConfig.yamlConfig = PowerMockito.mock(FileConfiguration.class);
        this.pluginConfig.configFile = PowerMockito.mock(File.class);
        this.pluginConfig.loadConfig();
    }

    @Test
    public void testCleaningItem() {
        final ItemStack defaultValue = new ItemStack(Material.IRON_HOE);
        final ItemStack newValue = new ItemStack(Material.STONE_HOE);

        Assert.assertEquals(this.pluginConfig.getCurrentCleaningItem().getType(), defaultValue.getType());

        pluginConfig.setAndSaveCurrentCleaningItem(newValue);
        Assert.assertEquals(this.pluginConfig.getCurrentCleaningItem().getType(), newValue.getType());
    }

    @Test
    public void testCooldownInSeconds() {
        final int defaultValue = 5;
        final int newValue = 10;

        Assert.assertEquals(this.pluginConfig.getCooldownInSeconds(), defaultValue);

        pluginConfig.setAndSaveCooldownInSeconds(newValue);
        Assert.assertEquals(this.pluginConfig.getCooldownInSeconds(), newValue);
    }

    @Test
    public void testIsCleanInventoryCommandActive() {
        final boolean defaultValue = true;
        final boolean newValue = false;

        Assert.assertEquals(this.pluginConfig.isCleanInventoryCommandActive(), defaultValue);

        pluginConfig.setAndSaveIsCleanInventoryCommandActive(newValue);
        Assert.assertEquals(this.pluginConfig.isCleanInventoryCommandActive(), newValue);
    }

    @Test
    public void testIsCooldownActive() {
        final boolean defaultValue = false;
        final boolean newValue = true;

        Assert.assertEquals(this.pluginConfig.isCooldownActive(), defaultValue);

        pluginConfig.setAndSaveIsCooldownActive(newValue);
        Assert.assertEquals(this.pluginConfig.isCooldownActive(), newValue);
    }

    @Test
    public void testIsDurabilityLossActive() {
        final boolean defaultValue = true;
        final boolean newValue = false;

        Assert.assertEquals(this.pluginConfig.isDurabilityLossActive(), defaultValue);

        pluginConfig.setAndSaveDurabilityLossActive(newValue);
        Assert.assertEquals(this.pluginConfig.isDurabilityLossActive(), newValue);
    }

    @Test
    public void testIsCleaningItemActive() {
        final boolean defaultValue = true;
        final boolean newValue = false;

        Assert.assertEquals(this.pluginConfig.isCleaningItemActive(), defaultValue);

        pluginConfig.setAndSaveIsCleaningItemActive(newValue);
        Assert.assertEquals(this.pluginConfig.isCleaningItemActive(), newValue);
    }

    @Test
    public void testIsOpenInventoryEventDetectionModeActive() {
        final boolean defaultValue = false;
        final boolean newValue = true;

        Assert.assertEquals(this.pluginConfig.isOpenInventoryEventDetectionModeActive(), defaultValue);

        pluginConfig.setAndSaveOpenInventoryEventDetectionModeActive(newValue);
        Assert.assertEquals(this.pluginConfig.isOpenInventoryEventDetectionModeActive(), newValue);
    }

    @Test
    public void testIsBlockRefillActive() {
        final boolean defaultValue = true;
        final boolean newValue = false;

        Assert.assertEquals(this.pluginConfig.isBlockRefillActive(), defaultValue);

        pluginConfig.setAndSaveIsBlockRefillActive(newValue);
        Assert.assertEquals(this.pluginConfig.isBlockRefillActive(), newValue);
    }

    @Test
    public void testIsConsumablesRefillActive() {
        final boolean defaultValue = true;
        final boolean newValue = false;

        Assert.assertEquals(this.pluginConfig.isConsumablesRefillActive(), defaultValue);

        pluginConfig.setAndSaveConsumablesRefillActive(newValue);
        Assert.assertEquals(this.pluginConfig.isConsumablesRefillActive(), newValue);
    }

    @Test
    public void testIsAutoSortChestActive() {
        final boolean defaultValue = false;
        final boolean newValue = true;

        Assert.assertEquals(this.pluginConfig.isAutoSortChestActive(), defaultValue);

        pluginConfig.setAndSaveIsAutoSortChestActive(newValue);
        Assert.assertEquals(this.pluginConfig.isAutoSortChestActive(), newValue);
    }

    @Test
    public void testDefaultItemEvaluatorType() {
        final ItemEvaluatorType defaultValue = ItemEvaluatorType.BACK_BEGIN_STRING;
        final ItemEvaluatorType newValue = ItemEvaluatorType.BEGIN_BACK_STRING;

        Assert.assertEquals(this.pluginConfig.getDefaultItemEvaluatorType(), defaultValue);

        pluginConfig.setAndSaveDefaultItemEvaluatorType(newValue);
        Assert.assertEquals(this.pluginConfig.getDefaultItemEvaluatorType(), newValue);
    }

    @Test
    public void testDefaultSortingPattern() {
        final SortingPattern defaultValue = SortingPattern.LEFT_TO_RIGHT_TOP_TO_BOTTOM;
        final SortingPattern newValue = SortingPattern.BOTTOM_TO_TOP_LEFT_TO_RIGHT;

        Assert.assertEquals(this.pluginConfig.getDefaultSortingPattern(), defaultValue);

        pluginConfig.setAndSaveDefaultSortingPattern(newValue);
        Assert.assertEquals(this.pluginConfig.getDefaultSortingPattern(), newValue);
    }
}
