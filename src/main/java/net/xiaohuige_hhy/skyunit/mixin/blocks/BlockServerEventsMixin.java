package net.xiaohuige_hhy.skyunit.mixin.blocks;

import com.solegendary.reignofnether.blocks.BlockServerEvents;

import net.minecraftforge.event.TickEvent;
import net.xiaohuige_hhy.skyunit.nether.NetherLavaRiverServerEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockServerEvents.class)
public class BlockServerEventsMixin {

	@Inject(method = "onWorldTick", at = @At("TAIL"), remap = false)
	private static void netherLavaRiver(TickEvent.LevelTickEvent evt, CallbackInfo ci) {
		if (!evt.level.isClientSide) NetherLavaRiverServerEvent.setNetherLavaRiverSources();
	}

}
