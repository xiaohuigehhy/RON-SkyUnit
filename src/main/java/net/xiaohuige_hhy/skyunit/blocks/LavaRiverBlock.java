package net.xiaohuige_hhy.skyunit.blocks;

import com.solegendary.reignofnether.faction.Faction;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.piglins.GruntUnit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

public class LavaRiverBlock extends Block {
	
	public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
	
	public LavaRiverBlock(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
		return RenderShape.MODEL;
	}
	
	@Override
	public boolean canBeReplaced(@NotNull BlockState pState, @NotNull Fluid pFluid) {
		return false;
	}
	
	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
		return SHAPE;
	}
	
	public @NotNull VoxelShape getVisualShape(@NotNull BlockState pState, @NotNull BlockGetter pReader, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
		return Shapes.block();
	}
	
	@Override
	public boolean useShapeForLightOcclusion(@NotNull BlockState pState) {
		return true;
	}
	
	public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
		boolean isPiglinFaction = pEntity instanceof Unit unit && unit.getFaction() == Faction.PIGLINS;
		boolean isDamageTick = pEntity.tickCount % 10 == 0;
		
		if (!pEntity.isSteppingCarefully() &&
			pEntity instanceof LivingEntity &&
			!(pEntity instanceof GruntUnit) &&
			!EnchantmentHelper.hasFrostWalker((LivingEntity) pEntity) &&
			!isPiglinFaction && isDamageTick) {
			pEntity.lavaHurt();
		}
		
		super.entityInside(pState, pLevel, pPos, pEntity);
	}
}
