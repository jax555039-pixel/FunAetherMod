package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;

import com.jax.funaethermod.client.model.HollowEntityModel;
import com.jax.funaethermod.entity.HollowEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HollowEntityRenderer extends MobRenderer<HollowEntity, HollowEntityModel<HollowEntity>> {

    

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/hollow.png"
            );

    public HollowEntityRenderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new HollowEntityModel<>(
                        context.bakeLayer(
                                HollowEntityModel.LAYER_LOCATION
                        )
                ),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            HollowEntity entity
    ) {
        return TEXTURE;
    }
}