package net.xiaohuige_hhy.skyunit.mixin.sandbox;

import static com.solegendary.reignofnether.player.PlayerServerEvents.serverLevel;

import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.sandbox.SandboxServer;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitEntityRegistrar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SandboxServer.class)
public class SandboxServerMixin {
	
	@Inject(method = "spawnUnit", at = @At("HEAD"), remap = false, cancellable = true)
	private static void spawnSkyUnitUnit(String playerName, String unitName, BlockPos blockPos, CallbackInfo ci) {
		if (serverLevel == null)
			return;
		
		EntityType<? extends Mob> entityType = EntityRegistrar.getEntityType(unitName);
		entityType = entityType != null ? entityType : SkyUnitEntityRegistrar.getEntityType(unitName);
		
		if (entityType != null) {
			Entity entity = UnitServerEvents.spawnMob(entityType, serverLevel, blockPos, playerName);
			if (entity instanceof Unit unit && (playerName.isEmpty() || playerName.equals("Enemy"))) {
				unit.setAnchor(blockPos);
			}
		}
		ci.cancel();
	}
	
	
}
