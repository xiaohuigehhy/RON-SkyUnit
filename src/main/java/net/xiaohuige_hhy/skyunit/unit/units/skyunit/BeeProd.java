package net.xiaohuige_hhy.skyunit.unit.units.skyunit;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.building.production.StartProductionButton;
import com.solegendary.reignofnether.building.production.StopProductionButton;
import com.solegendary.reignofnether.hud.buttons.UnitSpawnButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.unit.units.piglins.GruntProd;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitEntityRegistrar;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;

import java.util.ArrayList;
import java.util.List;

public class BeeProd extends ProductionItem {
	
	public final static String itemName = "Bee";
	public final static ResourceCost cost = SkyUnitResourceCosts.BEE;
	
	public BeeProd() {
		super(cost);
		this.onComplete = (Level level, ProductionPlacement placement) -> {
			if (!level.isClientSide())
				placement.produceUnit((ServerLevel) level, SkyUnitEntityRegistrar.BEE_UNIT.get(), placement.ownerName, true);
		};
	}
	
	public String getItemName() {
		return BeeProd.itemName;
	}
	
	public UnitSpawnButton getPlaceButton() {
		return new UnitSpawnButton(
			itemName,
			ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/bee.png"),
			List.of(
				FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.bee"), Style.EMPTY.withBold(true)),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.bee.tooltip1"), Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.bee.tooltip2"), Style.EMPTY)
			)
		);
	}
	
	public StartProductionButton getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
		List<FormattedCharSequence> tooltipLines = new ArrayList<>(List.of(
			FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.bee"), Style.EMPTY.withBold(true)),
			ResourceCosts.getFormattedCost(cost),
			ResourceCosts.getFormattedPopAndTime(cost),
			FormattedCharSequence.forward("", Style.EMPTY),
			FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.bee.tooltip1"), Style.EMPTY),
			FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.bee.tooltip2"), Style.EMPTY)
		));
		
		return new StartProductionButton(
			GruntProd.itemName,
			ResourceLocation.fromNamespaceAndPath(ReignOfNether.MOD_ID, "textures/mobheads/bee.png"),
			hotkey,
			() -> false,
			() -> true,
			tooltipLines,
			this
		);
	}
	
	public StopProductionButton getCancelButton(ProductionPlacement prodBuilding, boolean first) {
		return new StopProductionButton(
			BeeProd.itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/bee.png"),
			prodBuilding,
			this,
			first
		);
	}
}