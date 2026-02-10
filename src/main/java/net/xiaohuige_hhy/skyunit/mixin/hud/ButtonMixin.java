package net.xiaohuige_hhy.skyunit.mixin.hud;

import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.SkyUnitConfigs;
import net.xiaohuige_hhy.skyunit.unit.interfaces.SkyUnitUnit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Mixin(Button.class)
public class ButtonMixin {
	
	@Unique
	private static final String[] skyUnit$startPosButtons = {"Villagers", "Monsters", "Piglins"};
	
	@Shadow
	public String name;
	
	@Shadow
	public Supplier<Boolean> isSelected;
	
	@Shadow
	public ResourceLocation iconResource;
	
	@Inject(method = "checkClicked", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V", ordinal = 0), remap = false)
	private void updateSkyUnitSelection(int mouseX, int mouseY, boolean leftClick, CallbackInfo ci) {
		if (Arrays.asList(skyUnit$startPosButtons).contains(name)) {
			SkyUnitConfigs.selectedSkyUnitFaction = false;
		}
	}
	
	@Inject(method = "<init>(Ljava/lang/String;ILnet/minecraft/resources/ResourceLocation;Lcom/solegendary/reignofnether/keybinds/Keybinding;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/lang/Runnable;Ljava/lang/Runnable;Ljava/util/List;)V", at = @At(value = "TAIL"), remap = false)
	private void updateStartPosSelection(String name, int iconSize, ResourceLocation iconRl, Keybinding hotkey, Supplier<Boolean> isSelected, Supplier<Boolean> isHidden, Supplier<Boolean> isEnabled, Runnable onLeftClick, Runnable onRightClick, List<FormattedCharSequence> tooltipLines, CallbackInfo ci) {
		if (Arrays.asList(skyUnit$startPosButtons).contains(name)) {
			((Button) (Object) this).isSelected = () -> isSelected.get() && !SkyUnitConfigs.selectedSkyUnitFaction;
		}
	}
	
	@Inject(method = "<init>(Ljava/lang/String;ILnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/lang/Runnable;Ljava/lang/Runnable;Ljava/util/List;)V", at = @At(value = "TAIL"), remap = false)
	private void setSkyUnit(String name, int iconSize, ResourceLocation iconRl, LivingEntity entity, Supplier<Boolean> isSelected, Supplier<Boolean> isHidden, Supplier<Boolean> isEnabled, Runnable onLeftClick, Runnable onRightClick, List<FormattedCharSequence> tooltipLines, CallbackInfo ci) {
		if (entity instanceof SkyUnitUnit) {
			this.iconResource = ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, iconRl.getPath());
		}
	}
	
}
