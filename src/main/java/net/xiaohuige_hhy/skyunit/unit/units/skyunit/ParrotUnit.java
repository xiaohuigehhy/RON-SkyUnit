package net.xiaohuige_hhy.skyunit.unit.units.skyunit;

import com.solegendary.reignofnether.ability.Abilities;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.building.BuildingPlaceButton;
import com.solegendary.reignofnether.building.custombuilding.CustomBuildingClientEvents;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.Checkpoint;
import com.solegendary.reignofnether.unit.goals.AbstractMeleeAttackUnitGoal;
import com.solegendary.reignofnether.unit.goals.BuildRepairGoal;
import com.solegendary.reignofnether.unit.goals.GarrisonGoal;
import com.solegendary.reignofnether.unit.goals.GatherResourcesGoal;
import com.solegendary.reignofnether.unit.goals.MeleeAttackUnitGoal;
import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import com.solegendary.reignofnether.unit.goals.RandomLookAroundUnitGoal;
import com.solegendary.reignofnether.unit.goals.ReturnResourcesGoal;
import com.solegendary.reignofnether.unit.goals.SelectedTargetGoal;
import com.solegendary.reignofnether.unit.goals.UsePortalGoal;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.interfaces.WorkerUnit;

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
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.xiaohuige_hhy.skyunit.SkyUnitConfigs;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;
import net.xiaohuige_hhy.skyunit.unit.interfaces.SkyUnitUnit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class ParrotUnit extends Parrot implements SkyUnitUnit, WorkerUnit, AttackerUnit {
	public static final Abilities ABILITIES = new Abilities();
	public static final EntityDataAccessor<String> ownerDataAccessor =
		SynchedEntityData.defineId(ParrotUnit.class, EntityDataSerializers.STRING);
	final static public float attackDamage = 1.0f;
	final static public float attacksPerSecond = 0.5f;
	final static public float attackRange = 2; // only used by ranged units or melee building attackers
	final static public float aggroRange = 0;
	final static public boolean willRetaliate = false; // will attack when hurt by an enemy
	final static public boolean aggressiveWhenIdle = false;
	final static public float maxHealth = 25.0f;
	final static public float armorValue = 0.0f;
	final static public float movementSpeed = 0.4f;
	final static public float flyingSpeed = 1f;
	private final ArrayList<Checkpoint> checkpoints = new ArrayList<>();
	private final List<ItemStack> items = new ArrayList<>();
	public BuildRepairGoal buildRepairGoal;
	public GatherResourcesGoal gatherResourcesGoal;
	public int maxResources = 100;
	Object2ObjectArrayMap<Ability, Float> cooldowns = Unit.createCooldownMap();
	Object2ObjectArrayMap<Ability, Integer> charges = new Object2ObjectArrayMap<>();
	
	
	Ability autocast;
	GarrisonGoal garrisonGoal;
	UsePortalGoal usePortalGoal;
	private Abilities abilities = ABILITIES.clone();
	private int eatingTicksLeft = 0;
	private BlockPos anchorPos = new BlockPos(0, 0, 0);
	private MoveToTargetBlockGoal moveGoal;
	private SelectedTargetGoal<? extends LivingEntity> targetGoal;
	private ReturnResourcesGoal returnResourcesGoal;
	private AbstractMeleeAttackUnitGoal attackGoal;
	
	
	// if true causes moveGoal and attackGoal to work together to allow attack moving
	// moves to a block but will chase/attack nearby monsters in range up to a certain distance away
	private LivingEntity followTarget = null; // if nonnull, continuously moves to the target
	private boolean holdPosition = false;
	private BlockPos attackMoveTarget = null;
	
	public ParrotUnit(EntityType<? extends Parrot> entityType, Level level) {
		super(entityType, level);
		
		updateAbilityButtons();
	}
	
	public static List<BuildingPlaceButton> getBuildingButtons() {
		List<BuildingPlaceButton> buttons = new ArrayList<>(SkyUnitConfigs.SKYUNIT.getBuildingButtons());
		
		//TODO Add to Register
		CustomBuildingClientEvents.customBuildings.forEach(cb -> buttons.add(cb.getWorkerBuildButton(null)));
		
		return buttons;
	}
	
	public static AttributeSupplier.@NotNull Builder createAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.ATTACK_DAMAGE, ParrotUnit.attackDamage)
			.add(Attributes.MOVEMENT_SPEED, ParrotUnit.movementSpeed)
			.add(Attributes.FLYING_SPEED, ParrotUnit.flyingSpeed)
			.add(Attributes.MAX_HEALTH, ParrotUnit.maxHealth)
			.add(Attributes.FOLLOW_RANGE, Unit.getFollowRange())
			.add(Attributes.ARMOR, ParrotUnit.armorValue);
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
	
	public UsePortalGoal getUsePortalGoal() {
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
	
	public BuildRepairGoal getBuildRepairGoal() {
		return buildRepairGoal;
	}
	
	public GatherResourcesGoal getGatherResourceGoal() {
		return gatherResourcesGoal;
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
	
	// endregion
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ownerDataAccessor, "");
	}
	
	// combat stats
	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public float getUnitMaxHealth() {
		return maxHealth;
	}
	
	@Nullable
	public ResourceCost getCost() {
		return SkyUnitResourceCosts.PARROT;
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
	
	public BlockState getReplantBlockState() {
		return Blocks.BEETROOTS.defaultBlockState();
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
		WorkerUnit.tick(this);
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
		this.buildRepairGoal = new BuildRepairGoal(this);
		this.gatherResourcesGoal = new GatherResourcesGoal(this);
		this.returnResourcesGoal = new ReturnResourcesGoal(this);
	}
	
	@Override
	protected void registerGoals() {
		initialiseGoals();
		this.goalSelector.addGoal(2, usePortalGoal);
		
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, attackGoal);
		this.goalSelector.addGoal(2, buildRepairGoal);
		this.goalSelector.addGoal(2, gatherResourcesGoal);
		this.goalSelector.addGoal(2, returnResourcesGoal);
		this.goalSelector.addGoal(2, garrisonGoal);
		this.targetSelector.addGoal(2, targetGoal);
		this.goalSelector.addGoal(3, moveGoal);
		this.goalSelector.addGoal(4, new RandomLookAroundUnitGoal(this));
	}
	
	@Override
	public List<Button> getAbilityButtons() {
		List<Button> abilities = new ArrayList<>(getAbilities().getButtons(this));
		if (FMLEnvironment.dist == Dist.CLIENT) {
			abilities.addAll(getBuildingButtons());
		}
		return abilities;
	}
}