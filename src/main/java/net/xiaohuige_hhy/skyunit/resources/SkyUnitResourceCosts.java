package net.xiaohuige_hhy.skyunit.resources;

import com.solegendary.reignofnether.resources.ResourceCost;

import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.config.SkyUnitCommonConfigs;

public class SkyUnitResourceCosts {
	
	private static final String ID = SkyUnit.MOD_ID;
	
	public static final ResourceCost ILLUSIONER = new ResourceCost(ID, "ILLUSIONER");
	public static final ResourceCost RESEARCH_MYSTERIOUS_LIBRARY = new ResourceCost(ID, "RESEARCH_MYSTERIOUS_LIBRARY");
	public static final ResourceCost RESEARCH_NARROW_LAVA_RIVER = new ResourceCost(ID, "RESEARCH_LAVA_RIVER1");
	
	public static void deferredLoadResourceCosts() {
		ILLUSIONER.bakeValues(SkyUnitCommonConfigs.UnitCosts.ILLUSIONER);
		RESEARCH_MYSTERIOUS_LIBRARY.bakeValues(SkyUnitCommonConfigs.ResearchCosts.RESEARCH_MYSTERIOUS_LIBRARY);
		RESEARCH_NARROW_LAVA_RIVER.bakeValues(SkyUnitCommonConfigs.ResearchCosts.RESEARCH_NARROW_LAVA_RIVER);
	}
	
}
