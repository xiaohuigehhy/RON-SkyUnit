package net.xiaohuige_hhy.skyunit.mixin.building.buildings.villagers;

import com.solegendary.reignofnether.building.BuildingBlock;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.buildings.villagers.Library;
import com.solegendary.reignofnether.building.production.ProductionBuilding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.resources.ResourceCost;

import net.minecraft.world.level.block.Blocks;
import net.xiaohuige_hhy.skyunit.ability.abilities.PromoteIllusioner;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Library.class)
public abstract class LibraryMixin extends ProductionBuilding {
	
	public LibraryMixin(String structureName, ResourceCost cost, boolean isCapitol) {
		super(structureName, cost, isCapitol);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"), remap = false)
	private void AddMysteriousLibrary(CallbackInfo ci) {
		this.abilities.add(new PromoteIllusioner(), Keybindings.keyL);
		this.productions.add(SkyUnitProductionItems.RESEARCH_MYSTERIOUS_LIBRARY, Keybindings.keyM);
	}
	
	@Inject(method = "getUpgradeLevel", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private void AddMysteriousLevel(@NotNull BuildingPlacement placement, CallbackInfoReturnable<Integer> cir) {
		for (BuildingBlock block : placement.getBlocks())
			if (block.getBlockState().getBlock() == Blocks.BEACON) {
				cir.setReturnValue(2);
				cir.cancel();
			}
	}
}
