package net.xiaohuige_hhy.skyunit.registars;


import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xiaohuige_hhy.skyunit.SkyUnit;

public class SkyUnitItemRegistrar {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SkyUnit.MOD_ID);
	
	public static final RegistryObject<ForgeSpawnEggItem> SKELETON_HORSE_SUMMON_UNIT =
		ITEMS.register("skeleton_horse_summon_unit_spawn_egg", () -> new ForgeSpawnEggItem(SkyUnitEntityRegistrar.SKELETON_HORSE_SUMMON_UNIT,
			6842447, 15066584, new Item.Properties()));
	
	public static final RegistryObject<ForgeSpawnEggItem> ILLUSIONER_UNIT =
		ITEMS.register("illusioner_unit_spawn_egg", () -> new ForgeSpawnEggItem(SkyUnitEntityRegistrar.ILLUSIONER_UNIT,
			7697781, 31436, new Item.Properties()));
	
	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
}
