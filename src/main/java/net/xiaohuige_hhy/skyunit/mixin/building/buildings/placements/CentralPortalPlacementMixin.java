package net.xiaohuige_hhy.skyunit.mixin.building.buildings.placements;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingBlock;
import com.solegendary.reignofnether.building.NetherConvertingBuilding;
import com.solegendary.reignofnether.building.NetherZone;
import com.solegendary.reignofnether.building.buildings.placements.CentralPortalPlacement;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.xiaohuige_hhy.skyunit.nether.NetherLavaRiverServerEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(CentralPortalPlacement.class)
public abstract class CentralPortalPlacementMixin extends ProductionPlacement implements NetherConvertingBuilding {
	public CentralPortalPlacementMixin(Building building, Level level, BlockPos originPos, Rotation rotation, String ownerName, ArrayList<BuildingBlock> blocks, boolean isCapitol) {
		super(building, level, originPos, rotation, ownerName, blocks, isCapitol);
	}
	
	@Inject(method = "setNetherZone", at = @At("RETURN"), remap = false)
	private void reload(NetherZone nz, CallbackInfo ci) {
		NetherLavaRiverServerEvent.changeNetherLavaRiver(level);
	}
	
//	@Inject(method = "tick", at = @At("RETURN"), remap = false)
//	private void reloadPerTick(Level tickLevel, CallbackInfo ci) {
////		if (tickAgeAfterBuilt > 0 && tickAgeAfterBuilt % 20 == 0)
////			NetherLavaRiverServerEvent.changeNetherLavaRiver(level);
//	}
	
}
