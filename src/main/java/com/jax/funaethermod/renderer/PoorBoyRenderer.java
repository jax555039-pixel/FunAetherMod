package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.PoorBoyModel;
import com.jax.funaethermod.entity.PoorBoyEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PoorBoyRenderer extends MobRenderer<PoorBoyEntity, PoorBoyModel<PoorBoyEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/poorboy.png"
            );

    public PoorBoyRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new PoorBoyModel<>(context.bakeLayer(PoorBoyModel.LAYER_LOCATION)),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(PoorBoyEntity entity) {
        return TEXTURE;
    }
}