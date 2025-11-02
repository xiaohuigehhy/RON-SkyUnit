package net.xiaohuige_hhy.skyunit.mixin.healthbars;

import com.solegendary.reignofnether.healthbars.HealthBarClientEvents;
import com.solegendary.reignofnether.unit.Relationship;
import com.solegendary.reignofnether.unit.UnitClientEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HealthBarClientEvents.class)
public class HealthBarClientEventsMixin {
	
	@Inject(method = "shouldShowHealthBar", at = @At("RETURN"), remap = false, cancellable = true)
	private static void shouldShowInvisibleHealthBar(Entity entity, Minecraft client, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(cir.getReturnValue() && !(entity.isInvisible() && entity instanceof LivingEntity livingEntity && (UnitClientEvents.getPlayerToEntityRelationship(livingEntity) == Relationship.HOSTILE || UnitClientEvents.getPlayerToEntityRelationship(livingEntity) == Relationship.NEUTRAL)));
	}
	
}
