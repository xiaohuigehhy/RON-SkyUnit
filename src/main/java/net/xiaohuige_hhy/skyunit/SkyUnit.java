package net.xiaohuige_hhy.skyunit;

import com.mojang.logging.LogUtils;
import com.solegendary.reignofnether.resources.ResourceCosts;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import net.xiaohuige_hhy.skyunit.building.SkyUnitBuildings;
import net.xiaohuige_hhy.skyunit.building.production.SkyUnitProductionItems;
import net.xiaohuige_hhy.skyunit.config.SkyUnitCommonConfigs;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitBlockRegistrar;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitEntityRegistrar;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitItemRegistrar;

import org.slf4j.Logger;

@Mod(SkyUnit.MOD_ID)
public class SkyUnit {
	
	public static final String MOD_ID = "skyunit";
	public static final String VERSION_STRING = "0.4.1";
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public SkyUnit(FMLJavaModLoadingContext mlctx) {
		SkyUnitItemRegistrar.init(mlctx);
		SkyUnitBlockRegistrar.init(mlctx);
		SkyUnitEntityRegistrar.init(mlctx);
		SkyUnitProductionItems.init();
		SkyUnitBuildings.init();
		SkyUnitConfigs.register();
		
		IEventBus bus = mlctx.getModEventBus();
		bus.addListener(SkyUnit::init);
		mlctx.registerConfig(ModConfig.Type.COMMON, SkyUnitCommonConfigs.SPEC, "skyunit-common-" + VERSION_STRING + ".toml");
		mlctx.registerExtensionPoint(
			IExtensionPoint.DisplayTest.class,
			() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true)
		);
	}
	
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		ResourceCosts.deferredLoadResourceCosts();
	}
	
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}
	
	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			
			LOGGER.info("HELLO FROM CLIENT SETUP");
			LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
		}
	}
}
