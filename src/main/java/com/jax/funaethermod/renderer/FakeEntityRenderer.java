package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.FakeEntityModel;
import com.jax.funaethermod.entity.FakeEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FakeEntityRenderer extends MobRenderer<FakeEntity, FakeEntityModel<FakeEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/fake.png"
            );

    public FakeEntityRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new FakeEntityModel<>(context.bakeLayer(FakeEntityModel.LAYER_LOCATION)),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(FakeEntity entity) {
        return TEXTURE;
    }
}