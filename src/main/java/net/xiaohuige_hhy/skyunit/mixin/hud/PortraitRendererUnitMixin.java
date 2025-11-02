package net.xiaohuige_hhy.skyunit.mixin.hud;

import com.solegendary.reignofnether.hud.PortraitRendererUnit;
import com.solegendary.reignofnether.unit.units.villagers.PillagerUnit;
import com.solegendary.reignofnether.unit.units.villagers.VindicatorUnit;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;

import org.apache.commons.lang3.text.WordUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PortraitRendererUnit.class)
public class PortraitRendererUnitMixin {
	
	@Redirect(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lorg/apache/commons/lang3/text/WordUtils;capitalize(Ljava/lang/String;)Ljava/lang/String;"
		),
		remap = false
	)
	private String modifyCapitalizedName(String originalName, GuiGraphics guiGraphics, String name1, int x, int y, LivingEntity entity) {
		String name = WordUtils.capitalize(originalName);
		
		if (entity instanceof PillagerUnit pUnit && pUnit.getEnchant() == Enchantments.BLAST_PROTECTION) {
			name += " (MS & QC)";
		}
		if (entity instanceof VindicatorUnit pUnit && pUnit.getEnchant() == Enchantments.BLAST_PROTECTION) {
			name += " (哇，还有SM)";
		}
		
		return name;
	}
	
}
