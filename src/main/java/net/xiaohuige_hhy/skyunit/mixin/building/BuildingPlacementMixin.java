package net.xiaohuige_hhy.skyunit.mixin.building;

import com.solegendary.reignofnether.building.BuildingPlacement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BuildingPlacement.class)
public class BuildingPlacementMixin {
	@Shadow
	protected int highestBlockCountReached;
	
	@Inject(method = "getBlocksPlaced", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;stream()Ljava/util/stream/Stream;"), remap = false)
	private void getMysteriousLibrary(CallbackInfoReturnable<Integer> cir) {
		highestBlockCountReached = 2;
	}
	
	
}
