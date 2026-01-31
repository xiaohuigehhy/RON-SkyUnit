package net.xiaohuige_hhy.skyunit.mixin.building.buildings.placements;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingBlock;
import com.solegendary.reignofnether.building.buildings.placements.LibraryPlacement;
import com.solegendary.reignofnether.building.buildings.placements.ProductionPlacement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.xiaohuige_hhy.skyunit.ability.abilities.PromoteIllusioner;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(LibraryPlacement.class)
public abstract class LibraryPlacementMixin extends ProductionPlacement {
	public LibraryPlacementMixin(Building building, Level level, BlockPos originPos, Rotation rotation, String ownerName, ArrayList<BuildingBlock> blocks, boolean isCapitol) {
		super(building, level, originPos, rotation, ownerName, blocks, isCapitol);
	}
	
	@Shadow
	public abstract void tick(Level tickLevel);
	
	@Inject(method = "tick", at = @At("HEAD"), remap = false)
	private void updateIllusionerCharge(Level tickLevel, CallbackInfo ci) {
		if (this.getBuilding().getUpgradeLevel(this) == 2) {
			for (Ability ability : this.getAbilities()) {
				if (ability instanceof PromoteIllusioner promoteIllusioner) {
					int maxCharges = promoteIllusioner.upDateCharges(this);
					this.setCharges(promoteIllusioner, maxCharges);
				}
			}
		}
	}
}
