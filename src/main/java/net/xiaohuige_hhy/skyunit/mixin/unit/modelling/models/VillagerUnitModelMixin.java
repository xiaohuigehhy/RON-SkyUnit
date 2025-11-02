package net.xiaohuige_hhy.skyunit.mixin.unit.modelling.models;

import com.solegendary.reignofnether.unit.modelling.models.VillagerUnitModel;

import net.minecraft.world.entity.Entity;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerUnit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerUnitModel.class)
public class VillagerUnitModelMixin {
	
	@Inject(method = "getArmPose", at = @At("HEAD"), remap = false, cancellable = true)
	private void getIllusionerArmPose(Entity entity, CallbackInfoReturnable<VillagerUnitModel.ArmPose> cir) {
		if (entity instanceof IllusionerUnit illusionerUnit)
			cir.setReturnValue(illusionerUnit.getIllusionerArmPose());
	}
	
}
