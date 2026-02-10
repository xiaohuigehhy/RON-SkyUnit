package net.xiaohuige_hhy.skyunit.unit.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.PhantomUnit;

public class PhantomUnitAttackStrategyGoal extends Goal {
	private final PhantomUnit phantom;
	private final RandomSource random;
	private LivingEntity target;
	private int nextSweepTick;
	private int count = 5;
	
	public PhantomUnitAttackStrategyGoal(Mob mob) {
		this.phantom = (PhantomUnit) mob;
		this.random = mob.getRandom();
	}
	
	public boolean canUse() {
		
		this.target = this.phantom.getTarget();
		if (this.target == null)
			this.target = this.phantom.getFollowTarget();
		return this.target != null || this.phantom.getMoveGoal().getMoveTarget() != null;
	}
	
	public void start() {
		this.nextSweepTick = this.adjustedTickDelay(10);
		this.setAnchorAboveTarget();
		this.phantom.attackPhase = PhantomUnit.AttackPhase.CIRCLE;
		this.count = 4;
	}
	
	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		if (this.target != null)
			this.phantom.anchorPoint = this.phantom.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, this.phantom.anchorPoint).above(10 + this.random.nextInt(20));
	}
	
	public void tick() {
		if (this.target == null) {
			setAnchorAboveTarget();
			this.phantom.moveTargetPoint = this.phantom.anchorPoint.getCenter();
		} else if (this.phantom.attackPhase == PhantomUnit.AttackPhase.CIRCLE) {
			
			--this.nextSweepTick;
			if (this.nextSweepTick <= 0) {
				
				this.setAnchorAboveTarget();
				this.nextSweepTick = this.adjustedTickDelay((8 + this.random.nextInt(4)) * 20);
				if (this.phantom.getTarget() == null)
					return;
				if (this.count < 4)
					this.nextSweepTick = this.adjustedTickDelay(4 * 20);
				else {
					this.count = 0;
				}
				this.phantom.attackPhase = PhantomUnit.AttackPhase.SWOOP;
				this.count++;
			}
		}
		
	}
	
	private void setAnchorAboveTarget() {
		this.phantom.anchorPoint = this.target != null ? this.target.blockPosition().above(10 + this.random.nextInt(5)) : this.phantom.getMoveGoal().getMoveTarget().above(5);
		if (this.phantom.anchorPoint.getY() < this.phantom.level().getSeaLevel()) {
			this.phantom.anchorPoint = new BlockPos(this.phantom.anchorPoint.getX(), this.phantom.level().getSeaLevel() + 1, this.phantom.anchorPoint.getZ());
		}
	}
	
}
