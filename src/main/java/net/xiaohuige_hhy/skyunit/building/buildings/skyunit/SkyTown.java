package net.xiaohuige_hhy.skyunit.building.buildings.skyunit;

import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.ability.abilities.BackToWorkBuilding;
import com.solegendary.reignofnether.ability.abilities.CallToArmsBuilding;
import com.solegendary.reignofnether.building.production.ProductionBuilding;
import com.solegendary.reignofnether.building.production.ProductionItems;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.util.Faction;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.xiaohuige_hhy.skyunit.resources.SkyUnitResourceCosts;

public class SkyTown extends ProductionBuilding {

	public final static String buildingName = "Sky Town";
	public final static String structureName = "sky_town";
	public final static ResourceCost cost = SkyUnitResourceCosts.SKY_TOWN;

	public SkyTown() {
		super(structureName, cost, true);
		this.name = buildingName;
		this.portraitBlock = Blocks.WHITE_WOOL;
		this.icon = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/white_wool.png");

		this.buildTimeModifier = 0.331f; // 60s total build time with 3 parrots
		this.canAcceptResources = true;

		this.startingBlockTypes.add(Blocks.STONE_BLOCK);
		this.startingBlockTypes.add(Blocks.GRASS_BLOCK);

		this.productions.add(ProductionItems.PARROT, Keybindings.keyQ);
	}

	@Override
	public Faction getFaction() {
		return Faction.NONE;
	}

    @Override
    public BuildingPlacement createBuildingPlacement(Level level, BlockPos pos, Rotation rotation, String ownerName) {
        return new DamageWorldProductionPlacement(this, level, pos, rotation, ownerName, getAbsoluteBlockData(getRelativeBlockData(level), level, pos, rotation), true, MILITIA_RANGE, true, false);
    }

	public BuildingPlaceButton getBuildButton(Keybinding hotkey) {
        ResourceLocation key = ReignOfNetherRegistries.BUILDING.getKey(this);
        String name = I18n.get("buildings." + getFaction().name().toLowerCase() + "." + key.getNamespace() + "." + key.getPath());
        return new BuildingPlaceButton(
               name,
                ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/white_wool.png"),
                hotkey,
                () -> BuildingClientEvents.getBuildingToPlace() == SkyUnitBuildings.SkyTown,
                () -> false,
                () -> true,
                List.of(
                        FormattedCharSequence.forward(I18n.get("buildings.skyunit.reignofnether.skytown"), Style.EMPTY.withBold(true)),
                        ResourceCosts.getFormattedCost(cost),
                        ResourceCosts.getFormattedPop(cost),
                        FormattedCharSequence.forward("", Style.EMPTY),
                        FormattedCharSequence.forward(I18n.get("buildings.skyunit.reignofnether.skytown.tooltip1"), Style.EMPTY)
                ),
                this
        );
    }
}
