package net.xiaohuige_hhy.skyunit.mixin.building;

import com.solegendary.reignofnether.building.BuildingPlacement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BuildingPlacement.class)
public class BuildingPlacementMixin {
	@Shadow
	protected int highestBlockCountReached;
	
	@Inject(method = "getBlocksPlaced", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;stream()Ljava/util/stream/Stream;"), remap = false)
	private void getMysteriousLibrary(CallbackInfoReturnable<Integer> cir) {
		highestBlockCountReached = 2;
	}
	
	@Inject(method = "createArmourStandTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
	private void setStandData(CallbackInfo ci) {
		highestBlockCountReached = 2;
	
	}
	
}
