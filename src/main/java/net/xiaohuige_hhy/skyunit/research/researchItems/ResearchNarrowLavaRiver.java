package net.xiaohuige_hhy.skyunit.research.researchItems;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.buildings.placements.CentralPortalPlacement;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.building.production.ProdDupeRule;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.building.production.StartProductionButton;
import com.solegendary.reignofnether.building.production.StopProductionButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.research.ResearchServerEvents;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;

import java.util.List;

public class ResearchNarrowLavaRiver extends ProductionItem {
	
	public final static String itemName = "Narrow Research Lava River";
	public final static ResourceCost cost = SkyUnitResourceCosts.RESEARCH_NARROW_LAVA_RIVER;
	
	public ResearchNarrowLavaRiver() {
		super(cost, ProdDupeRule.DISALLOW);
		this.onComplete = (Level level, ProductionPlacement placement) -> {
			if (level.isClientSide()) {
				ResearchClient.addResearch(placement.ownerName, SkyUnitProductionItems.RESEARCH_NARROW_LAVA_RIVER);
			} else {
				ResearchServerEvents.addResearch(placement.ownerName, SkyUnitProductionItems.RESEARCH_NARROW_LAVA_RIVER);
			}
		};
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public StartProductionButton getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
		return new StartProductionButton(
			itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/block/lava.png"),
			ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/hud/icon_frame_bronze.png"),
			hotkey,
			() -> itemIsBeingProducedAt(prodBuilding) ||
				(prodBuilding instanceof CentralPortalPlacement centralPortalPlacement && centralPortalPlacement.getUpgradeLevel() != 0),
			() -> true,
			List.of(
				FormattedCharSequence.forward(I18n.get("research.reignofnether.beacon_level1"), Style.EMPTY.withBold(true)),
				ResourceCosts.getFormattedCost(cost),
				ResourceCosts.getFormattedTime(cost),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("research.reignofnether.beacon_level1.tooltip1"), Style.EMPTY),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("research.reignofnether.beacon_level_win"), Style.EMPTY)
			),
			this
		);
	}
	
	public StopProductionButton getCancelButton(ProductionPlacement prodBuilding, boolean first) {
		return new StopProductionButton(
			itemName,
			ResourceLocation.fromNamespaceAndPath("skyunit", "textures/block/lava.png"),
			ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/hud/icon_frame_bronze.png"),
			prodBuilding,
			this,
			first
		);
	}
}
