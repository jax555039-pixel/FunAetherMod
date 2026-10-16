package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.CorruptEntityModel;
import com.jax.funaethermod.entity.CorruptEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CorruptEntityRenderer
        extends MobRenderer<
                CorruptEntity,
                CorruptEntityModel<CorruptEntity>
        > {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/corrupt_entity.png"
            );

    public CorruptEntityRenderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new CorruptEntityModel<>(
                        context.bakeLayer(
                                CorruptEntityModel.LAYER_LOCATION
                        )
                ),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            CorruptEntity entity
    ) {
        return TEXTURE;
    }
}