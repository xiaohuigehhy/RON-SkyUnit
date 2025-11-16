//package net.xiaohuige_hhy.skyunit.building.buildings.skyunit;
//
//import com.solegendary.reignofnether.ability.Ability;
//import com.solegendary.reignofnether.ability.abilities.BackToWorkBuilding;
//import com.solegendary.reignofnether.ability.abilities.CallToArmsBuilding;
//import com.solegendary.reignofnether.building.production.ProductionBuilding;
//import com.solegendary.reignofnether.building.production.ProductionItems;
//import com.solegendary.reignofnether.hud.AbilityButton;
//import com.solegendary.reignofnether.keybinds.Keybinding;
//import com.solegendary.reignofnether.keybinds.Keybindings;
//import com.solegendary.reignofnether.resources.ResourceCost;
//import com.solegendary.reignofnether.resources.ResourceCosts;
//import com.solegendary.reignofnether.util.Faction;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.Blocks;
//import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;
//
//public class SkyTown extends ProductionBuilding {
//
//	public final static String buildingName = "Sky Town";
//	public final static String structureName = "sky_town";
//	public final static ResourceCost cost = SkyUnitResourceCosts.SKY_TOWN;
//
//	public SkyTown() {
//		super(structureName, cost, true);
//		this.name = buildingName;
//		this.portraitBlock = Blocks.POLISHED_GRANITE;
//		this.icon = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/polished_granite.png");
//
//		this.buildTimeModifier = 0.331f; // 60s total build time with 3 villagers
//		this.canAcceptResources = true;
//
//		this.startingBlockTypes.add(Blocks.STONE_BRICK_STAIRS);
//		this.startingBlockTypes.add(Blocks.GRASS_BLOCK);
//		this.startingBlockTypes.add(Blocks.POLISHED_ANDESITE_STAIRS);
//
//		Ability callToArms = new CallToArmsBuilding();
//		this.ab.add(callToArms, Keybindings.keyV);
//		BackToWorkBuilding backToWork = new BackToWorkBuilding();
//		this.abilities.add(backToWork, Keybindings.build);
//
//		this.productions.add(ProductionItems.VILLAGER, Keybindings.keyQ);
//	}
//
//	@Override
//	public Faction getFaction() {
//		return null;
//	}
//
//	@Override
//	public AbilityButton getBuildButton(Keybinding var1) {
//		return null;
//	}
//}
