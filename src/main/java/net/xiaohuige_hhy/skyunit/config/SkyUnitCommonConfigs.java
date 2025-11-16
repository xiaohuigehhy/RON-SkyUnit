package net.xiaohuige_hhy.skyunit.config;

import com.solegendary.reignofnether.config.ResourceCostConfigEntry;

import net.minecraftforge.common.ForgeConfigSpec;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;

public class SkyUnitCommonConfigs {
	
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	static {
		BUILDER.push("SkyUnit Configuration File");
		BUILDER.pop();
		BUILDER.comment("SkyUnit Unit cost configurations");
		
		BUILDER.comment("Villagers");
		UnitCosts.ILLUSIONER.define(BUILDER);
		ResearchCosts.RESEARCH_MYSTERIOUS_LIBRARY.define(BUILDER);
		ResearchCosts.RESEARCH_NARROW_LAVA_RIVER.define(BUILDER);
		ResearchCosts.SKY_TOWN.define(BUILDER);
		SPEC = BUILDER.build();
	}
	
	public static class UnitCosts implements Costs {
		public static final ResourceCostConfigEntry ILLUSIONER = ResourceCostConfigEntry.Unit(120, 80, 0, 0, 5, SkyUnitResourceCosts.ILLUSIONER, "Illusioner Config");
	}
	
	public static class ResearchCosts implements Costs {
		
		public static final ResourceCostConfigEntry RESEARCH_MYSTERIOUS_LIBRARY = ResourceCostConfigEntry.Research(0, 500, 200, 140, SkyUnitResourceCosts.RESEARCH_MYSTERIOUS_LIBRARY, "Mysterious Library Research Config");
		public static final ResourceCostConfigEntry RESEARCH_NARROW_LAVA_RIVER = ResourceCostConfigEntry.Research(0, 0, 200, 120, SkyUnitResourceCosts.RESEARCH_NARROW_LAVA_RIVER, "Narrow Lava River Research Config");
		public static final ResourceCostConfigEntry SKY_TOWN = ResourceCostConfigEntry.Research(0, 350, 250, 0, SkyUnitResourceCosts.SKY_TOWN, "Sky Town");
	}
	
	public interface Costs {}
}
