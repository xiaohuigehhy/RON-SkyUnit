package net.xiaohuige_hhy.skyunit.ability.abilities;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.modelling.models.VillagerUnitModel;
import com.solegendary.reignofnether.unit.units.villagers.RavagerUnit;
import com.solegendary.reignofnether.util.MyRenderer;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerUnit;

import java.util.List;

public class MirrorSpellGoal extends Ability {
	
	public static final int CD_MAX_SECONDS = 77;
	private final IllusionerUnit illusionerUnit;
	
	public MirrorSpellGoal(IllusionerUnit illusionerUnit) {
		super(UnitAction.ROAR, illusionerUnit.level(), CD_MAX_SECONDS * ResourceCost.TICKS_PER_SECOND, 0, 0, true, false);
		
		this.illusionerUnit = illusionerUnit;
		this.autocastEnableAction = UnitAction.SPIN_WEBS_AUTOCAST_ENABLE;
		this.autocastDisableAction = UnitAction.SPIN_WEBS_AUTOCAST_DISABLE;
	}
	
	@Override
	public AbilityButton getButton(Keybinding hotkey) {
		return new AbilityButton(
			"Mirror Spell Goal",
			new ResourceLocation("minecraft", "textures/mob_effect/invisibility.png"),
			hotkey,
			() -> {
				if (this.illusionerUnit.getCastMirrorSpellGoal() != null)
					return this.illusionerUnit.getCastMirrorSpellGoal().isCasting();
				return false;
			},
			() -> false,
			() -> true,
			() -> UnitClientEvents.sendUnitCommand(UnitAction.ROAR),
			null,
			List.of(
				FormattedCharSequence.forward(I18n.get("abilities.skyunit.mirror_spell_goal"), Style.EMPTY),
				FormattedCharSequence.forward(
					I18n.get("abilities.reignofnether.mirror_spell_goal", RavagerUnit.ROAR_DAMAGE, CD_MAX_SECONDS)
						+ RavagerUnit.ROAR_RANGE,
					MyRenderer.iconStyle
				),
				FormattedCharSequence.forward("", Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.mirror_spell_goal"), Style.EMPTY),
				FormattedCharSequence.forward(I18n.get("abilities.reignofnether.mirror_spell_goal"), Style.EMPTY)
			),
			this
		);
	}
	@Override
	public void use(Level level, Unit unitUsing, BlockPos bp) {
		illusionerUnit.getCastMirrorSpellGoal().setAbility(this);
		illusionerUnit.getCastMirrorSpellGoal().startCasting();
	}
	
}
