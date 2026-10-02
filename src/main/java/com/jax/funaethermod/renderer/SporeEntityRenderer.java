package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;

import com.jax.funaethermod.client.model.SporeEntityModel;
import com.jax.funaethermod.entity.SporeEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SporeEntityRenderer extends MobRenderer<SporeEntity, SporeEntityModel<SporeEntity>> {

    

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/spore_texture.png"
            );

    public SporeEntityRenderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new SporeEntityModel<>(
                        context.bakeLayer(
                                SporeEntityModel.LAYER_LOCATION
                        )
                ),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            SporeEntity entity
    ) {
        return TEXTURE;
    }
}