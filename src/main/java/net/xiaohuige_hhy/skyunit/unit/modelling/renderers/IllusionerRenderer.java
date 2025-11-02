package net.xiaohuige_hhy.skyunit.unit.modelling.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.solegendary.reignofnether.unit.modelling.models.VillagerUnitModel;
import com.solegendary.reignofnether.unit.modelling.renderers.AbstractVillagerUnitRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xiaohuige_hhy.skyunit.unit.units.villiagers.IllusionerUnit;

import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class IllusionerRenderer extends AbstractVillagerUnitRenderer<IllusionerUnit> {
	private static final ResourceLocation ILLUSIONER = new ResourceLocation("textures/entity/illager/illusioner.png");
	
	public IllusionerRenderer(EntityRendererProvider.Context p_174186_) {
		super(p_174186_, new VillagerUnitModel<>(p_174186_.bakeLayer(VillagerUnitModel.LAYER_LOCATION)), 0.5F);
		this.addLayer(new ItemInHandLayer<>(this, p_174186_.getItemInHandRenderer()) {
			public void render(@NotNull PoseStack p_114989_, @NotNull MultiBufferSource p_114990_, int p_114991_, @NotNull IllusionerUnit p_114992_, float p_114993_, float p_114994_, float p_114995_, float p_114996_, float p_114997_, float p_114998_) {
				if (p_114992_.isCastingSpell() || p_114992_.isAggressive()) {
					super.render(p_114989_, p_114990_, p_114991_, p_114992_, p_114993_, p_114994_, p_114995_, p_114996_, p_114997_, p_114998_);
				}
				
			}
		});
		this.model.getHat().visible = true;
	}
	
	public @NotNull ResourceLocation getTextureLocation(@NotNull IllusionerUnit pEntity) {
		return ILLUSIONER;
	}
	
	public void render(IllusionerUnit pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
		if (pEntity.isInvisible()) {
			Vec3[] avec3 = pEntity.getIllusionOffsets(pPartialTicks);
			float f = this.getBob(pEntity, pPartialTicks);
			
			for (int i = 0; i < avec3.length; ++i) {
				pPoseStack.pushPose();
				pPoseStack.translate(avec3[i].x + (double) Mth.cos((float) i + f * 0.5F) * 0.025D, avec3[i].y + (double) Mth.cos((float) i + f * 0.75F) * 0.0125D, avec3[i].z + (double) Mth.cos((float) i + f * 0.7F) * 0.025D);
				super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
				pPoseStack.popPose();
			}
		} else {
			super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
		}
		
	}
	
	protected boolean isBodyVisible(@NotNull IllusionerUnit pLivingEntity) {
		return true;
	}
}