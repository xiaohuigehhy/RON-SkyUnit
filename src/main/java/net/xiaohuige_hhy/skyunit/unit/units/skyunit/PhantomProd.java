package net.xiaohuige_hhy.skyunit.unit.units.skyunit;

import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.building.production.StartProductionButton;
import com.solegendary.reignofnether.building.production.StopProductionButton;
import com.solegendary.reignofnether.hud.buttons.UnitSpawnButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;

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

public class PhantomProd extends ProductionItem {
	
	public final static String itemName = "Phantom";
	public final static ResourceCost cost = SkyUnitResourceCosts.PHANTOM;
	
	public PhantomProd() {
		super(cost);
		this.onComplete = (Level level, ProductionPlacement placement) -> {
			if (!level.isClientSide())
				placement.produceUnit((ServerLevel) level, SkyUnitEntityRegistrar.PHANTOM_UNIT.get(), placement.ownerName, true);
		};
	}
	
	public String getItemName() {
		return PhantomProd.itemName;
	}
	
	public UnitSpawnButton getPlaceButton() {
		return new UnitSpawnButton(
			itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/phantom.png"),
			List.of(
				FormattedCharSequence.forward(I18n.get("units.skyunit.phantom"), Style.EMPTY.withBold(true)),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.skyunit.phantom.tooltip1"), Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.skyunit.phantom.tooltip2"), Style.EMPTY)
			)
		);
	}
	
	public StartProductionButton getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
		List<FormattedCharSequence> tooltipLines = new ArrayList<>(List.of(
			FormattedCharSequence.forward(I18n.get("units.skyunit.phantom"), Style.EMPTY.withBold(true)),
			ResourceCosts.getFormattedCost(cost),
			ResourceCosts.getFormattedPopAndTime(cost),
			FormattedCharSequence.forward("", Style.EMPTY),
			FormattedCharSequence.forward(I18n.get("units.skyunit.phantom.tooltip1"), Style.EMPTY),
			FormattedCharSequence.forward(I18n.get("units.skyunit.phantom.tooltip2"), Style.EMPTY)
		));
		
		return new StartProductionButton(
			PhantomProd.itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/phantom.png"),
			hotkey,
			() -> false,
			() -> true,
			tooltipLines,
			this
		);
	}
	
	public StopProductionButton getCancelButton(ProductionPlacement prodBuilding, boolean first) {
		return new StopProductionButton(
			PhantomProd.itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/phantom.png"),
			prodBuilding,
			this,
			first
		);
	}
}