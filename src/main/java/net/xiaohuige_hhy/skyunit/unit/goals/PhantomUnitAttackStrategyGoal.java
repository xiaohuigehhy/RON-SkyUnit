package net.xiaohuige_hhy.skyunit.unit.goals;

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
//		this.phantom.anchorPoint = this.phantom.getMoveGoal().getMoveTarget().above(5 + (this.target != null ? this.random.nextInt(5) : 0));
		this.phantom.attackPhase = PhantomUnit.AttackPhase.CIRCLE;
		this.count = 4;
	}
	
	public void stop() {
		if (this.target != null)
			this.phantom.anchorPoint = this.phantom.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, this.phantom.anchorPoint).above(10 + this.random.nextInt(20));
	}
	
	public void tick() {
		if (this.target == null && this.phantom.attackPhase == PhantomUnit.AttackPhase.SWOOP) {
			this.phantom.anchorPoint = this.phantom.getMoveGoal().getMoveTarget().above(2);
			this.phantom.moveTargetPoint = this.phantom.anchorPoint.getCenter();
		} else if (this.phantom.attackPhase == PhantomUnit.AttackPhase.CIRCLE) {
			if (this.target != null)
				this.phantom.anchorPoint = this.target.blockPosition().above(10);
			else
				this.phantom.anchorPoint = this.phantom.getMoveGoal().getMoveTarget().above(50);
			--this.nextSweepTick;
			if (this.nextSweepTick <= 0) {
				this.nextSweepTick = this.adjustedTickDelay((8 + this.random.nextInt(4)) * 20);
				this.phantom.anchorPoint = this.phantom.anchorPoint.above(this.random.nextInt(5));
				if (this.phantom.getTarget() == null && this.phantom.getMoveGoal().getMoveTarget() == null)
					return;
				if (this.count < 4)
					this.nextSweepTick = this.adjustedTickDelay(2 * 20);
				this.phantom.attackPhase = PhantomUnit.AttackPhase.SWOOP;
				this.count = (this.count + 1) % 5;
			}
		}
		
	}
	
}
