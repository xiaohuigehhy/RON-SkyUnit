package net.xiaohuige_hhy.skyunit.unit.units.villiagers;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.BuildingServerboundPacket;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;
import com.solegendary.reignofnether.building.production.ProductionItem;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.sandbox.SandboxAction;
import com.solegendary.reignofnether.sandbox.SandboxClientEvents;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;
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
	
	public AbilityButton getPlaceButton() {
		return new AbilityButton(
			itemName,
			new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			null,
			() -> SandboxClientEvents.spawnUnitName.equals(itemName),
			() -> false,
			() -> true,
			() -> {
				CursorClientEvents.setLeftClickSandboxAction(SandboxAction.SPAWN_UNIT);
				SandboxClientEvents.spawnUnitName = itemName;
			},
			null,
			List.of(
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner"), Style.EMPTY.withBold(true)),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner.tooltip1"), Style.EMPTY),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("units.villagers.skyunit.illusioner.tooltip2"), Style.EMPTY)
			),
			null
		);
	}
	
	public Button getStartButton(ProductionPlacement prodBuilding, Keybinding hotkey) {
		return new Button(
			IllusionerProd.itemName,
			14,
			new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			hotkey,
			() -> false,
			() -> false,
			() -> true,
			() -> BuildingServerboundPacket.startProduction(SkyUnitProductionItems.ILLUSIONER),
			null,
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
			)
		);
	}
	
	public Button getCancelButton(ProductionPlacement prodBuilding, boolean first) {
		return new Button(
			IllusionerProd.itemName,
			14,
			new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			(Keybinding) null,
			() -> false,
			() -> false,
			() -> true,
			() -> BuildingServerboundPacket.cancelProduction(prodBuilding.originPos, SkyUnitProductionItems.ILLUSIONER, first),
			null,
			null
		);
	}
}
