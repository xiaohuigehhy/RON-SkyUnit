package net.xiaohuige_hhy.skyunit.mixin.util;

import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.util.MiscUtil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiscUtil.class)
public class MiscUtilMixin {
	
	@Inject(method = "getSimpleEntityName", at = @At("RETURN"), cancellable = true, remap = false)
	private static void getSkeletonHorseName(Entity entity, CallbackInfoReturnable<String> cir) {
		if (cir.getReturnValue() != null && entity instanceof Unit) {
			cir.setReturnValue(cir.getReturnValue().replace(
				"entity.skyunit.", ""
			));
		}
	}
	
	@Inject(method = "findClosestAttackableEntity", at = @At("RETURN"), remap = false, cancellable = true)
	private static void findClosestAttackableEntityButCheckInvisible(Mob unitMob, float range, ServerLevel level, CallbackInfoReturnable<LivingEntity> cir) {
		cir.setReturnValue((cir.getReturnValue() != null && cir.getReturnValue().isInvisible()) ? null : cir.getReturnValue());
	}
	
}
