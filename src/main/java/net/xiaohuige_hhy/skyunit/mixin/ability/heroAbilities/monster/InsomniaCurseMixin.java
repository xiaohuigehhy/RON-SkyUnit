package net.xiaohuige_hhy.skyunit.mixin.ability.heroAbilities.monster;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.ability.heroAbilities.monster.InsomniaCurse;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.NecromancerUnit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.SkyUnit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InsomniaCurse.class)
public class InsomniaCurseMixin extends HeroAbility {
	protected InsomniaCurseMixin(HeroUnit hero) {
		super(hero, 3, 30, UnitAction.INSOMNIA_CURSE, 30 * ResourceCost.TICKS_PER_SECOND, 12, 0, true);
		maxCharges = 1;
		charges = maxCharges;
	}
	
	@Inject(method = "updateStatsForRank", at = @At("HEAD"), cancellable = true, remap = false)
	public void updateCooldownForRank(CallbackInfo ci) {
		if (rank == 1) {
			cooldownMax = 30 * ResourceCost.TICKS_PER_SECOND;
		} else if (rank == 2) {
			cooldownMax = 25 * ResourceCost.TICKS_PER_SECOND;
		} else if (rank == 3) {
			cooldownMax= 20 * ResourceCost.TICKS_PER_SECOND;
		}
		maxCharges = 1;
		ci.cancel();
	}
	
	@Inject(method = "getButton", at = @At("HEAD"), cancellable = true, remap = false)
	public void getSkeletonHorseButton(Keybinding hotkey, CallbackInfoReturnable<AbilityButton> cir) {
		cir.setReturnValue(
			new AbilityButton("Curse of Insomnia",
				new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/skeleton_horse_summon.png"),
				hotkey,
				() -> CursorClientEvents.getLeftClickAction() == UnitAction.INSOMNIA_CURSE,
				() -> rank == 0,
				() -> true,
				() -> CursorClientEvents.setLeftClickAction(UnitAction.INSOMNIA_CURSE),
				null,
				getTooltipLines(),
				this
			));
		cir.cancel();
	}
	
	@Inject(method = "getRankUpButton", at = @At("HEAD"), cancellable = true, remap = false)
	public void getRankUpButton(CallbackInfoReturnable<Button> cir) {
		cir.setReturnValue(super.getRankUpButtonProtected(
			"Curse of Insomnia",
			new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/skeleton_horse_summon.png")
		));
		cir.cancel();
	}
	
	@Inject(method = "use(Lnet/minecraft/world/level/Level;Lcom/solegendary/reignofnether/unit/interfaces/Unit;Lnet/minecraft/core/BlockPos;)V", at = @At("HEAD"), cancellable = true, remap = false)
	public void useGround(Level level, Unit unitUsing, BlockPos targetBp, CallbackInfo ci) {
		((NecromancerUnit) unitUsing).getCastPhantomGoal().setAbility(this);
		((NecromancerUnit) unitUsing).getCastPhantomGoal().setTarget(targetBp);
		ci.cancel();
	}
	
}
