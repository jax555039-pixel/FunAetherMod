package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.TrickModel;
import com.jax.funaethermod.entity.TrickEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TrickRenderer
        extends MobRenderer<TrickEntity, TrickModel<TrickEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/trick.png"
            );

    public TrickRenderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new TrickModel<>(
                        context.bakeLayer(
                                TrickModel.LAYER_LOCATION
                        )
                ),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            TrickEntity entity
    ) {
        return TEXTURE;
    }
}