package net.xiaohuige_hhy.skyunit.registars;

import com.solegendary.reignofnether.registrars.ItemRegistrar;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.blocks.LavaRiverBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SkyUnitBlockRegistrar {
	
	public static final DeferredRegister<Block> BLOCKS =
		DeferredRegister.create(ForgeRegistries.BLOCKS, SkyUnit.MOD_ID);
	public static Map<ResourceKey<CreativeModeTab>, List<Item>> blockItems = new HashMap<>();
	public static final RegistryObject<Block> LAVA_RIVER_BLOCK = registerBlock("lava_river_block", () ->
			new LavaRiverBlock(BlockBehaviour.Properties.of()
				.mapColor(MapColor.FIRE)
				.noCollission()
				.randomTicks()
				.strength(100.0F)
				.lightLevel((p_220867_) -> 15)
				.isValidSpawn((p_187421_, p_187422_, p_187423_, p_187424_) -> p_187424_.fireImmune())
				),
		CreativeModeTabs.BUILDING_BLOCKS);
	
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, ResourceKey<CreativeModeTab> tab) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn, tab);
		return toReturn;
	}
	
	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, ResourceKey<CreativeModeTab> tab) {
		ItemRegistrar.ITEMS.register(name, () -> {
			BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
			blockItems.computeIfAbsent(tab, (k) -> new ArrayList<>()).add(blockItem);
			return blockItem;
		});
	}
	
	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
}
