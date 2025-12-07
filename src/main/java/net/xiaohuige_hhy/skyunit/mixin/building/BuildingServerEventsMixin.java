package net.xiaohuige_hhy.skyunit.mixin.building;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.BuildingSave;
import com.solegendary.reignofnether.building.BuildingServerEvents;
import com.solegendary.reignofnether.building.NetherZoneSaveData;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.xiaohuige_hhy.skyunit.research.researchItems.ResearchMysteriousLibrary;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Objects;

@Mixin(BuildingServerEvents.class)
public class BuildingServerEventsMixin {
	
	@Shadow
	private static ServerLevel serverLevel;
	
	@Inject(method = "lambda$loadBuildingsAndNetherZones$2", at = @At(value = "INVOKE", target = "Lcom/solegendary/reignofnether/building/BuildingPlacement;changeStructure(Ljava/lang/String;)V", ordinal = 2), locals = LocalCapture.CAPTURE_FAILHARD, remap = false, cancellable = true)
	private static void loadLibrary(ServerLevel level, NetherZoneSaveData netherData, ArrayList<BlockPos> placedNZs, @NotNull BuildingSave b, CallbackInfo ci, BuildingPlacement building) {
		if (b.upgradeLevel == 2) {
			building.changeStructure(ResearchMysteriousLibrary.mysteriousStructureName);
			ReignOfNether.LOGGER.info("loaded building in serverevents: " + Objects.requireNonNull(ReignOfNetherRegistries.BUILDING.getKey(b.building)) + "|" + b.originPos);
			ci.cancel();
		}
	}

//	@Inject(method = "placeBuilding", at = @At(value = "RETURN"), remap = false)
//	private static void loadNetherLavaRiver(Building building, BlockPos pos, Rotation rotation, String ownerName, int[] builderUnitIds, boolean queue, boolean isDiagonalBridge, CallbackInfoReturnable<BuildingPlacement> cir) {
//		NetherLavaRiverServerEvent.changeNetherLavaRiver(serverLevel);
//	}
	
}
