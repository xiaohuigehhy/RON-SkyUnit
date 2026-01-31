package net.xiaohuige_hhy.skyunit.mixin.player;

import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.player.PlayerServerEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.RegistryObject;
import net.xiaohuige_hhy.skyunit.hud.buttons.SkyUnitStartButton;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitEntityRegistrar;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerServerEvents.class)
public class PlayerServerEventMixin {
	
	@Redirect(method = "startRTS(ILnet/minecraft/world/phys/Vec3;Lcom/solegendary/reignofnether/faction/Faction;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/RegistryObject;get()Ljava/lang/Object;", ordinal = 0), remap = false)
	private static Object redirectVillagerUnitGet(
		RegistryObject<EntityType<? extends Unit>> originalRegistryObject,
		// 方法参数
		int playerId,
		net.minecraft.world.phys.Vec3 pos,
		com.solegendary.reignofnether.faction.Faction faction,
		int startPosColorId
	) {
		if (faction == Faction.VILLAGERS && SkyUnitStartButton.selectedSkyUnitFaction) {
			return SkyUnitEntityRegistrar.PARROT_UNIT.get();
		}
		
		return originalRegistryObject.get();
	}
	
}
