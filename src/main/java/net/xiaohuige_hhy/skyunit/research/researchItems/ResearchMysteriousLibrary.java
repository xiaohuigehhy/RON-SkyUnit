package net.xiaohuige_hhy.skyunit.research.researchItems;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.BuildingServerboundPacket;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.building.buildings.villagers.Library;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.research.researchItems.ResearchGrandLibrary;
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

public class ResearchMysteriousLibrary extends ProductionItem {
	
	public final static String itemName = "Mysterious Library";
	public final static ResourceCost cost = SkyUnitResourceCosts.RESEARCH_MYSTERIOUS_LIBRARY;
	public final static String mysteriousStructureName = "library_mysterious";
	
	public ResearchMysteriousLibrary() {
		super(cost);
		this.onComplete = (Level level, ProductionPlacement placement) -> {
			if (placement.getBuilding() instanceof Library)
				placement.changeStructure(mysteriousStructureName);
		};
	}
	
	public String getItemName() {
		return ResearchMysteriousLibrary.itemName;
	}
	
	public Button getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
		return new Button(
			ResearchMysteriousLibrary.itemName,
			14,
			new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/icon_frame_bronze.png"),
			Keybindings.keyL,
			() -> false,
			() -> SkyUnitProductionItems.RESEARCH_MYSTERIOUS_LIBRARY.itemIsBeingProducedAt(prodBuilding) ||
				(prodBuilding.getBuilding() instanceof Library && prodBuilding.getUpgradeLevel() != 1),
			() -> true,
			() -> BuildingServerboundPacket.startProduction(SkyUnitProductionItems.RESEARCH_MYSTERIOUS_LIBRARY),
			null,
			List.of(
				FormattedCharSequence.forward(I18n.get("research.reignofnether.mysterious_library"), Style.EMPTY.withBold(true)),
				ResourceCosts.getFormattedCost(cost),
				ResourceCosts.getFormattedTime(cost),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("research.reignofnether.mysterious_library.tooltip1"), Style.EMPTY)
			)
		);
	}
	
	public Button getCancelButton(ProductionPlacement prodBuilding, boolean first) {
		return new Button(
			ResearchGrandLibrary.itemName,
			14,
			new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/icon_frame_bronze.png"),
			null,
			() -> false,
			() -> false,
			() -> true,
			() -> BuildingServerboundPacket.cancelProduction(prodBuilding.minCorner, SkyUnitProductionItems.RESEARCH_MYSTERIOUS_LIBRARY, first),
			null,
			null
		);
	}
	
}
