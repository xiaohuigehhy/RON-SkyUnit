package net.xiaohuige_hhy.skyunit.mixin.building;

import com.solegendary.reignofnether.building.BuildingBlock;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BuildingBlock.class)
public abstract class BuildingBlockMixin {
	
	@Shadow
	private BlockState blockState;
	
	@Inject(method = "isPlaced", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, remap = false, cancellable = true)
	public void DontCheck(Level level, CallbackInfoReturnable<Boolean> cir, BlockState bs, boolean isMatchingWallBlock, Block block1, Block block2) {
		if ((block1 instanceof StairBlock && block2 instanceof StairBlock) ||
			(block1 instanceof FenceBlock && block2 instanceof FenceBlock) ||
			(block1 instanceof WallBlock && block2 instanceof WallBlock) ||
			(block1 instanceof IronBarsBlock && block2 instanceof IronBarsBlock))
			cir.setReturnValue(!this.blockState.isAir() && (bs == this.blockState || isMatchingWallBlock));
	}
	
}
