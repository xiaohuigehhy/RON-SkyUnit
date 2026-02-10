package net.xiaohuige_hhy.skyunit.unit.units.skyunit;

import com.solegendary.reignofnether.ability.Abilities;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.time.NightUtils;
import com.solegendary.reignofnether.unit.Checkpoint;
import com.solegendary.reignofnether.unit.goals.FlyingUsePortalGoal;
import com.solegendary.reignofnether.unit.goals.GarrisonGoal;
import com.solegendary.reignofnether.unit.goals.MeleeAttackBuildingGoal;
import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import com.solegendary.reignofnether.unit.goals.ReturnResourcesGoal;
import com.solegendary.reignofnether.unit.goals.SelectedTargetGoal;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;
import net.xiaohuige_hhy.skyunit.unit.controls.PhantomUnitMoveControl;
import net.xiaohuige_hhy.skyunit.unit.goals.PhantomUnitAttackGoal;
import net.xiaohuige_hhy.skyunit.unit.goals.PhantomUnitAttackStrategyGoal;
import net.xiaohuige_hhy.skyunit.unit.goals.PhantomUnitFollowGoal;
import net.xiaohuige_hhy.skyunit.unit.interfaces.SkyUnitUnit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class PhantomUnit extends Phantom implements SkyUnitUnit, AttackerUnit {
	public static final Abilities ABILITIES = new Abilities();
	public static final EntityDataAccessor<String> ownerDataAccessor =
		SynchedEntityData.defineId(PhantomUnit.class, EntityDataSerializers.STRING);
	final static public float attackDamage = 6.0f;
	final static public float attacksPerSecond = 0.5f;
	final static public float aggroRange = 12;
	final static public float armorValue = 0.0f;
	final static public boolean willRetaliate = true; // will attack when hurt by an enemy
	final static public boolean aggressiveWhenIdle = true;
	final static public float maxHealth = 50.0f;
	final static public float movementSpeed = 0.25f;
	final static public float flyingSpeed = 1.0f;
	final static public float attackRange = 2;
	private final ArrayList<Checkpoint> checkpoints = new ArrayList<>();
	private final List<ItemStack> items = new ArrayList<>();
	public int maxResources = 100;
	public Vec3 moveTargetPoint = Vec3.ZERO;
	public AttackPhase attackPhase = AttackPhase.CIRCLE;
	public BlockPos anchorPoint = BlockPos.ZERO;
	Object2ObjectArrayMap<Ability, Float> cooldowns = Unit.createCooldownMap();
	Object2ObjectArrayMap<Ability, Integer> charges = new Object2ObjectArrayMap<>();
	Ability autocast;
	GarrisonGoal garrisonGoal;
	FlyingUsePortalGoal usePortalGoal;
	private MeleeAttackBuildingGoal attackBuildingGoal;
	private Abilities abilities = ABILITIES.clone();
	private int eatingTicksLeft = 0;
	private BlockPos anchorPos = new BlockPos(0, 0, 0);
	private MoveToTargetBlockGoal moveGoal;
	private SelectedTargetGoal<? extends LivingEntity> targetGoal;
	private ReturnResourcesGoal returnResourcesGoal;
	private LivingEntity followTarget = null; // if nonnull, continuously moves to the target
	private boolean holdPosition = false;
	private BlockPos attackMoveTarget = null;
	private Goal attackGoal;
	
	public PhantomUnit(EntityType<? extends Phantom> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new PhantomUnitMoveControl(this);
		updateAbilityButtons();
	}
	
	public static AttributeSupplier.@NotNull Builder createAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.ATTACK_DAMAGE, PhantomUnit.attackDamage)
			.add(Attributes.MOVEMENT_SPEED, PhantomUnit.movementSpeed)
			.add(Attributes.FLYING_SPEED, PhantomUnit.flyingSpeed)
			.add(Attributes.MAX_HEALTH, PhantomUnit.maxHealth)
			.add(Attributes.FOLLOW_RANGE, Unit.getFollowRange())
			.add(Attributes.ARMOR, PhantomUnit.armorValue);
	}
	
	@Override
	protected boolean isSunBurnTick() {
		return NightUtils.isSunBurnTick(this);
	}
	
	@Override
	public SunlightEffect getSunlightEffect() {
		return SunlightEffect.FIRE;
	}
	
	//region
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
	
	public FlyingUsePortalGoal getUsePortalGoal() {
		return usePortalGoal;
	}
	
	public boolean canUsePortal() {
		return getUsePortalGoal() != null;
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
	
	public ReturnResourcesGoal getReturnResourcesGoal() {
		return returnResourcesGoal;
	}
	
	public int getMaxResources() {
		return maxResources;
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
	
	// which player owns this unit? this format ensures its synched to client without having to use packets
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
	
	// endregion
	
	// comPhantom stats
	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public float getUnitMaxHealth() {
		return maxHealth;
	}
	
	@Nullable
	public ResourceCost getCost() {
		return SkyUnitResourceCosts.PHANTOM;
	}
	
	public boolean getWillRetaliate() {
		return willRetaliate;
	}
	
	public int getAttackCooldown() {
		return (int) (20 / attacksPerSecond);
	}
	
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
	
	public float getUnitAttackDamage() {
		return attackDamage;
	}
	
	public BlockPos getAttackMoveTarget() {
		return attackMoveTarget;
	}
	
	public void setAttackMoveTarget(@Nullable BlockPos bp) {
		this.attackMoveTarget = bp;
	}
	
	public boolean canAttackBuildings() {
		return getAttackBuildingGoal() != null;
	}
	
	public Goal getAttackGoal() {
		return attackGoal;
	}
	
	public Goal getAttackBuildingGoal() {
		return null;
	}
	
	@Override
	public boolean isPushable() {
		return false;
	}
	
	@Override
	public boolean isLeftHanded() {
		return false;
	}
	
	@Override // prevent vanilla logic for picking up items
	protected void pickUpItem(@NotNull ItemEntity pItemEntity) {
	}
	
	@Override
	protected void customServerAiStep() {
	}
	
	@Override
	public LivingEntity getTarget() {
		return this.targetGoal.getTarget();
	}
	
	public void tick() {
		this.setCanPickUpLoot(true);
		super.tick();
		Unit.tick(this);
		AttackerUnit.tick(this);
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
	
	@Override
	public void initialiseGoals() {
		this.usePortalGoal = new FlyingUsePortalGoal(this);
		this.moveGoal = new PhantomUnitFollowGoal(this);
		this.attackGoal = new PhantomUnitAttackGoal(this);
		this.targetGoal = new SelectedTargetGoal<>(this, true, true);
		this.garrisonGoal = new GarrisonGoal(this);
		this.returnResourcesGoal = new ReturnResourcesGoal(this);
	}
	
	@Override
	protected void registerGoals() {
		initialiseGoals();
		this.goalSelector.addGoal(2, usePortalGoal);
		
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new PhantomUnitAttackStrategyGoal(this));
		this.goalSelector.addGoal(2, attackGoal);
		this.goalSelector.addGoal(2, returnResourcesGoal);
		this.goalSelector.addGoal(2, garrisonGoal);
		this.targetSelector.addGoal(2, targetGoal);
		this.targetSelector.addGoal(3, moveGoal);
	}
	
	@Override
	public List<Button> getAbilityButtons() {
		return new ArrayList<>(getAbilities().getButtons(this));
	}
	
	public enum AttackPhase {
		CIRCLE,
		SWOOP
	}
}