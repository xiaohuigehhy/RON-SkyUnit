package net.xiaohuige_hhy.skyunit.mixin.unit.units.monsters;

import com.solegendary.reignofnether.ability.Abilities;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.ability.AbilityClientboundPacket;
import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.ability.heroAbilities.necromancer.InsomniaCurse;
import com.solegendary.reignofnether.ability.heroAbilities.necromancer.SoulSiphonPassive;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitAnimationAction;
import com.solegendary.reignofnether.unit.goals.GarrisonGoal;
import com.solegendary.reignofnether.unit.goals.GenericTargetedSpellGoal;
import com.solegendary.reignofnether.unit.goals.MoveToTargetBlockGoal;
import com.solegendary.reignofnether.unit.goals.ReturnResourcesGoal;
import com.solegendary.reignofnether.unit.goals.SelectedTargetGoal;
import com.solegendary.reignofnether.unit.goals.UnitRangedAttackGoal;
import com.solegendary.reignofnether.unit.goals.UsePortalGoal;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.NecromancerUnit;
import com.solegendary.reignofnether.unit.units.monsters.SkeletonUnit;
import com.solegendary.reignofnether.unit.units.monsters.StrayUnit;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.xiaohuige_hhy.skyunit.ability.abilities.VanguardCharge;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitEntityRegistrar;
import net.xiaohuige_hhy.skyunit.unit.units.monsters.SkeletonHorseSummonUnit;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

@Mixin(NecromancerUnit.class)
public abstract class NecromancerUnitMixin extends Skeleton {
	
	@Unique
	private final static float skyUnit$baseMovementSpeed = 0.19F;
	@Unique
	private final static float skyUnit$movementSpeedPerRank = 0.07F;
	@Shadow
	@Final
	public static Abilities ABILITIES;
	@Shadow(remap = false)
	@Final
	private static int ATTACK_WINDUP_TICKS;
	@Shadow
	public int souls;
	@Shadow(remap = false)
	UsePortalGoal usePortalGoal;
	@Shadow(remap = false)
	GarrisonGoal garrisonGoal;
	@Shadow(remap = false)
	private GenericTargetedSpellGoal castPhantomGoal;
	@Shadow(remap = false)
	private int eatingTicksLeft;
	@Shadow(remap = false)
	private int experience;
	@Shadow(remap = false)
	private SelectedTargetGoal<? extends LivingEntity> targetGoal;
	@Shadow(remap = false)
	private ReturnResourcesGoal returnResourcesGoal;
	@Shadow(remap = false)
	private MoveToTargetBlockGoal moveGoal;
	@Shadow(remap = false)
	private UnitRangedAttackGoal<? extends LivingEntity> attackGoal;
	@Shadow
	private Abilities abilities;
	
