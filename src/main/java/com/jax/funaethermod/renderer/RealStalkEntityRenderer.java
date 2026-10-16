package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.RealEntityModel;
import com.jax.funaethermod.entity.RealStalkEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RealStalkEntityRenderer extends MobRenderer<RealStalkEntity, RealEntityModel<RealStalkEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(FunAetherMod.MODID, "textures/entity/real_entity.png");

    public RealStalkEntityRenderer(EntityRendererProvider.Context context) {
        super(context,
                new RealEntityModel<>(context.bakeLayer(RealEntityModel.LAYER_LOCATION)),
                0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(RealStalkEntity entity) {
        return TEXTURE;
    }
}