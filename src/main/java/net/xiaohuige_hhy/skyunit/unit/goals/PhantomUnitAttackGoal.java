package net.xiaohuige_hhy.skyunit.unit.goals;

import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.PhantomUnit;

import java.util.EnumSet;

public class PhantomUnitAttackGoal extends MoveToTargetBlockGoal {
	private final PhantomUnit phantom;
	
	public PhantomUnitAttackGoal(Mob mob) {
		super(mob, false, 0);
		phantom = (PhantomUnit) mob;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}
	
	
	public boolean canUse() {
		return (this.phantom.getTarget() != null && this.phantom.attackPhase == PhantomUnit.AttackPhase.SWOOP);
	}
	
	public void start() {
		phantom.attackPhase = PhantomUnit.AttackPhase.SWOOP;
	}
	
	public boolean canContinueToUse() {
		LivingEntity livingentity = this.phantom.getTarget();
		if (livingentity == null) {
			return false;
		} else if (!livingentity.isAlive()) {
			return false;
		} else {
			if (livingentity instanceof Player player) {
				if (livingentity.isSpectator() || player.isCreative()) {
					return false;
				}
			}
			
			return this.canUse();
		}
	}
	
	public void stop() {
		this.phantom.attackPhase = PhantomUnit.AttackPhase.CIRCLE;
	}
	
	public void tick() {
		LivingEntity livingentity = this.phantom.getTarget();
		if (livingentity != null) {
			this.phantom.moveTargetPoint = new Vec3(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
			if (this.phantom.getBoundingBox().inflate(0.2F).intersects(livingentity.getBoundingBox())) {
				this.phantom.doHurtTarget(livingentity);
				this.phantom.attackPhase = PhantomUnit.AttackPhase.CIRCLE;
			} else if (this.phantom.horizontalCollision) {
				this.phantom.attackPhase = PhantomUnit.AttackPhase.CIRCLE;
			}
			
		}
	}
	
}