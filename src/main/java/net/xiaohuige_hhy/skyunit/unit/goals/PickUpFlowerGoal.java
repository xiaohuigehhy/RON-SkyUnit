package net.xiaohuige_hhy.skyunit.unit.goals;

import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;

import net.minecraft.world.entity.animal.Bee;

public class PickUpFlowerGoal extends MoveToTargetBlockGoal {
	
	private final Bee bee;
	
	public PickUpFlowerGoal(Bee bee, boolean persistent, int reachRange) {
		super(bee, persistent, reachRange);
		this.bee = bee;
	}
	
	// 可以添加蜜蜂特有的行为
	@Override
	public void start() {
		super.start();
		
		// 蜜蜂开始移动时的特殊行为
		if (moveTarget != null) {
			System.out.println("蜜蜂开始向 " + moveTarget + " 移动");
			
			// 可以让蜜蜂发出声音或粒子效果
			// bee.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
			//     bee.getX(), bee.getY() + 0.5, bee.getZ(), 0, 0, 0);
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		
		// 蜜蜂停止移动时的特殊行为
		System.out.println("蜜蜂停止移动");
	}
	
	// 覆盖父类方法，为蜜蜂添加特殊逻辑
	@Override
	public boolean canUse() {
		// 只有在蜜蜂不生气且没有授粉时才使用移动Goal
		if (bee.isAngry()) {
			return false;
		}
		return super.canUse(); // 调用父类的逻辑
	}
	
	// 检查是否可以继续移动
	@Override
	public boolean canContinueToUse() {
		// 如果蜜蜂生气了，停止移动
		if (bee.isAngry()) {
			return false;
		}
		return super.canContinueToUse();
	}
	
}
