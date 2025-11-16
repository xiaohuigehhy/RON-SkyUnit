package net.xiaohuige_hhy.skyunit.mixin.ability.abilities;

import com.solegendary.reignofnether.ability.EnchantAbility;
import com.solegendary.reignofnether.ability.abilities.EnchantSharpness;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.unit.UnitAction;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantSharpness.class)
public class EnchantSharpnessMixin extends EnchantAbility {
	
	@Shadow @Final private static UnitAction ENCHANT_ACTION;
	
	public EnchantSharpnessMixin() {
		super(ENCHANT_ACTION, ResourceCosts.ENCHANT_SHARPNESS);
	}
	
	@Inject(method = "hasAnyEnchant", at = @At("HEAD"), remap = false, cancellable = true)
	public void DontCheckEnchant(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(hasSameEnchant(entity));
		cir.cancel();
	}
	
	@Inject(method = "doEnchant", at = @At("HEAD"), remap = false, cancellable = true)
	public void addEnchant(LivingEntity entity, CallbackInfo ci) {
		ItemStack item = entity.getItemBySlot(EquipmentSlot.MAINHAND);
		if (item != ItemStack.EMPTY) {
			item.enchant(EnchantSharpness.actualEnchantment, EnchantSharpness.enchantLevel);
		}
		ci.cancel();
	}
}
