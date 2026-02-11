package net.xiaohuige_hhy.skyunit.unit.interfaces;

import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.resources.ResourceName;
import com.solegendary.reignofnether.resources.ResourceSource;
import com.solegendary.reignofnether.resources.ResourceSources;
import com.solegendary.reignofnether.unit.Relationship;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.goals.GatherResourcesGoal;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.interfaces.WorkerUnit;
import com.solegendary.reignofnether.unit.packets.UnitAnimationClientboundPacket;
import com.solegendary.reignofnether.unit.packets.UnitSyncClientboundPacket;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface SkyUnitUnit extends Unit {
	
	@Override
	default Faction getFaction() {
		return Faction.NONE;
	}
	static void checkAndPickupResources(Unit unit) {
		Mob unitMob = (Mob) unit;
		if (unitMob.canPickUpLoot()) {
			for (ItemEntity itementity : unitMob.level().getEntitiesOfClass(ItemEntity.class, unitMob.getBoundingBox().inflate(1, 1, 1))) {
				if (!itementity.isRemoved() && !itementity.getItem().isEmpty() && unitMob.isAlive()) {
					if (!Unit.atMaxResources(unit)) {
						ItemStack itemstack = itementity.getItem();
						ResourceSource resBlock = ResourceSources.getFromItem(itemstack.getItem());
						if (resBlock != null) {
							while (!Unit.atMaxResources(unit) && itemstack.getCount() > 0) {
								unitMob.onItemPickup(itementity);
								unitMob.take(itementity, 1);
								unit.getItems().add(new ItemStack(itemstack.getItem(), 1));
								itemstack.setCount(itemstack.getCount() - 1);
							}
							if (itemstack.getCount() <= 0)
								itementity.discard();
							
							UnitSyncClientboundPacket.sendSyncResourcesPacket(unit);
						}
						if (Unit.atThresholdResources(unit) && unit instanceof WorkerUnit workerUnit) {
							GatherResourcesGoal goal = workerUnit.getGatherResourceGoal();
							if (goal != null && goal.getTargetResourceName() != ResourceName.NONE)
								goal.saveAndReturnResources();
						}
					}
				}
			}
		}
	}
	
	static void checkAndPickupEdibleFood(Unit unit) {
		Mob unitMob = (Mob) unit;
		if (!unit.isHoldingEdibleFood()) {
			for (ItemEntity itementity : unitMob.level().getEntitiesOfClass(ItemEntity.class, unitMob.getBoundingBox().inflate(1, 1, 1))) {
				
				ItemStack itemstack = itementity.getItem();
				if (itemstack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
					if (unitMob.getAbsorptionAmount() > 0)
						continue;
				} else if (unitMob.getHealth() >= unitMob.getMaxHealth()) {
					continue;
				}
				Relationship rl = UnitServerEvents.getUnitToEntityRelationship(unit, itementity);
				if (!itementity.isRemoved() && !itemstack.isEmpty() && unitMob.isAlive() && !unit.getOwnerName().isEmpty() &&
					(rl != Relationship.HOSTILE || itementity.tickCount > HOSTILE_FOOD_DELAY_TICKS) && ResourceSources.isPreparedFood(itemstack.getItem())) {
					if (ResourceSources.isPreparedFood(itemstack.getItem()) &&
						(unitMob.getHealth() < unitMob.getMaxHealth() || itemstack.getItem() == Items.ENCHANTED_GOLDEN_APPLE)) {
						unitMob.onItemPickup(itementity);
						unitMob.take(itementity, 1);
						unit.getItems().add(new ItemStack(itemstack.getItem(), 1));
						UnitAnimationClientboundPacket.sendEatFoodPacket(unitMob, BuiltInRegistries.ITEM.getId(itemstack.getItem()));
						itemstack.setCount(itemstack.getCount() - 1);
						if (itemstack.getCount() <= 0)
							itementity.discard();
						break;
					}
				}
			}
		}
	}
}
