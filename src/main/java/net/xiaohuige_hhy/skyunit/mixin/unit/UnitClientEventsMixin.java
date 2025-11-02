package net.xiaohuige_hhy.skyunit.mixin.unit;

import com.solegendary.reignofnether.unit.Relationship;
import com.solegendary.reignofnether.unit.UnitClientEvents;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Mixin(UnitClientEvents.class)
public abstract class UnitClientEventsMixin {
	
	
	@Shadow
	@Final
	private static ArrayList<LivingEntity> allUnits;
	
	@Redirect(
		method = "onRenderLevel",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Set;addAll(Ljava/util/Collection;)Z",
			ordinal = 0
		),
		remap = false
	)
	private static boolean filterInvisibleEntitiesWhenAdding(Set<Entity> targetSet, Collection<? extends Entity> collection) {
		for (Entity entity : collection) {
			if (!entity.isInvisible() || (entity instanceof LivingEntity livingEntity && ((UnitClientEvents.getPlayerToEntityRelationship(livingEntity) == Relationship.OWNED) || UnitClientEvents.getPlayerToEntityRelationship(livingEntity) == Relationship.FRIENDLY))) {
				targetSet.add(entity);
			}
		}
		return !collection.isEmpty();
	}
	
	@Redirect(
		method = "onRenderLevel",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Set;addAll(Ljava/util/Collection;)Z",
			ordinal = 1
		),
		remap = false
	)
	private static boolean filterInvisibleEntitiesWhenAddingSecond(Set<Entity> targetSet, Collection<? extends Entity> collection) {
		return filterInvisibleEntitiesWhenAdding(targetSet, collection);
	}
	
	@Inject(method = "onRenderLevel", at = @At("HEAD"), remap = false)
	private static void deleteInvisibleEntity(RenderLevelStageEvent evt, CallbackInfo ci) {
		allUnits.removeIf(entity -> entity.isInvisible() &&
			(entity instanceof LivingEntity &&
				(UnitClientEvents.getPlayerToEntityRelationship(entity) == Relationship.NEUTRAL ||
					UnitClientEvents.getPlayerToEntityRelationship(entity) == Relationship.HOSTILE)));
		
		
	}
}




