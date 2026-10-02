package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;

import com.jax.funaethermod.client.model.GluttonyEntityModel;
import com.jax.funaethermod.entity.GluttonyEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GluttonyEntityRenderer extends MobRenderer<GluttonyEntity, GluttonyEntityModel<GluttonyEntity>> {

    

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/gluttony.png"
            );

    public GluttonyEntityRenderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new GluttonyEntityModel<>(
                        context.bakeLayer(
                                GluttonyEntityModel.LAYER_LOCATION
                        )
                ),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            GluttonyEntity entity
    ) {
        return TEXTURE;
    }
}