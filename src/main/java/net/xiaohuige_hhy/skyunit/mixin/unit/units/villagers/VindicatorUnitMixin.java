package net.xiaohuige_hhy.skyunit.mixin.unit.units.villagers;

import com.solegendary.reignofnether.unit.units.villagers.VindicatorUnit;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(VindicatorUnit.class)
public class VindicatorUnitMixin extends Vindicator {
	
	public VindicatorUnitMixin(EntityType<? extends Vindicator> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Inject(method = "getEnchant", at = @At("HEAD"), cancellable = true, remap = false)
	public void getEnchantType(CallbackInfoReturnable<Enchantment> cir) {
		ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
		Set<Enchantment> enchant = itemStack.getAllEnchantments().keySet();
		if (enchant.size() > 1) {
			cir.setReturnValue(Enchantments.BLAST_PROTECTION);
		} else {
			cir.setReturnValue(enchant.stream().findFirst().orElse(null));
		}
	}
	
}
