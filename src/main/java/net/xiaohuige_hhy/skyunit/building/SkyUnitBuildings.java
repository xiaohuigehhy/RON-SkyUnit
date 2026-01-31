package net.xiaohuige_hhy.skyunit.building;

import com.solegendary.reignofnether.api.ReignOfNetherRegistries;
import com.solegendary.reignofnether.building.Building;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.building.buildings.skyunit.SkyTown;

public class SkyUnitBuildings {
    public static final SkyTown SKYTOWN = register(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "skytown"), new SkyTown());

    private static <T extends Building> T register(ResourceLocation id, T building) {
        return Registry.register(ReignOfNetherRegistries.BUILDING, id, building);
    }

    public static void init() {}
}
