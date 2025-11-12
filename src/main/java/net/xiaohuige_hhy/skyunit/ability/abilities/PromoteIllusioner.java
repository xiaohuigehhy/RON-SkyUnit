package net.xiaohuige_hhy.skyunit.ability.abilities;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.BuildingServerEvents;
import com.solegendary.reignofnether.building.buildings.placements.CastlePlacement;
import com.solegendary.reignofnether.building.buildings.villagers.Library;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.units.villagers.EvokerUnit;
import com.solegendary.reignofnether.unit.units.villagers.PillagerUnit;
import com.solegendary.reignofnether.util.MiscUtil;
import com.solegendary.reignofnether.util.MyRenderer;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitEntityRegistrar;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerUnit;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PromoteIllusioner extends Ability {
	
	private static final int CD_MAX = 20 * ResourceCost.TICKS_PER_SECOND;
	private static final int RANGE = 20;
	private static final float PILLAGER_RANGE = 16;
	
	IllusionerUnit promotedIllusioner = null;
	BuildingPlacement buildingPlacement;
	
	public PromoteIllusioner(@NotNull BuildingPlacement buildingPlacement) {
		super(UnitAction.PROMOTE_ILLAGER, buildingPlacement.getLevel(), CD_MAX, RANGE, 0, true, true);
		this.buildingPlacement = buildingPlacement;
		this.defaultHotkey = Keybindings.keyY;
	}
	
	private int getMaxCharges() {
		int charge = 2;
		for (BuildingPlacement building : BuildingServerEvents.getBuildings()) {
			if (building.ownerName.equals(buildingPlacement.ownerName)) {
				if (building instanceof CastlePlacement && building.isBuilt) {
					charge += 5;
				}
			}
		}
		return charge;
	}
	
	private int getCharges() {
		int charge = 0;
		for (LivingEntity entity : UnitServerEvents.getAllUnits()) {
			if (entity instanceof IllusionerUnit illusioner && illusioner.getOwnerName().equals(buildingPlacement.ownerName)) {
				charge++;
			}
		}
		return charge;
	}
	
	@Override
	public AbilityButton getButton(Keybinding hotkey) {
		return new AbilityButton("Promote Illusioner",
			new ResourceLocation(SkyUnit.MOD_ID, "textures/mobheads/illusioner.png"),
			hotkey,
			() -> false,
			() -> (buildingPlacement.getBuilding() instanceof Library && buildingPlacement.getUpgradeLevel() != 2) || (getMaxCharges() == getCharges()),
			() -> true,
			() -> CursorClientEvents.setLeftClickAction(UnitAction.PROMOTE_ILLAGER),
			null,
			List.of(
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.promote_illusioner"),
					Style.EMPTY.withBold(true)
				),
				FormattedCharSequence.forward(
					I18n.get("abilities.reignofnether.promote_illusioner.tooltip1", CD_MAX / 20) + RANGE,
					MyRenderer.iconStyle
				),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.promote_illusioner.tooltip2"),
					Style.EMPTY
				),
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.promote_illusioner.tooltip3"),
					Style.EMPTY
				),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.promote_illusioner.tooltip4"), Style.EMPTY)
			),
			this
		);
	}
	
	@Override
	public void tickCooldown() {
		super.tickCooldown();
		this.maxCharges = getMaxCharges() - getCharges();
		this.charges = getMaxCharges() - getCharges();
		//使用Library的tick进行改变charge
	}
	
	@Override
	public boolean isOffCooldown() {
		return this.getCooldown() <= 0;
	}
	
	@Override
	public boolean canBypassCooldown() {
		return false;
	}
	
	@Override
	public void use(Level level, @NotNull BuildingPlacement buildingUsing, @NotNull LivingEntity targetEntity) {
		Vec3 pos = targetEntity.getEyePosition();
		if (buildingUsing.centrePos.distToCenterSqr(pos.x, pos.y, pos.z) > RANGE * RANGE) {
			if (level.isClientSide()) {
				HudClientEvents.showTemporaryMessage(I18n.get("abilities.reignofnether.promote_illusioner.error1"));
			}
		} else if (targetEntity instanceof PillagerUnit unit) {
			
			if (!unit.getOwnerName().equals(this.buildingPlacement.ownerName)) {
				if (level.isClientSide()) {
					HudClientEvents.showTemporaryMessage(I18n.get("abilities.reignofnether.promote_illusioner.error3"));
				}
				return;
			}
			if (!level.isClientSide()) {
				List<EvokerUnit> nearbyEntities = MiscUtil.getEntitiesWithinRange(unit.position(), PILLAGER_RANGE, EvokerUnit.class, level);
				EvokerUnit evokerUnit = null;
				if (nearbyEntities != null) {
					for (EvokerUnit tle : nearbyEntities) {
						if (Objects.equals(tle.getOwnerName(), unit.getOwnerName())) {
							evokerUnit = tle;
							break;
						}
					}
				}
				
				if (evokerUnit != null) {
					IllusionerUnit illusionerUnit = SkyUnitEntityRegistrar.ILLUSIONER_UNIT.get().create(level);
					if (illusionerUnit != null) {
						level.addFreshEntity(illusionerUnit);
						illusionerUnit.setOwnerName(unit.getOwnerName());
						illusionerUnit.setPos(unit.position());
						promotedIllusioner = illusionerUnit;
					}
					evokerUnit.discard();
					unit.discard();
				}
				MiscUtil.shootFirework(level, unit.getEyePosition());
			}
			
			this.setToMaxCooldown();
		} else {
			if (level.isClientSide()) {
				HudClientEvents.showTemporaryMessage(I18n.get("abilities.reignofnether.promote_illusioner.error4"));
			}
		}
	}
}
