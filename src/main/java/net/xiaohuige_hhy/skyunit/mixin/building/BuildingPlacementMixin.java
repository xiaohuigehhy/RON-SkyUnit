package net.xiaohuige_hhy.skyunit.mixin.building;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingPlacement;

import net.xiaohuige_hhy.skyunit.ability.abilities.PromoteIllusioner;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BuildingPlacement.class)
public abstract class BuildingPlacementMixin {
	@Shadow
	protected int highestBlockCountReached;
	
	@Shadow
	private Building building;
	
	@Shadow
	public abstract int getCharges(Ability ability);
	
	@Shadow
	public abstract void refreshBlocks();
	
	@Inject(method = "getBlocksPlaced", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;stream()Ljava/util/stream/Stream;"), remap = false)
	private void getMysteriousLibrary(CallbackInfoReturnable<Integer> cir) {
		highestBlockCountReached = 2;
	}
	
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/solegendary/reignofnether/ability/Ability;usesCharges()Z"), remap = false)
	private boolean getAbility(Ability instance) {
		if (instance instanceof PromoteIllusioner)
			return false;
		return instance.usesCharges();
	}
	
	
}
