package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.RealEntityModel;
import com.jax.funaethermod.entity.RealEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RealEntityRenderer extends MobRenderer<RealEntity, RealEntityModel<RealEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(FunAetherMod.MODID, "textures/entity/real_entity.png");

    public RealEntityRenderer(EntityRendererProvider.Context context) {
        super(context,
                new RealEntityModel<>(context.bakeLayer(RealEntityModel.LAYER_LOCATION)),
                0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(RealEntity entity) {
        return TEXTURE;
    }
}