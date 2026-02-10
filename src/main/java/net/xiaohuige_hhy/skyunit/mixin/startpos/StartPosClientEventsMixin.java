package net.xiaohuige_hhy.skyunit.mixin.startpos;

import static com.solegendary.reignofnether.startpos.StartPosClientEvents.getPos;

import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.startpos.StartPosClientEvents;

import net.minecraft.client.Minecraft;
import net.xiaohuige_hhy.skyunit.SkyUnitConfigs;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StartPosClientEvents.class)
public class StartPosClientEventsMixin {
	
	
	@Shadow @Final private static Minecraft MC;
	
	
	@Inject(method = "isSelectedPosReservedByOther", at = @At("RETURN"), remap = false, cancellable = true)
	private static void isSelectedPosReservedByOther(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(getPos() != null &&
			!getPos().playerName.isEmpty() &&
			MC.player != null &&
			!getPos().playerName.equals(MC.player.getName().getString()) &&
			getPos().faction != Faction.NONE &&
			!SkyUnitConfigs.selectedSkyUnitFaction);
	}
	
}
