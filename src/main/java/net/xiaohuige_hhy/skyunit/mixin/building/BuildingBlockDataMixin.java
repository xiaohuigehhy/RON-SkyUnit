package net.xiaohuige_hhy.skyunit.mixin.building;

import com.solegendary.reignofnether.building.BuildingBlockData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BuildingBlockData.class)
public class BuildingBlockDataMixin {
	
	@Inject(method = "getBuildingNbt", at = @At(value = "RETURN"), remap = false, cancellable = true)
	private static void addSkyUnitBuilding(String structureName, ResourceManager resManager, @NotNull CallbackInfoReturnable<CompoundTag> cir) {
		if (cir.getReturnValue() == null) {
			try {
				ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("skyunit", "structures/" + structureName + ".nbt");
				Optional<Resource> rs = resManager.getResource(rl);
				if (rs.isPresent()) cir.setReturnValue(NbtIo.readCompressed(rs.get().open()));
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
}
