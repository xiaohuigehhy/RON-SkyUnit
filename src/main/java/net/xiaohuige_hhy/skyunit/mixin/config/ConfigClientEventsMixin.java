package net.xiaohuige_hhy.skyunit.mixin.config;

import com.solegendary.reignofnether.config.ConfigClientEvents;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfigClientEvents.class)
public class ConfigClientEventsMixin {

    @Inject(method = "onPlayerJoin", at = @At("TAIL"), remap = false)
	private static void DoSkyUnitOnPlayerJoin(PlayerEvent.PlayerLoggedInEvent evt, CallbackInfo ci) {
	    SkyUnitResourceCosts.deferredLoadResourceCosts();
    }

}
