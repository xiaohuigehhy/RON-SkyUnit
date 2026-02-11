package net.xiaohuige_hhy.skyunit.unit.controls;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.PhantomUnit;

public class PhantomUnitMoveControl extends MoveControl {
	private final PhantomUnit phantom;
	public float speed = 0.1F;
	
	public PhantomUnitMoveControl(PhantomUnit pMob) {
		super(pMob);
		this.phantom = pMob;
	}
	
	protected boolean touchingTarget() {
		return this.phantom.moveTargetPoint.distanceToSqr(this.phantom.getX(), this.phantom.getY(), this.phantom.getZ()) < 1.0D;
	}
	
	public void tick() {
		if (this.phantom.getFollowTarget() != null || this.phantom.hasLivingTarget() || (this.phantom.getMoveGoal().getMoveTarget() != null && !touchingTarget())) {
			phantomMoveTarget();
		} else {
			this.mob.setYya(0.0F);
			this.mob.setZza(0.0F);
			this.phantom.setMovementSpeed(0.0F);
		}
		
	}
	
	private void phantomMoveTarget() {
		if (this.phantom.horizontalCollision) {
			this.phantom.setYRot(this.phantom.getYRot() + 180.0F);
			this.speed = 0.1F;
			this.phantom.attackPhase = PhantomUnit.AttackPhase.CIRCLE;
		}
		double d0 = this.phantom.moveTargetPoint.x - this.phantom.getX();
		double d1 = this.phantom.moveTargetPoint.y - this.phantom.getY();
		double d2 = this.phantom.moveTargetPoint.z - this.phantom.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		if (Math.abs(d3) > (double) 1.0E-5F) {
			double d4 = 1.0D - Math.abs(d1 * (double) 0.7F) / d3;
			d0 *= d4;
			d2 *= d4;
			d3 = Math.sqrt(d0 * d0 + d2 * d2);
			double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
			float f = this.phantom.getYRot();
			float f1 = (float) Mth.atan2(d2, d0);
			float f2 = Mth.wrapDegrees(this.phantom.getYRot() + 90.0F);
			float f3 = Mth.wrapDegrees(f1 * (180F / (float) Math.PI));
			this.phantom.setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
			this.phantom.yBodyRot = this.phantom.getYRot();
			if (Mth.degreesDifferenceAbs(f, this.phantom.getYRot()) < 3.0F) {
				speed = Mth.approach(speed, 1.8F, 0.005F * (1.8F / speed));
			} else {
				speed = Mth.approach(speed, 0.2F, 0.025F);
			}
			
			float f4 = (float) (-(Mth.atan2(-d1, d3) * (double) (180F / (float) Math.PI)));
			this.phantom.setXRot(f4);
			float f5 = this.phantom.getYRot() + 90.0F;
			if (this.phantom.getTarget() == null || !this.phantom.blockPosition().closerThan(this.phantom.getTarget().blockPosition(), 50D)) {
				AttributeInstance ai = mob.getAttribute(Attributes.FLYING_SPEED);
				if (ai != null) {
					speed = (float) Math.min(speed, ai.getValue());
				}
			}
			this.phantom.setMovementSpeed(this.speed);
			double d6 = (double) (speed * Mth.cos(f5 * ((float) Math.PI / 180F))) * Math.abs(d0 / d5);
			double d7 = (double) (speed * Mth.sin(f5 * ((float) Math.PI / 180F))) * Math.abs(d2 / d5);
			double d8 = (double) (speed * Mth.sin(f4 * ((float) Math.PI / 180F))) * Math.abs(d1 / d5);
			Vec3 vec3 = this.phantom.getDeltaMovement();
			this.phantom.setDeltaMovement(vec3.add((new Vec3(d6, d8, d7)).subtract(vec3).scale(0.2D)));
		}
	}
}