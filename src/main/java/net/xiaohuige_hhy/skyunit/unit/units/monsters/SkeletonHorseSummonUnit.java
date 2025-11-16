package net.xiaohuige_hhy.skyunit.unit.units.monsters;

import com.solegendary.reignofnether.ability.Abilities;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.Checkpoint;
import com.solegendary.reignofnether.unit.goals.AbstractMeleeAttackUnitGoal;
import com.solegendary.reignofnether.unit.goals.GarrisonGoal;
import com.solegendary.reignofnether.unit.goals.MeleeAttackBuildingGoal;
import com.solegendary.reignofnether.unit.goals.MeleeAttackUnitGoal;
import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import com.solegendary.reignofnether.unit.goals.RandomLookAroundUnitGoal;
import com.solegendary.reignofnether.unit.goals.ReturnResourcesGoal;
import com.solegendary.reignofnether.unit.goals.SelectedTargetGoal;
import com.solegendary.reignofnether.unit.goals.UsePortalGoal;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.util.Faction;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class SkeletonHorseSummonUnit extends SkeletonHorse implements Unit, AttackerUnit {
	public static final Abilities ABILITIES = new Abilities();
	public static final EntityDataAccessor<String> ownerDataAccessor =
		SynchedEntityData.defineId(SkeletonHorseSummonUnit.class, EntityDataSerializers.STRING);
	final static public float maxHealth = 50.0f;
	final static public float armorValue = 0.0f;
	final static public float baseMovementSpeed = 0.4F;
	final static public float aggroRange = 10;
	final static public boolean aggressiveWhenIdle = true;
	final static public boolean willRetaliate = true;
	final static public float attacksPerSecond = 0.5f;
	final static public float attackRange = 2;
	final static public float attackDamage = 2.0f;
	final static public float rangedDamageResist = 0.2f;
	private final ArrayList<Checkpoint> checkpoints = new ArrayList<>();
	private final List<ItemStack> items = new ArrayList<>();
	public int maxResources = 0;
	Object2ObjectArrayMap<Ability, Float> cooldowns = Unit.createCooldownMap();
	Object2ObjectArrayMap<Ability, Integer> charges = new Object2ObjectArrayMap<>();
	
	Ability autocast;
	UsePortalGoal usePortalGoal;
	GarrisonGoal garrisonGoal;
	private int eatingTicksLeft = 0;
	private BlockPos anchorPos = new BlockPos(0, 0, 0);
	private MoveToTargetBlockGoal moveGoal;
	private SelectedTargetGoal<? extends LivingEntity> targetGoal;
	private ReturnResourcesGoal returnResourcesGoal;
	
	private BlockPos attackMoveTarget = null;
	
	private LivingEntity followTarget = null;
	private boolean holdPosition = false;
	private AbstractMeleeAttackUnitGoal attackGoal;
	private MeleeAttackBuildingGoal attackBuildingGoal;
	private Abilities abilities = ABILITIES.clone();
	private int limitedLifeTicks = 900;
	
	public SkeletonHorseSummonUnit(EntityType<? extends SkeletonHorse> entityType, Level level) {
		super(entityType, level);
		updateAbilityButtons();
	}
	
	public static AttributeSupplier.@NotNull Builder createAttributes() {
		return SkeletonHorse.createBaseHorseAttributes()
			.add(Attributes.ATTACK_DAMAGE, SkeletonHorseSummonUnit.attackDamage)
			.add(Attributes.ARMOR, SkeletonHorseSummonUnit.armorValue)
			.add(Attributes.MAX_HEALTH, SkeletonHorseSummonUnit.maxHealth)
			.add(Attributes.FOLLOW_RANGE, Unit.getFollowRange())
			.add(Attributes.ATTACK_KNOCKBACK, 0.05d);
	}
	
	@Override
	public void updateAbilityButtons() {
		abilities = ABILITIES.clone();
	}
	
	@Override
	public Object2ObjectArrayMap<Ability, Float> getCooldowns() {
		return cooldowns;
	}
	
	@Override
	public boolean hasAutocast(Ability ability) {
		return autocast == ability;
	}
	
	@Override
	public void setAutocast(Ability autocast) {
		this.autocast = autocast;
	}
	
	@Override
	public Object2ObjectArrayMap<Ability, Integer> getCharges() {
		return charges;
	}
	
	public int getEatingTicksLeft() {
		return eatingTicksLeft;
	}
	
	public void setEatingTicksLeft(int amount) {
		eatingTicksLeft = amount;
	}
	
	@Override
	public boolean isTrap() {
		return false;
	}
	
	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
		return pSpawnData;
	}
	
	@Override
	public boolean isEating() {
		return false;
	}
	
	@Override
	protected void registerGoals() {
		initialiseGoals();
		this.goalSelector.addGoal(2, usePortalGoal);
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, attackGoal);
		this.goalSelector.addGoal(2, attackBuildingGoal);
		this.goalSelector.addGoal(2, returnResourcesGoal);
		this.targetSelector.addGoal(2, targetGoal);
		this.goalSelector.addGoal(3, moveGoal);
		this.goalSelector.addGoal(4, new RandomLookAroundUnitGoal(this));
	}
	
	@Override
	public SunlightEffect getSunlightEffect() {
		return SunlightEffect.SLOWNESS_II;
	}
	
	@Override
	public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
		return super.hurt(pSource, pAmount);
	}
	
	@Override
	public boolean canEatGrass() {
		return false;
	}
	
	@Override
	public boolean isStanding() {
		return false;
	}
	
	public BlockPos getAnchor() {
		return anchorPos;
	}
	
	public void setAnchor(BlockPos bp) {
		anchorPos = bp;
	}
	
	public ArrayList<Checkpoint> getCheckpoints() {
		return checkpoints;
	}
	
	public GarrisonGoal getGarrisonGoal() {
		return garrisonGoal;
	}
	
	public boolean canGarrison() {
		return getGarrisonGoal() != null;
	}
	
	public UsePortalGoal getUsePortalGoal() {
		return usePortalGoal;
	}
	
	public boolean canUsePortal() {
		return getUsePortalGoal() != null;
	}
	
	public Faction getFaction() {
		return Faction.MONSTERS;
	}
	
	public Abilities getAbilities() {
		return abilities;
	}
	
	public List<ItemStack> getItems() {
		return items;
	}
	
	public MoveToTargetBlockGoal getMoveGoal() {
		return moveGoal;
	}
	
	public SelectedTargetGoal<? extends LivingEntity> getTargetGoal() {
		return targetGoal;
	}
	
	public Goal getAttackBuildingGoal() {
		return attackBuildingGoal;
	}
	
	public Goal getAttackGoal() {
		return attackGoal;
	}
	
	public ReturnResourcesGoal getReturnResourcesGoal() {
		return returnResourcesGoal;
	}
	
	public int getMaxResources() {
		return maxResources;
	}
	
	public BlockPos getAttackMoveTarget() {
		return attackMoveTarget;
	}
	
	public void setAttackMoveTarget(@Nullable BlockPos bp) {
		this.attackMoveTarget = bp;
	}
	
	public LivingEntity getFollowTarget() {
		return followTarget;
	}
	
	public void setFollowTarget(@Nullable LivingEntity target) {
		this.followTarget = target;
	}
	
	public boolean getHoldPosition() {
		return holdPosition;
	}
	
	public void setHoldPosition(boolean holdPosition) {
		this.holdPosition = holdPosition;
	}
	
	public String getOwnerName() {
		return this.entityData.get(ownerDataAccessor);
	}
	
	public void setOwnerName(String name) {
		this.entityData.set(ownerDataAccessor, name);
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ownerDataAccessor, "");
	}
	
	@Override
	public boolean getWillRetaliate() {
		return willRetaliate;
	}
	
	@Override
	public int getAttackCooldown() {
		return (int) (20 / attacksPerSecond);
	}
	
	@Override
	public int getAge() {
		return 0;
	}
	
	@Override
	public float getAttacksPerSecond() {
		return attacksPerSecond;
	}
	
	public float getAggroRange() {
		return aggroRange;
	}
	
	public boolean getAggressiveWhenIdle() {
		return aggressiveWhenIdle && !isVehicle();
	}
	
	@Override
	public float getAttackRange() {
		return attackRange;
	}
	
	@Override
	public float getUnitAttackDamage() {
		return attackDamage;
	}
	
	public float getMovementSpeed() {
		return baseMovementSpeed;
	}
	
	public float getUnitMaxHealth() {
		return maxHealth;
	}
	
	public ResourceCost getCost() {
		return ResourceCost.Unit(0, 0, 0, 0, 2);
	}
	
	public boolean canAttackBuildings() {
		return getAttackBuildingGoal() != null;
	}
	
	@Override
	public float getUnitRangedArmorPercentage() {
		return rangedDamageResist;
	}
	
	@Override
	public void tick() {
		this.setCanPickUpLoot(false);
		
		super.tick();
		Unit.tick(this);
		AttackerUnit.tick(this);
		
		if (--this.limitedLifeTicks <= 0) {
			this.limitedLifeTicks = 20;
			this.hurt(damageSources().starve(), 1.0F);
		}
	}
	
	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		this.addUnitSaveData(pCompound);
	}
	
	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.readUnitSaveData(pCompound);
	}
	
	public void initialiseGoals() {
		this.usePortalGoal = new UsePortalGoal(this);
		this.moveGoal = new MoveToTargetBlockGoal(this, false, 0);
		this.targetGoal = new SelectedTargetGoal<>(this, true, true);
		this.garrisonGoal = new GarrisonGoal(this);
		this.attackGoal = new MeleeAttackUnitGoal(this, false);
		this.attackBuildingGoal = new MeleeAttackBuildingGoal(this);
		this.returnResourcesGoal = new ReturnResourcesGoal(this);
	}
}