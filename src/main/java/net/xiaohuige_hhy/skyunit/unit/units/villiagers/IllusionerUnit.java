package net.xiaohuige_hhy.skyunit.unit.units.villiagers;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.ability.AbilityClientboundPacket;
import com.solegendary.reignofnether.ability.abilities.PromoteIllager;
import com.solegendary.reignofnether.fogofwar.FogOfWarClientboundPacket;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.unit.Checkpoint;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitAnimationAction;
import com.solegendary.reignofnether.unit.goals.GarrisonGoal;
import com.solegendary.reignofnether.unit.goals.GenericTargetedSpellGoal;
import com.solegendary.reignofnether.unit.goals.GenericUntargetedSpellGoal;
import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import com.solegendary.reignofnether.unit.goals.RandomLookAroundUnitGoal;
import com.solegendary.reignofnether.unit.goals.ReturnResourcesGoal;
import com.solegendary.reignofnether.unit.goals.SelectedTargetGoal;
import com.solegendary.reignofnether.unit.goals.UnitBowAttackGoal;
import com.solegendary.reignofnether.unit.goals.UsePortalGoal;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.RangedAttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.modelling.models.VillagerUnitModel;
import com.solegendary.reignofnether.util.Faction;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.ability.abilities.BlindnessSpellGoal;
import net.xiaohuige_hhy.skyunit.ability.abilities.MirrorSpellGoal;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class IllusionerUnit extends Illusioner implements Unit, AttackerUnit, RangedAttackerUnit {
	
	public static final EntityDataAccessor<String> ownerDataAccessor =
		SynchedEntityData.defineId(IllusionerUnit.class, EntityDataSerializers.STRING);
	final static public float maxHealth = 80.0f;
	final static public float armorValue = 0.0f;
	final static public float movementSpeed = 0.3F;
	final static public float aggroRange = 18.0f;
	final static public boolean aggressiveWhenIdle = true;
	final static public boolean willRetaliate = true;
	final static public float attacksPerSecond = 0.7f;
	final static public float attackRange = 18.0f;
	final static public float attackDamage = 10.0f;
	public static final int CAST_MIRROR_SPELL_CHANNEL_SECONDS = 1;
	public static final int CAST_BLINDNESS_SPELL_CHANNEL_SECONDS = 1;
	private final ArrayList<Checkpoint> checkpoints = new ArrayList<>();
	private final List<AbilityButton> abilityButtons = new ArrayList<>();
	private final List<Ability> abilities = new ArrayList<>();
	private final List<ItemStack> items = new ArrayList<>();
	public int maxResources = 100;
	public int fogRevealDuration = 0;
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
	private UnitBowAttackGoal<? extends LivingEntity> attackGoal;
	private GenericUntargetedSpellGoal castMirrorSpellGoal;
	private GenericTargetedSpellGoal castBlindnessSpellGoal;
	
	public IllusionerUnit(EntityType<? extends Illusioner> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.abilities.add(new MirrorSpellGoal(this));
		this.abilities.add(new BlindnessSpellGoal(this));
		updateAbilityButtons();
	}
	
	public static AttributeSupplier.@NotNull Builder createAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MOVEMENT_SPEED, IllusionerUnit.movementSpeed)
			.add(Attributes.MAX_HEALTH, IllusionerUnit.maxHealth)
			.add(Attributes.FOLLOW_RANGE, Unit.getFollowRange())
			.add(Attributes.ARMOR, IllusionerUnit.armorValue);
	}
	
	public GenericUntargetedSpellGoal getCastMirrorSpellGoal() {
		return castMirrorSpellGoal;
	}
	
	@Override
	public int getEatingTicksLeft() {
		return eatingTicksLeft;
	}
	
	@Override
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
	
	public Faction getFaction() {
		return Faction.VILLAGERS;
	}
	
	public List<AbilityButton> getAbilityButtons() {
		return abilityButtons;
	}
	
	public List<Ability> getAbilities() {
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
		return null;
	}
	
	public UnitBowAttackGoal<? extends LivingEntity> getAttackGoal() {
		return attackGoal;
	}
	
	public ReturnResourcesGoal getReturnResourcesGoal() {
		return returnResourcesGoal;
	}
	
	public int getMaxResources() {
		return maxResources;
	}
	
	// endregion
	
	public BlockPos getAttackMoveTarget() {
		return attackMoveTarget;
	}
	
	public void setAttackMoveTarget(@javax.annotation.Nullable BlockPos bp) {
		this.attackMoveTarget = bp;
	}
	
	public LivingEntity getFollowTarget() {
		return followTarget;
	}
	
	public void setFollowTarget(@javax.annotation.Nullable LivingEntity target) {
		this.followTarget = target;
	}
	
	public boolean getHoldPosition() {
		return holdPosition;
	}
	
	public void setHoldPosition(boolean holdPosition) {
		this.holdPosition = holdPosition;
	}
	
	// which player owns this unit? this format ensures its synced to client without having to use packets
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
	
	// combat stats
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
	
	public float getMovementSpeed() {
		return movementSpeed;
	}
	
	public float getUnitAttackDamage() {
		return attackDamage;
	}
	
	public float getUnitMaxHealth() {
		return maxHealth;
	}
	
	public float getUnitArmorPercentage() {
		return armorValue;
	}
	
	@Nullable
	public ResourceCost getCost() {
		return ResourceCosts.PILLAGER;
	}
	
	public boolean canAttackBuildings() {
		return getAttackBuildingGoal() != null && isPassenger();
	}
	
	public int getFogRevealDuration() {
		return fogRevealDuration;
	}
	
	public void setFogRevealDuration(int duration) {
		fogRevealDuration = duration;
	}
	
	@Override
	public boolean removeWhenFarAway(double d) {
		return false;
	}
	
	public void tick() {
		this.setCanPickUpLoot(true);
		super.tick();
		Unit.tick(this);
		AttackerUnit.tick(this);
		this.castMirrorSpellGoal.tick();
		this.castBlindnessSpellGoal.tick();
		PromoteIllager.checkAndApplyBuff(this);
		
		for (Ability ability : getAbilities()) {
			if (ability instanceof BlindnessSpellGoal blindnessSpellGoal) {
				if (getTargetGoal() != null) {
					LivingEntity targetEntity = getTargetGoal().getTarget();
					if (targetEntity != null && blindnessSpellGoal.isAutocasting() && !isCastingSpell() && blindnessSpellGoal.isOffCooldown() && !level().isClientSide() && tickCount % 4 == 0 && this.distanceTo(targetEntity) <= getAttackRange()) {
						ability.use(level(), this, targetEntity);
					}
				}
			}
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
		this.targetGoal = new SelectedTargetGoal<>(this, true, false);
		this.garrisonGoal = new GarrisonGoal(this);
		this.attackGoal = new UnitBowAttackGoal<>(this);
		this.returnResourcesGoal = new ReturnResourcesGoal(this);
		this.castMirrorSpellGoal = new GenericUntargetedSpellGoal(
			this,
			CAST_MIRROR_SPELL_CHANNEL_SECONDS * ResourceCost.TICKS_PER_SECOND,
			this::performMirrorSpellCasting,
			UnitAnimationAction.NON_KEYFRAME_START,
			UnitAnimationAction.NON_KEYFRAME_STOP
		);
		this.castBlindnessSpellGoal = new GenericTargetedSpellGoal(
			this,
			CAST_BLINDNESS_SPELL_CHANNEL_SECONDS * ResourceCost.TICKS_PER_SECOND,
			BlindnessSpellGoal.RANGE,
			this::performBlindnessSpellCasting,
			null,
			null
		);
	}
	
	public VillagerUnitModel.ArmPose getIllusionerArmPose() {
		if (this.isCastingSpell()) {
			return VillagerUnitModel.ArmPose.SPELLCASTING;
		} else {
			return this.isAggressive() ? VillagerUnitModel.ArmPose.BOW_AND_ARROW :VillagerUnitModel.ArmPose.CROSSED;
		}
	}
	
	@Override
	public boolean isCastingSpell() {
		for (Ability ability : getAbilities()) {
			if (ability instanceof MirrorSpellGoal) {
				if (ability.isOffCooldown() && this.getCastMirrorSpellGoal() != null && this.getCastMirrorSpellGoal().isCasting())
					return true;
			}
			if (ability instanceof BlindnessSpellGoal) {
				if (ability.isOffCooldown() && this.getCastBlindnessSpellGoal() != null && this.getCastBlindnessSpellGoal().isCasting())
					return true;
			}
		}
		return false;
	}
	
	public void performBlindnessSpellCasting(LivingEntity entity) {
		if (entity != null) {
			entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400));
			if (entity instanceof Unit unit)
				Unit.fullResetBehaviours(unit);
		}
		
		
		for (Ability ability : getAbilities())
			if (ability instanceof BlindnessSpellGoal blindnessSpellGoal) {
				blindnessSpellGoal.setToMaxCooldown();
				if (!level().isClientSide())
					AbilityClientboundPacket.sendSetCooldownPacket(getId(), UnitAction.TELEPORT, blindnessSpellGoal.cooldownMax);
			}
		
	}
	
	public void performMirrorSpellCasting() {
		if (this.level().isClientSide())
			return;
		this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
		level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(50),
			e -> e instanceof Unit unit && unit.getTargetGoal().getTarget() == this
		).forEach(unit -> Unit.fullResetBehaviours((Unit) unit));
		
		
		for (Ability ability : getAbilities())
			if (ability instanceof MirrorSpellGoal)
				AbilityClientboundPacket.sendSetCooldownPacket(getId(), UnitAction.ROAR, MirrorSpellGoal.CD_MAX_SECONDS);
		
	}
	
	@Override
	public void resetBehaviours() {
		if (this.getCastBlindnessSpellGoal() != null)
			this.castBlindnessSpellGoal.stop();
		this.castMirrorSpellGoal.stop();
		if (attackGoal != null && !this.abilities.isEmpty() && this.abilities.get(0).isOffCooldown())
			this.attackGoal.resetCooldown();
		this.getCheckpoints().clear();
	}
	
	@Override
	protected void registerGoals() {
		initialiseGoals();
		this.goalSelector.addGoal(2, usePortalGoal);
		
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, attackGoal);
		this.goalSelector.addGoal(2, returnResourcesGoal);
		this.goalSelector.addGoal(2, garrisonGoal);
		this.targetSelector.addGoal(2, targetGoal);
		this.goalSelector.addGoal(2, castBlindnessSpellGoal);
		this.goalSelector.addGoal(3, moveGoal);
		this.goalSelector.addGoal(4, new RandomLookAroundUnitGoal(this));
	}
	
	@Override
	public void setupEquipmentAndUpgradesServer() {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
	}
	
	@Override
	public void performUnitRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
		if (isCastingSpell())
			return;
		
		ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
		AbstractArrow abstractarrow = ProjectileUtil.getMobArrow(this, itemstack, pDistanceFactor);
		if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem)
			abstractarrow = ((net.minecraft.world.item.BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);
		double d0 = pTarget.getX() - this.getX();
		double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
		double d2 = pTarget.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		if (pTarget.getEyeHeight() <= 1.0f)
			d1 -= (1.0f - pTarget.getEyeHeight());
		
		abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.6F, 0);
		this.level().addFreshEntity(abstractarrow);
		
		if (!level().isClientSide() && pTarget instanceof Unit unit)
			FogOfWarClientboundPacket.revealRangedUnit(unit.getOwnerName(), this.getId());
	}
	
	public GenericTargetedSpellGoal getCastBlindnessSpellGoal() {
		return castBlindnessSpellGoal;
	}
}
