package net.xiaohuige_hhy.skyunit.mixin.unit.units.monsters;

import com.solegendary.reignofnether.unit.goals.UnitBowAttackGoal;
import com.solegendary.reignofnether.unit.units.monsters.SkeletonUnit;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkeletonUnit.class)
public abstract class SkeletonUnitMixin extends Skeleton {
	
	@Shadow(remap = false)
	@Final
	public static float attackDamage;
	@Unique
	private float skyUnit$attackKnockBack = 0;
	@Shadow
	private UnitBowAttackGoal<? extends LivingEntity> attackGoal;
	
	public SkeletonUnitMixin(EntityType<? extends Skeleton> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Shadow
	public abstract Goal getAttackGoal();
	
	@Inject(method = "getUnitAttackDamage", at = @At("HEAD"), cancellable = true, remap = false)
	public void getUnitArrowDamage(CallbackInfoReturnable<Float> cir) {
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, this);
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, this);
		cir.setReturnValue(attackDamage);
		if (i > 0) {
			cir.setReturnValue((float) (attackDamage + (double) i * 0.5D + 0.5D));
		}
		if (j > 0) {
			AttributeInstance ai = this.getAttribute(Attributes.ATTACK_KNOCKBACK);
			if (ai != null) {
				ai.setBaseValue(skyUnit$attackKnockBack);
			}
		}
		
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void addTick(CallbackInfo ci) {
		if (!this.isPassenger() && this.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.IRON_HELMET && this.hurtTime > 0) {
			this.broadcastBreakEvent(EquipmentSlot.HEAD);
			this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
		}
	}
	
	
	@Unique
	public float skyUnit$getAttackKnockBack() {
		return skyUnit$attackKnockBack;
	}
	
	@Unique
	public void skyUnit$setAttackKnockBack(float attackKnockBack) {
		this.skyUnit$attackKnockBack = attackKnockBack;
	}
}
