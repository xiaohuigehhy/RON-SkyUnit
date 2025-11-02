package net.xiaohuige_hhy.skyunit.ability.abilities;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.cursor.CursorClientEvents;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.villagers.RavagerUnit;
import com.solegendary.reignofnether.util.MyRenderer;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerUnit;

import java.util.List;

public class BlindnessSpellGoal extends Ability {
	
	public static final int CD_MAX_SECONDS = 9;
	public static final int RANGE = 16;
	private final IllusionerUnit illusionerUnit;
	
	public BlindnessSpellGoal(IllusionerUnit illusionerUnit) {
		super(UnitAction.TELEPORT, illusionerUnit.level(), CD_MAX_SECONDS * ResourceCost.TICKS_PER_SECOND, RANGE, 0, true);
		
		this.illusionerUnit = illusionerUnit;
		this.autocastEnableAction = UnitAction.SPIN_WEBS_AUTOCAST_ENABLE;
		this.autocastDisableAction = UnitAction.SPIN_WEBS_AUTOCAST_DISABLE;
	}
	
	@Override
	public AbilityButton getButton(Keybinding hotkey) {
		return new AbilityButton(
			"Blindness Spell Goal",
			new ResourceLocation("minecraft", "textures/mob_effect/blindness.png"),
			hotkey,
			() -> CursorClientEvents.getLeftClickAction() == UnitAction.TELEPORT || isAutocasting(),
			() -> false,
			() -> true,
			() -> CursorClientEvents.setLeftClickAction(UnitAction.TELEPORT),
			this::toggleAutocast,
			List.of(
				FormattedCharSequence.forward(I18n.get("abilities.skyunit.blindness_spell_goal"), Style.EMPTY),
				FormattedCharSequence.forward(
					I18n.get("abilities.reignofnether.blindness_spell_goal", RavagerUnit.ROAR_DAMAGE, CD_MAX_SECONDS)
						+ RavagerUnit.ROAR_RANGE,
					MyRenderer.iconStyle
				),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.blindness_spell_goal"), Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.blindness_spell_goal"), Style.EMPTY)
			),
			this
		);
	}
	
	@Override
	public void use(Level level, Unit unitUsing, LivingEntity livingEntity) {
		if (!isOffCooldown())
			return;
		illusionerUnit.getCastBlindnessSpellGoal().setAbility(this);
		illusionerUnit.getCastBlindnessSpellGoal().setTarget(livingEntity);
	}
	
}
