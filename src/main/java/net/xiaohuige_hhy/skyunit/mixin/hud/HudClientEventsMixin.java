package net.xiaohuige_hhy.skyunit.mixin.hud;

import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.hud.buttons.StartButtons;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenEvent;
import net.xiaohuige_hhy.skyunit.SkyUnitConfigs;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(HudClientEvents.class)
public class HudClientEventsMixin {
	
	@Shadow
	public static int mouseX;
	
	@Shadow
	public static int mouseY;
	
	@Shadow
	@Final
	private static ArrayList<Button> renderedButtons;
	
	@Shadow @Final private static Minecraft MC;
	
	@Inject(method = "onDrawScreen", at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;", ordinal = 26), remap = false)
	private static void drawStartButton(ScreenEvent.Render.Post evt, CallbackInfo ci) {
		int screenWidth = MC.getWindow().getGuiScaledWidth();
		if (!SkyUnitConfigs.skyUnitStartButton.isHidden.get()) {
			SkyUnitConfigs.skyUnitStartButton.render(evt.getGuiGraphics(),
				screenWidth - (StartButtons.ICON_SIZE * 2),
				40,
				mouseX,
				mouseY
			);
			renderedButtons.add(SkyUnitConfigs.skyUnitStartButton);
		}
	}
	
	
	@Inject(method = "onDrawScreen", at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;", ordinal = 29), remap = false)
	private static void drawReadyButton(ScreenEvent.Render.Post evt, CallbackInfo ci) {
		int screenWidth = MC.getWindow().getGuiScaledWidth();
		
		if (!SkyUnitConfigs.skyUnitReadyButton.isHidden.get()) {
			SkyUnitConfigs.skyUnitReadyButton.render(evt.getGuiGraphics(),
				screenWidth - (StartButtons.ICON_SIZE * 2),
				40,
				mouseX,
				mouseY
			);
			renderedButtons.add(SkyUnitConfigs.skyUnitReadyButton);
		}
	}
}
