package net.xiaohuige_hhy.skyunit.unit.modelling.renderers;


import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xiaohuige_hhy.skyunit.unit.units.monsters.SkeletonHorseSummonUnit;

import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SkeletonHorseSummonRenderer extends MobRenderer<SkeletonHorseSummonUnit, HorseModel<SkeletonHorseSummonUnit>> {
	
	private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/horse/horse_skeleton.png");
	
	public SkeletonHorseSummonRenderer(EntityRendererProvider.Context pContext) {
		super(pContext, new HorseModel<>(pContext.bakeLayer(ModelLayers.SKELETON_HORSE)), 1.0F);
	}
	
	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull SkeletonHorseSummonUnit pEntity) {
		return TEXTURE_LOCATION;
	}
}