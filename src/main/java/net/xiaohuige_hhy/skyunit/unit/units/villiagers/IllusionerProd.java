package net.xiaohuige_hhy.skyunit.unit.units.villiagers;

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

import java.util.List;

public class IllusionerProd extends ProductionItem {
	
	public final static String itemName = "Illusioner";
	public final static ResourceCost cost = SkyUnitResourceCosts.ILLUSIONER;
	
	public IllusionerProd() {
		super(cost);
		this.onComplete = (Level level, ProductionPlacement placement) -> {
			if (!level.isClientSide())
				placement.produceUnit((ServerLevel) level, SkyUnitEntityRegistrar.ILLUSIONER_UNIT.get(), placement.ownerName, true);
		};
	}
	
	public String getItemName() {
		return IllusionerProd.itemName;
	}
	
	public UnitSpawnButton getPlaceButton() {
		return new UnitSpawnButton(
			itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			List.of(
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner"), Style.EMPTY.withBold(true)),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner.tooltip1"), Style.EMPTY),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner.tooltip2"), Style.EMPTY)
			)
		);
	}
	
	public StartProductionButton getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
		return new StartProductionButton(
			IllusionerProd.itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			hotkey,
			() -> false,
			() -> true,
			List.of(
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner"), Style.EMPTY.withBold(true)),
				ResourceCosts.getFormattedCost(cost),
				ResourceCosts.getFormattedPopAndTime(cost),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner.tooltip1"), Style.EMPTY),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner.tooltip2"), Style.EMPTY),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner.tooltip3"), Style.EMPTY)
			),
			this
		);
	}
	
	public StopProductionButton getCancelButton(ProductionPlacement prodBuilding, boolean first) {
		return new StopProductionButton(
			IllusionerProd.itemName,
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			prodBuilding,
			this,
			first
		);
	}
}
