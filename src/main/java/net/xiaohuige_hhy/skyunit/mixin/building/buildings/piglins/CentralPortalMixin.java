package net.xiaohuige_hhy.skyunit.mixin.building.buildings.piglins;

import com.solegendary.reignofnether.building.BuildingServerEvents;
import com.solegendary.reignofnether.building.NetherZone;
import com.solegendary.reignofnether.building.buildings.piglins.CentralPortal;
import com.solegendary.reignofnether.building.production.ProductionBuilding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.resources.ResourceCost;

import net.minecraft.server.level.ServerLevel;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CentralPortal.class)
public abstract class CentralPortalMixin extends ProductionBuilding {
	
	public CentralPortalMixin(String structureName, ResourceCost cost, boolean isCapitol) {
		super(structureName, cost, isCapitol);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"), remap = false)
	private void addLavaRiverResearch(CallbackInfo ci) {
		this.productions.add(SkyUnitProductionItems.RESEARCH_NARROW_LAVA_RIVER, Keybindings.keyC);
	}
}
