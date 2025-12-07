package net.xiaohuige_hhy.skyunit.nether;

import com.mojang.datafixers.util.Pair;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.building.BuildingPlacement;
import com.solegendary.reignofnether.building.NetherConvertingBuilding;
import com.solegendary.reignofnether.building.NetherZone;
import com.solegendary.reignofnether.research.ResearchServerEvents;
import com.solegendary.reignofnether.util.MiscUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitBlockRegistrar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetherLavaRiverServerEvent {

	public static final int VISIBLE_BORDER_ADJ = 2;
	public static final ArrayList<NetherZone> availableNetherZones = new ArrayList<>();
	private static final int NETHER_LAVA_RIVER_SOURCES_UPDATE_TICKS_MAX = 50;
	public static ArrayList<Pair<BlockPos, Integer>> netherLavaRiverSourceOrigins = new ArrayList<>();
	public static Map<BlockPos, BlockState> netherLavaRiverBlocks = new HashMap<>();
	//	public static Map<BlockPos, BlockState> prenetherLavaRiverBlocks = new HashMap<>();
	private static int netherLavaRiverSourcesUpdateTicks = NETHER_LAVA_RIVER_SOURCES_UPDATE_TICKS_MAX;

	public static void setNetherLavaRiverSources() {

		netherLavaRiverSourcesUpdateTicks -= 1;
		if (netherLavaRiverSourcesUpdateTicks <= 0) {
			netherLavaRiverSourcesUpdateTicks = NETHER_LAVA_RIVER_SOURCES_UPDATE_TICKS_MAX;

			netherLavaRiverSourceOrigins.clear();
			availableNetherZones.clear();

			for (BuildingPlacement building : BuildingClientEvents.getBuildings()) {
				if (!ResearchServerEvents.playerHasResearch(building.ownerName, SkyUnitProductionItems.RESEARCH_NARROW_LAVA_RIVER)) {
					continue;
				}

				if (!building.isExploredClientside || !(building instanceof NetherConvertingBuilding ncb)) {
					continue;
				}
//				netherLavaRiverSourceOrigins.add(new Pair<>(building.centrePos, (int) ncb.getMaxRange() - VISIBLE_BORDER_ADJ));
//				availableNetherZones.add(ncb.getZone());
			}
		}
	}

	public static void changeNetherLavaRiver(@NotNull Level level) {
		if (level.isClientSide)
			return;


		Set<BlockPos> currentPositions = new HashSet<>();
		for (NetherZone netherZone : availableNetherZones) {
			if (netherZone != null && !netherZone.isRestoring())
				currentPositions.addAll(getRangeIndicatorCircleBlocks(netherZone.getOrigin(), (int) netherZone.getMaxRange(), level));
		}

//		currentPositions.forEach(bp -> {
//			if (!netherLavaRiverBlocks.containsKey(bp) &&
//				!level.getBlockState(bp).is(Blocks.WHITE_WOOL)) {
//				if (netherLavaRiverBlocks.containsKey(bp))
//					netherLavaRiverBlocks.put(bp, netherLavaRiverBlocks.get(bp));
//				else
//					netherLavaRiverBlocks.put(bp, level.getBlockState(bp));
//			}
//		});
//
//		netherLavaRiverBlocks.keySet().forEach(bp ->
//			level.setBlockAndUpdate(bp, Blocks.WHITE_WOOL.defaultBlockState()));
//
//		Iterator<Map.Entry<BlockPos, BlockState>> iterator = prenetherLavaRiverBlocks.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Map.Entry<BlockPos, BlockState> entry = iterator.next();
//			BlockPos bp = entry.getKey();
//			if (!currentPositions.contains(bp)) {
//				level.setBlockAndUpdate(bp, entry.getValue());
//				iterator.remove();
//			}
//		}
//
//		prenetherLavaRiverBlocks.putAll(netherLavaRiverBlocks);
		if (netherLavaRiverBlocks.size() >= currentPositions.size()) {
			Map<BlockPos, BlockState> lavaRiverBlocksToRemove = new HashMap<>(netherLavaRiverBlocks);
			lavaRiverBlocksToRemove.keySet().removeAll(currentPositions);
			lavaRiverBlocksToRemove.forEach(
				(bp, bs) -> {
					netherLavaRiverBlocks.remove(bp);
					level.setBlockAndUpdate(bp, bs);
				}
			);
		}
		currentPositions.forEach(
			bp -> {
				netherLavaRiverBlocks.put(bp, level.getBlockState(bp));
				level.setBlockAndUpdate(bp, SkyUnitBlockRegistrar.LAVA_RIVER_BLOCK.get().defaultBlockState());
			}
		);
	}

//	public static BlockState getOverworldBlock(Level level, BlockState netherBs) {
//		try {
//			if (!netherBs.isAir()) {
//				for (Map.Entry<Block, List<Block>> entrySet : NetherBlocks.MAPPINGS.entrySet()) {
//					if (netherBs.getBlock() == Blocks.OBSIDIAN)
//						return Blocks.WATER.defaultBlockState();
//					else if (netherBs.getBlock() == Blocks.NETHERRACK)
//						return Blocks.DIRT.defaultBlockState();
//					else if (entrySet.getKey().getName().getString().equals(netherBs.getBlock().getName().getString()))
//						return entrySet.getValue().get(0).defaultBlockState();
//				}
//			}
//		} catch (NullPointerException e) {
//			return null;
//		}
//		return null;
//	}

	public static @NotNull Set<BlockPos> getRangeIndicatorCircleBlocks(BlockPos centrePos, int radius, Level level) {
		if (radius <= 0)
			return Set.of();

		ArrayList<BlockPos> bps = new ArrayList<>();

		Set<BlockPos> netherLavaRiverCircleBps;
		netherLavaRiverCircleBps = MiscUtil.CircleUtil.getCircleWithCulledOverlaps(centrePos, radius, netherLavaRiverSourceOrigins);
		for (BlockPos bp : netherLavaRiverCircleBps) {
			for (int i = 0; i < 3; i++) {
				int x = bp.getX();
				int z = bp.getZ();
				if (i == 1)
					x += 1;
				else if (i == 2)
					z += 1;

				int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1;
				BlockPos topBp = new BlockPos(x, groundY, z);
				bps.add(topBp);

				int y = 1;
				if (level.getBlockState(topBp).getBlock() instanceof LeavesBlock) {
					BlockPos bottomBp;
					BlockState bs;
					do {
						bottomBp = topBp.offset(0, -y, 0);
						bs = level.getBlockState(bottomBp);
						y += 1;
					} while (y < 30 && (bs.getBlock() instanceof LeavesBlock || !bs.isSolid()));
					if (!level.getBlockState(bottomBp.above()).isSolid())
						bps.add(bottomBp);
				}
			}
		}
		return new HashSet<>(bps);
	}


}
