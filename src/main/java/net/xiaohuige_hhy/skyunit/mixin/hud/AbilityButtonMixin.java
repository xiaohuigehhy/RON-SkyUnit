package net.xiaohuige_hhy.skyunit.mixin.hud;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(AbilityButton.class)
public class AbilityButtonMixin extends Button {
	public AbilityButtonMixin(String name, int iconSize, ResourceLocation iconRl, @Nullable Keybinding hotkey, Supplier<Boolean> isSelected, Supplier<Boolean> isHidden, Supplier<Boolean> isEnabled, @Nullable Runnable onLeftClick, @Nullable Runnable onRightClick, @Nullable List<FormattedCharSequence> tooltipLines) {
		super(name, iconSize, iconRl, hotkey, isSelected, isHidden, isEnabled, onLeftClick, onRightClick, tooltipLines);
	}
	
	
	@Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/resources/ResourceLocation;Lcom/solegendary/reignofnether/keybinds/Keybinding;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/util/function/Supplier;Ljava/lang/Runnable;Ljava/lang/Runnable;Ljava/util/List;Lcom/solegendary/reignofnether/ability/Ability;Lcom/solegendary/reignofnether/building/BuildingPlacement;)V", at = @At("TAIL"), remap = false)
	private void setBuilding(String name, ResourceLocation rl, Keybinding hotkey, Supplier<Boolean> isSelected, Supplier<Boolean> isHidden, Supplier<Boolean> isEnabled, Runnable onLeftClick, Runnable onRightClick, List<FormattedCharSequence> tooltipLines, @javax.annotation.Nullable Ability ability, BuildingPlacement placement, CallbackInfo ci) {
		building = placement;
	}
}
