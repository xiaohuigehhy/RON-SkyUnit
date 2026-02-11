package net.xiaohuige_hhy.skyunit.mixin.hud;

import com.solegendary.reignofnether.hud.PortraitRendererUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;

import net.minecraft.client.gui.GuiGraphics;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.PhantomUnit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PortraitRendererUnit.class)
public class PortraitRendererUnitMixin {
	
//	@ModifyVariable(
//		method = "renderStats",
//		at = @At(
//			value = "INVOKE",
//			target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z",
//			ordinal = 0
//		),
//		ordinal = 7,
//		remap = false
//	)
//	private String addPhantomAttackBonus(String atkStr, GuiGraphics guiGraphics, String name, int x, int y, int mouseX, int mouseY, Unit unit) {
//		if (unit instanceof PhantomUnit) {
//			return atkStr + "+" + ((PhantomUnit) unit).getUnitAdditionDamage();
//		}
//		return atkStr;
//	}
}
