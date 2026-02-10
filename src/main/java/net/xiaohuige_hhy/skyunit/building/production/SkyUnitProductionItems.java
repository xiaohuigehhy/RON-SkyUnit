package net.xiaohuige_hhy.skyunit.building.production;

import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.building.production.ProductionItem;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.research.researchItems.ResearchMysteriousLibrary;
import net.xiaohuige_hhy.skyunit.research.researchItems.ResearchNarrowLavaRiver;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.BeeProd;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.ParrotProd;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.PhantomProd;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerProd;

public class SkyUnitProductionItems {
	public static final BeeProd BEE = register(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "bee"), new BeeProd());
	public static final ParrotProd PARROT = register(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "parrot"), new ParrotProd());
	public static final PhantomProd PHANTOM = register(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "phantom"), new PhantomProd());
	public static final IllusionerProd ILLUSIONER = register(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "illusioner"), new IllusionerProd());
	public static final ResearchMysteriousLibrary RESEARCH_MYSTERIOUS_LIBRARY = register(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "mysterious_library"), new ResearchMysteriousLibrary());
	public static final ResearchNarrowLavaRiver RESEARCH_NARROW_LAVA_RIVER = register(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "narrow_lava_river"), new ResearchNarrowLavaRiver());
	
	private static <T extends ProductionItem> T register(ResourceLocation id, T building) {
		return Registry.register(ReignOfNetherRegistries.PRODUCTION_ITEM, id, building);
	}
	
	public static void init() {
	}
}
