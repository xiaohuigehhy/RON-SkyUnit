package net.xiaohuige_hhy.skyunit.registars;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.xiaohuige_hhy.skyunit.SkyUnit;
import net.xiaohuige_hhy.skyunit.unit.units.monsters.SkeletonHorseSummonUnit;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerProd;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerUnit;

public class SkyUnitEntityRegistrar {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SkyUnit.MOD_ID);
	private static final int UNIT_CLIENT_TRACKING_RANGE = 100;
	public static final RegistryObject<EntityType<SkeletonHorseSummonUnit>> SKELETON_HORSE_SUMMON_UNIT = ENTITIES.register("skeleton_horse_summon_unit",
		() -> EntityType.Builder.of(SkeletonHorseSummonUnit::new, MobCategory.MONSTER)
			.sized(EntityType.SKELETON_HORSE.getWidth(), EntityType.SKELETON_HORSE.getHeight())
			.clientTrackingRange(UNIT_CLIENT_TRACKING_RANGE)
			.build(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "skeleton_horse_summon_unit").toString()));
	public static final RegistryObject<EntityType<IllusionerUnit>> ILLUSIONER_UNIT = ENTITIES.register("illusioner_unit",
		() -> EntityType.Builder.of(IllusionerUnit::new, MobCategory.CREATURE)
			.sized(EntityType.ILLUSIONER.getWidth(), EntityType.ILLUSIONER.getHeight())
			.clientTrackingRange(UNIT_CLIENT_TRACKING_RANGE)
			.build(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "illusioner_unit").toString()));
	public static final RegistryObject<EntityType<ParrotUnit>> PARROT_UNIT = ENTITIES.register("parrot_unit", 
		() -> EntityType.Builder.of(ParrotUnit::new, MobCategory.CREATURE)
			.sized(EntityType.PARROT.getWidth(), EntityType.PARROT.getHeight())
			.clientTrackingRange(UNIT_CLIENT_TRACKING_RANGE)
			.build(ResourceLocation.fromNamespaceAndPath(SkyUnit.MOD_ID, "parrot_unit").toString()));
	
	public static void init(FMLJavaModLoadingContext context) {
		ENTITIES.register(context.getModEventBus());
	}
	
	public static EntityType<? extends Mob> getEntityType(String unitName) {
		return switch (unitName) {
			case "Skeleton Horse" -> SkyUnitEntityRegistrar.SKELETON_HORSE_SUMMON_UNIT.get();
			case IllusionerProd.itemName -> SkyUnitEntityRegistrar.ILLUSIONER_UNIT.get();
			case ParrotProd.itemName -> SkyUnitEntityRegistrar.PARROT_UNIT.get();
			default -> null;
		};
	}
}
