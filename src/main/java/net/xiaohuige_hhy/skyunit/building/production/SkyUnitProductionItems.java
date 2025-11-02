package net.xiaohuige_hhy.skyunit.building.production;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.building.production.ProductionItem;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.research.researchItems.ResearchMysteriousLibrary;
import net.xiaohuige_hhy.skyunit.research.researchItems.ResearchNarrowLavaRiver;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerProd;

public class SkyUnitProductionItems {
	public static final IllusionerProd ILLUSIONER = register(new ResourceLocation(SkyUnit.MOD_ID, "illusioner"), new IllusionerProd());
	public static final ResearchMysteriousLibrary RESEARCH_MYSTERIOUS_LIBRARY = register(new ResourceLocation(SkyUnit.MOD_ID, "mysterious_library"), new ResearchMysteriousLibrary());
	public static final ResearchNarrowLavaRiver RESEARCH_NARROW_LAVA_RIVER = register(new ResourceLocation(SkyUnit.MOD_ID, "narrow_lava_river"), new ResearchNarrowLavaRiver());
	
	private static <T extends ProductionItem> T register(ResourceLocation id, T building) {
		return Registry.register(ReignOfNetherRegistries.PRODUCTION_ITEM, id, building);
	}
	
	public static void init() {}
}
