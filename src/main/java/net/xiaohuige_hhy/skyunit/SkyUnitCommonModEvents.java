package net.xiaohuige_hhy.skyunit;


import com.solegendary.reignofnether.registrars.PacketHandler;

import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.PhantomRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitBlockRegistrar;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitEntityRegistrar;
import net.xiaohuige_hhy.skyunit.registars.SkyUnitItemRegistrar;
import net.xiaohuige_hhy.skyunit.unit.modelling.renderers.IllusionerRenderer;
import net.xiaohuige_hhy.skyunit.unit.modelling.renderers.SkeletonHorseSummonRenderer;
import net.xiaohuige_hhy.skyunit.unit.units.monsters.SkeletonHorseSummonUnit;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.BeeUnit;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.ParrotUnit;
import net.xiaohuige_hhy.skyunit.unit.units.skyunit.PhantomUnit;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerUnit;

@Mod.EventBusSubscriber(modid = SkyUnit.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkyUnitCommonModEvents {
	
	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(PacketHandler::init);
	}
	
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		evt.registerEntityRenderer(SkyUnitEntityRegistrar.SKELETON_HORSE_SUMMON_UNIT.get(), SkeletonHorseSummonRenderer::new);
		evt.registerEntityRenderer(SkyUnitEntityRegistrar.ILLUSIONER_UNIT.get(), IllusionerRenderer::new);
		evt.registerEntityRenderer(SkyUnitEntityRegistrar.PARROT_UNIT.get(), ParrotRenderer::new);
		evt.registerEntityRenderer(SkyUnitEntityRegistrar.BEE_UNIT.get(), BeeRenderer::new);
		evt.registerEntityRenderer(SkyUnitEntityRegistrar.PHANTOM_UNIT.get(), PhantomRenderer::new);
	}
	
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent evt) {
		evt.put(SkyUnitEntityRegistrar.SKELETON_HORSE_SUMMON_UNIT.get(), SkeletonHorseSummonUnit.createAttributes().build());
		evt.put(SkyUnitEntityRegistrar.ILLUSIONER_UNIT.get(), IllusionerUnit.createAttributes().build());
		evt.put(SkyUnitEntityRegistrar.PARROT_UNIT.get(), ParrotUnit.createAttributes().build());
		evt.put(SkyUnitEntityRegistrar.BEE_UNIT.get(), BeeUnit.createAttributes().build());
		evt.put(SkyUnitEntityRegistrar.PHANTOM_UNIT.get(), PhantomUnit.createAttributes().build());
	}
	
	@SubscribeEvent
	public static void creativeTabSetup(BuildCreativeModeTabContentsEvent event) {
		if (BuiltInRegistries.CREATIVE_MODE_TAB.getKey(event.getTab()) == CreativeModeTabs.BUILDING_BLOCKS.location()) {
			for (Item item : SkyUnitBlockRegistrar.blockItems.get(CreativeModeTabs.BUILDING_BLOCKS)) {
				event.accept(item);
			}
		}
		if (BuiltInRegistries.CREATIVE_MODE_TAB.getKey(event.getTab()) == CreativeModeTabs.SPAWN_EGGS.location()) {
			event.accept(SkyUnitItemRegistrar.SKELETON_HORSE_SUMMON_UNIT);
			event.accept(SkyUnitItemRegistrar.ILLUSIONER_UNIT);
			event.accept(SkyUnitItemRegistrar.PARROT_UNIT);
			event.accept(SkyUnitItemRegistrar.BEE_UNIT);
			event.accept(SkyUnitItemRegistrar.PHANTOM_UNIT);
		}
	}
}