	protected NecromancerUnitMixin(EntityType<? extends Skeleton> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lcom/solegendary/reignofnether/ability/Abilities;add(Lcom/solegendary/reignofnether/ability/Ability;)V"
		),
		remap = false
	)
	private static void redirectAdd(Abilities instance, Ability ability) {
		if (ability instanceof InsomniaCurse) {
			instance.add(new VanguardCharge());
			return;
		}
		instance.add(ability);
	}
	
	@Shadow(remap = false)
	public abstract String getOwnerName();
	
	@Shadow
	public abstract void setOwnerName(String name);
	
	@Shadow(remap = false)
	public abstract int consumeSoulsAndGetSoulRank();
	
	@Shadow(remap = false)
	public abstract void initialiseGoals();
	
	@Shadow
	public abstract void setAnchor(BlockPos bp);
	
	@Shadow
	public abstract boolean hasAutocast(Ability ability);
	
	@Shadow
	public abstract Object2ObjectArrayMap<HeroAbility, Integer> getHeroAbilityRanks();
	
	@Shadow public abstract SoulSiphonPassive getSoulSiphon();
	
	@Unique
	private void SkyUnit$summonSkeletonHorseEntity(LivingEntity targetEntity) {
		if (this.level().isClientSide())
			return;
		this.SkyUnit$summonSkeletonTrap(targetEntity, targetEntity.blockPosition());
	}
	
	@Unique
	private void SkyUnit$summonSkeletonHorseGround(BlockPos targetBp) {
		if (this.level().isClientSide())
			return;
		this.SkyUnit$summonSkeletonTrap(null, targetBp);
	}
	
	
	@Inject(method = "initialiseGoals", at = @At("TAIL"), remap = false)
	private void changePhantomSummonGoal(CallbackInfo ci) {
		this.targetGoal = new SelectedTargetGoal<>(this, true, true);
		this.castPhantomGoal = new GenericTargetedSpellGoal(
			this,
			0,
			VanguardCharge.RANGE,
			UnitAnimationAction.ATTACK_UNIT,
			this::SkyUnit$summonSkeletonHorseEntity,
			this::SkyUnit$summonSkeletonHorseGround,
			null
		);
	}
	
	@Unique
	private void SkyUnit$summonSkeletonTrap(LivingEntity entityTarget, BlockPos pos) {
		TargetingConditions conditions = TargetingConditions.forCombat()
			.ignoreLineOfSight()
			.selector(e ->
				e.isAlive() && Objects.equals(((Unit) e).getOwnerName(), this.getOwnerName()) && !e.isPassenger()
			);
		if (level() instanceof ServerLevel serverlevel) {
			LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(serverlevel);
			if (lightningbolt != null) {
				lightningbolt.moveTo(pos.getX(), pos.getY(), pos.getZ());
				lightningbolt.setVisualOnly(true);
				serverlevel.addFreshEntity(lightningbolt);
				SoulSiphonPassive soulSiphon = getSoulSiphon();
				float soul = 1.0F;
				if (soulSiphon != null && hasAutocast(soulSiphon) && this.souls > 0) {
					soul = Math.min(soulSiphon.soulsPerCast, this.souls);
				}
				for (int i = 0; i < soul / 2; ++i) {
					double x = this.getX();
					double y = this.getY();
					double z = this.getZ();
					AABB searchArea = this.getBoundingBox().inflate(16.0);
					LivingEntity skeleton = level().getNearestEntity(StrayUnit.class, conditions, this, x, y, z, searchArea);
					if (skeleton == null) {
						skeleton = level().getNearestEntity(SkeletonUnit.class, conditions, this, x, y, z, searchArea);
					}
					if (skeleton != null) {
						AbstractHorse abstracthorse = this.SkyUnit$createHorse(entityTarget);
						if (abstracthorse != null) {
							abstracthorse.setPos(pos.getCenter());
							skeleton.startRiding(abstracthorse);
							this.SkyUnit$createSkeleton(skeleton, abstracthorse);
							abstracthorse.push(this.getRandom().triangle(0.0D, 1.1485D), 0.5D, this.getRandom().triangle(0.0D, 1.1485D));
						}
						if (hasAutocast(soulSiphon)) {
							this.souls -= 2;
							if (!level().isClientSide())
								AbilityClientboundPacket.doAbility(getId(), UnitAction.SOUL_SIPHON_UPDATE, this.souls);
						}
						
					}
					
				}
			}
		}
	}
	
	@Unique
	@Nullable
	private AbstractHorse SkyUnit$createHorse(LivingEntity entityTarget) {
		SkeletonHorseSummonUnit skeletonHorse = SkyUnitEntityRegistrar.SKELETON_HORSE_SUMMON_UNIT.get().create(this.level());
		if (skeletonHorse != null) {
			level().addFreshEntity(skeletonHorse);
			skeletonHorse.setTamed(true);
			skeletonHorse.setAge(0);
			skeletonHorse.setOwnerName(this.getOwnerName());
			AttributeInstance ai = skeletonHorse.getAttribute(Attributes.MOVEMENT_SPEED);
			if (ai != null) {
				ai.setBaseValue(skyUnit$baseMovementSpeed + getHeroAbilityRanks().get(skyUnit$getVanguardCharge()) * skyUnit$movementSpeedPerRank);
			}
		}
		
		return skeletonHorse;
	}
	
	@Unique
	public VanguardCharge skyUnit$getVanguardCharge() {
		for (Ability ability : abilities.get())
			if (ability instanceof VanguardCharge)
				return (VanguardCharge) ability;
		return null;
	}
	
	@Contract("_, _ -> param1")
	@Unique
	private @NotNull LivingEntity SkyUnit$createSkeleton(@NotNull LivingEntity skeleton, @NotNull AbstractHorse pHorse) {
		skeleton.setPos(pHorse.getX(), pHorse.getY(), pHorse.getZ());
		((Unit) skeleton).setOwnerName(this.getOwnerName());
		ItemStack helmet = new ItemStack(Items.IRON_HELMET);
		helmet.setDamageValue(100);
		if (skeleton.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
			skeleton.setItemSlot(EquipmentSlot.HEAD, helmet);
		}
		
		skeleton.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(skeleton.getRandom(), this.SkyUnit$disenchant(skeleton.getMainHandItem()), (int) (22.0F * (float) skeleton.getRandom().nextInt(18)), false));
		skeleton.setItemSlot(EquipmentSlot.HEAD, EnchantmentHelper.enchantItem(skeleton.getRandom(), this.SkyUnit$disenchant(skeleton.getItemBySlot(EquipmentSlot.HEAD)), (int) (22.0F * (float) skeleton.getRandom().nextInt(18)), false));
		return skeleton;
	}
	
	@Contract("_ -> param1")
	@Unique
	private @NotNull ItemStack SkyUnit$disenchant(@NotNull ItemStack pStack) {
		pStack.removeTagKey("Enchantments");
		return pStack;
	}
	
}