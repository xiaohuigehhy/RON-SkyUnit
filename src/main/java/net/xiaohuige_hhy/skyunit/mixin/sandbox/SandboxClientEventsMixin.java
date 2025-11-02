package net.xiaohuige_hhy.skyunit.mixin.sandbox;

import com.solegendary.reignofnether.building.production.ProductionItems;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.sandbox.SandboxClientEvents;
import com.solegendary.reignofnether.util.Faction;

import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SandboxClientEvents.class)
public class SandboxClientEventsMixin {
	
	@Shadow
	private static Faction faction;
	
	@Inject(method = "getUnitButtons", at = @At("HEAD"), remap = false, cancellable = true)
	private static void getSkyUnitUnitButtons(CallbackInfoReturnable<List<AbilityButton>> cir) {
		
		if (faction == Faction.VILLAGERS) {
			cir.setReturnValue(List.of(
					ProductionItems.VILLAGER.getPlaceButton(),
					ProductionItems.VINDICATOR.getPlaceButton(),
					ProductionItems.PILLAGER.getPlaceButton(),
					ProductionItems.IRON_GOLEM.getPlaceButton(),
					ProductionItems.WITCH.getPlaceButton(),
					ProductionItems.EVOKER.getPlaceButton(),
					ProductionItems.RAVAGER.getPlaceButton(),
					SkyUnitProductionItems.ILLUSIONER.getPlaceButton(),
					ProductionItems.ROYAL_GUARD.getPlaceButton()
				)
			);
			cir.cancel();
		}
	}
	
}
