package net.xiaohuige_hhy.skyunit.mixin.world.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	
	public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Shadow
	public abstract boolean hasEffect(MobEffect pEffect);
	
	@Shadow
	public abstract MobType getMobType();
	
	@Inject(method = "getLastDamageSource", at = @At(value = "RETURN"), cancellable = true)
	private void isCastingClientSide(CallbackInfoReturnable<DamageSource> cir) {
		if (cir.getReturnValue() != null) {
			Entity entity = cir.getReturnValue().getEntity();
			if (entity != null) {
				cir.setReturnValue((entity.isInvisible() || (this.hasEffect(MobEffects.BLINDNESS) && this.position().distanceTo(entity.position()) >= 5)) ? null : cir.getReturnValue());
			}
			
		}
	}
	
	
}
