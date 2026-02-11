package net.xiaohuige_hhy.skyunit.unit.goals;

import com.solegendary.reignofnether.registrars.GameRuleRegistrar;
import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.util.MiscUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.PhantomUnit;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class PhantomUnitFollowGoal extends MoveToTargetBlockGoal {
	private final PhantomUnit phantom;
	private final RandomSource random;
	private float angle;
	private float distance;
	private float height;
	private float clockwise;
	
	public PhantomUnitFollowGoal(Mob mob) {
		super(mob, false, 0);
		phantom = (PhantomUnit) mob;
		random = mob.getRandom();
		this.setFlags(EnumSet.of(Flag.MOVE));
	}
	
	@Override
	public void setMoveTarget(@Nullable BlockPos bp) {
		if (bp != null) {
			MiscUtil.addUnitCheckpoint((Unit) mob, bp, true);
		}
		this.moveTarget = bp;
	}
	
	public boolean canUse() {
		return (this.phantom.getTarget() != null || this.phantom.getFollowTarget() != null || this.phantom.getMoveGoal().getMoveTarget() != null) && this.phantom.attackPhase == PhantomUnit.AttackPhase.CIRCLE;
	}
	
	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}
	
	public void start() {
		phantom.attackPhase = PhantomUnit.AttackPhase.CIRCLE;
		this.distance = 5.0F + this.random.nextFloat() * 10.0F;
		this.height = -4.0F + this.random.nextFloat() * 9.0F;
		this.clockwise = this.random.nextBoolean() ? 1.0F : -1.0F;
		this.selectNext();
	}
	
	public void tick() {
		if (this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
			this.height = -4.0F + this.random.nextFloat() * 9.0F;
		}
		
		if (this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
			++this.distance;
			if (this.distance > 15.0F) {
				this.distance = 5.0F;
				this.clockwise = -this.clockwise;
			}
		}
		
		if (this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
			this.angle = this.random.nextFloat() * 2.0F * (float) Math.PI;
			this.selectNext();
		}
		
		if (this.touchingTarget()) {
			this.selectNext();
		}
		
		if (this.phantom.moveTargetPoint.y < this.phantom.getY() && !this.phantom.level().isEmptyBlock(this.phantom.blockPosition().below(1))) {
			this.height = Math.max(1.0F, this.height);
			this.selectNext();
		}
		
		if (this.phantom.moveTargetPoint.y > this.phantom.getY() && !this.phantom.level().isEmptyBlock(this.phantom.blockPosition().above(1))) {
			this.height = Math.min(-1.0F, this.height);
			this.selectNext();
		}
		
	}
	
	protected boolean touchingTarget() {
		return this.phantom.moveTargetPoint.distanceToSqr(this.phantom.getX(), this.phantom.getY(), this.phantom.getZ()) < 4.0D;
	}
	
	private void selectNext() {
		if (BlockPos.ZERO.equals(this.phantom.anchorPoint)) {
			this.phantom.anchorPoint = this.phantom.blockPosition();
		}
		
		this.angle += this.clockwise * 15.0F * ((float) Math.PI / 180F);
		this.phantom.moveTargetPoint = Vec3.atLowerCornerOf(this.phantom.anchorPoint).add(this.distance * Mth.cos(this.angle), -4.0F + this.height, this.distance * Mth.sin(this.angle));
		double maxHeight = this.mob.level().getGameRules().getRule(GameRuleRegistrar.FLYING_MAX_Y_LEVEL).get();
		if (this.phantom.moveTargetPoint.y > maxHeight)
			this.phantom.moveTargetPoint = new Vec3(0, maxHeight, 0);
	}
}