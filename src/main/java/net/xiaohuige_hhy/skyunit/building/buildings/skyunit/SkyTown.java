package net.xiaohuige_hhy.skyunit.building.buildings.skyunit;

import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.BuildingPlaceButton;
import com.solegendary.reignofnether.building.production.ProductionBuilding;
import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Blocks;
import net.xiaohuige_hhy.skyunit.building.SkyUnitBuildings;
import net.xiaohuige_hhy.skyunit.building.buildings.SkyUnitBuilding;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;

import java.util.List;

public class SkyTown extends ProductionBuilding implements SkyUnitBuilding {
	
	public final static String buildingName = "Sky Town";
	public final static String structureName = "sky_town";
	public final static ResourceCost cost = SkyUnitResourceCosts.SKY_TOWN;
	
	public SkyTown() {
		super(structureName, cost, true);
		this.name = buildingName;
		this.portraitBlock = Blocks.RED_WOOL;
		this.icon = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/red_wool.png");
		
		this.buildTimeModifier = 0.331f; // 60s total build time with 3 parrots
		this.canAcceptResources = true;
		
		this.startingBlockTypes.add(Blocks.MUD_BRICKS);
		this.startingBlockTypes.add(Blocks.GRASS_BLOCK);
		
		this.productions.add(SkyUnitProductionItems.PARROT, Keybindings.keyQ);
	}
	
	@Override
	public Faction getFaction() {
		return Faction.NONE;
	}
	
	public BuildingPlaceButton getBuildButton(Keybinding hotkey) {
		ResourceLocation key = ReignOfNetherRegistries.BUILDING.getKey(this);
		String name = null;
		if (key != null) {
			name = I18n.get("buildings." + getFaction().name().toLowerCase() + "." + key.getNamespace() + "." + key.getPath());
		}
		return new BuildingPlaceButton(
			name,
			ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/white_wool.png"),
			hotkey,
			() -> BuildingClientEvents.getBuildingToPlace() == SkyUnitBuildings.SKY_TOWN,
			() -> false,
			() -> true,
			List.of(
				FormattedCharSequence.forward(I18n.get("buildings.skyunit.reignofnether.skytown"), Style.EMPTY.withBold(true)),
				ResourceCosts.getFormattedCost(cost),
				ResourceCosts.getFormattedPop(cost),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("buildings.skyunit.reignofnether.skytown.tooltip1"), Style.EMPTY)
			),
			this
		);
	}
}
