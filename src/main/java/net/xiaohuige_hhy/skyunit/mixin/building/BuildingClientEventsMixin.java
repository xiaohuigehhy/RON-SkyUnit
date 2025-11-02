package net.xiaohuige_hhy.skyunit.mixin.building;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.buildings.placements.PortalPlacement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.xiaohuige_hhy.skyunit.research.researchItems.ResearchMysteriousLibrary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BuildingClientEvents.class)
public class BuildingClientEventsMixin {
	
	@Inject(method = "placeBuilding", at = @At(value = "INVOKE", target = "Lcom/solegendary/reignofnether/building/BuildingPlacement;changeStructure(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
	private static void loadLibrary(Building building, BlockPos pos, Rotation rotation, String ownerName, int numBlocksToPlace, boolean isDiagonalBridge, int upgradeLevel, boolean isBuilt, PortalPlacement.PortalType portalType, BlockPos portalDestination, boolean forPlayerLoggingIn, CallbackInfo ci, BuildingPlacement newBuilding) {
		if (upgradeLevel == 2) {
			newBuilding.changeStructure(ResearchMysteriousLibrary.mysteriousStructureName);
		}
	}
	
}
