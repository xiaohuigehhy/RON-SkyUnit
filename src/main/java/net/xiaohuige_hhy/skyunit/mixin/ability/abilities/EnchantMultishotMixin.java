package net.xiaohuige_hhy.skyunit.mixin.ability.abilities;

import com.solegendary.reignofnether.ability.EnchantAbility;
import com.solegendary.reignofnether.ability.abilities.EnchantMultishot;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.unit.UnitAction;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantMultishot.class)
public class EnchantMultishotMixin extends EnchantAbility {
	
	@Shadow
	@Final
	private static UnitAction ENCHANT_ACTION;
	
	public EnchantMultishotMixin() {
		super(ENCHANT_ACTION, ResourceCosts.ENCHANT_MULTISHOT);
	}
	
	@Inject(method = "hasAnyEnchant", at = @At("HEAD"), remap = false, cancellable = true)
	public void DontCheckEnchant(LivingEntity entity, @NotNull CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(hasSameEnchant(entity));
		cir.cancel();
	}
	
	@Inject(method = "doEnchant", at = @At("HEAD"), remap = false, cancellable = true)
	public void addEnchant(@NotNull LivingEntity entity, CallbackInfo ci) {
		ItemStack item = entity.getItemBySlot(EquipmentSlot.MAINHAND);
		if (item != ItemStack.EMPTY) {
			item.enchant(EnchantMultishot.actualEnchantment, EnchantMultishot.enchantLevel);
		}
		ci.cancel();
	}
}
