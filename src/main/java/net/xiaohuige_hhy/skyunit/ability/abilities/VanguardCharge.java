package net.xiaohuige_hhy.skyunit.ability.abilities;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;
import static com.solegendary.reignofnether.util.MiscUtil.fcsIcons;

import com.solegendary.reignofnether.ability.HeroAbility;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.HeroUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.NecromancerUnit;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.SkyUnit;

import java.util.List;

public class VanguardCharge extends HeroAbility {
	
	public static final int RANGE = 12;
	public static final float SKELETON_DAMAGE = 4;
	public static final float SKELETON_HORSE_DAMAGE = 2;
	public static final int CONSUME_SOUL_RANK_PER_SKELETON_HORSE = 2;
	
	public VanguardCharge() {
		super(3, 30, UnitAction.INSOMNIA_CURSE, 30 * ResourceCost.TICKS_PER_SECOND, RANGE, 0, true);
		maxCharges = 1;
	}
	
	@Override
	public boolean rankUp(HeroUnit hero) {
		if (super.rankUp(hero)) {
			updateStatsForRank(hero);
			return true;
		}
		return false;
	}
	
	@Override
	public void updateStatsForRank(HeroUnit hero) {
		if (getRank(hero) == 1) {
			cooldownMax = 30 * ResourceCost.TICKS_PER_SECOND;
		} else if (getRank(hero) == 2) {
			cooldownMax = 25 * ResourceCost.TICKS_PER_SECOND;
		} else if (getRank(hero) == 3) {
			cooldownMax = 20 * ResourceCost.TICKS_PER_SECOND;
		}
	}
	
	@Override
	public AbilityButton getButton(Keybinding hotkey, Unit unit) {
		if (!(unit instanceof HeroUnit hero))
			return null;
		return new AbilityButton("Vanguard Charge",
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/skeleton_horse.png"),
			hotkey,
			() -> CursorClientEvents.getLeftClickAction() == UnitAction.INSOMNIA_CURSE,
			() -> getRank(hero) == 0,
			() -> true,
			() -> CursorClientEvents.setLeftClickAction(UnitAction.INSOMNIA_CURSE),
			null,
			getTooltipLines(hero),
			this,
			hero
		);
	}
	
	@Override
	public Button getRankUpButton(HeroUnit hero) {
		return super.getRankUpButtonProtected(
			"Vanguard Charge",
			ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "textures/mobheads/skeleton_horse.png"),
			hero
		);
	}
	
	public List<FormattedCharSequence> getTooltipLines(HeroUnit hero) {
		return List.of(
			fcs(I18n.get("abilities.reignofnether.vanguard_charge") + " " + rankString(hero), true),
			fcsIcons(I18n.get("abilities.reignofnether.vanguard_charge.stats", SKELETON_DAMAGE, SKELETON_HORSE_DAMAGE
				, cooldownMax / 20, RANGE, manaCost)),
			fcs(""),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.tooltip1")),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.tooltip2", CONSUME_SOUL_RANK_PER_SKELETON_HORSE)),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.tooltip3")),
			fcs(""),
			fcs(I18n.get("abilities.reignofnether.charges", maxCharges))
		);
	}
	
	public List<FormattedCharSequence> getRankUpTooltipLines(HeroUnit hero) {
		return List.of(
			fcs(I18n.get("abilities.reignofnether.vanguard_charge"), true),
			fcs(I18n.get("abilities.reignofnether.level_req", getLevelRequirement(hero)), getLevelReqStyle(hero)),
			fcs(""),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.tooltip1")),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.tooltip2", CONSUME_SOUL_RANK_PER_SKELETON_HORSE)),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.tooltip3")),
			fcs(""),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.rank1"), getRank(hero) == 0),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.rank2"), getRank(hero) == 1),
			fcs(I18n.get("abilities.reignofnether.vanguard_charge.rank3"), getRank(hero) == 2)
		);
	}
	
	@Override
	public void use(Level level, Unit unitUsing, LivingEntity targetEntity) {
		if (targetEntity == unitUsing)
			return;
		((NecromancerUnit) unitUsing).getCastPhantomGoal().setAbility(this);
		((NecromancerUnit) unitUsing).getCastPhantomGoal().setTarget(targetEntity);
	}
	
	@Override
	public void use(Level level, Unit unitUsing, BlockPos targetBp) {
		((NecromancerUnit) unitUsing).getCastPhantomGoal().setAbility(this);
		((NecromancerUnit) unitUsing).getCastPhantomGoal().setTarget(targetBp);
	}
}
