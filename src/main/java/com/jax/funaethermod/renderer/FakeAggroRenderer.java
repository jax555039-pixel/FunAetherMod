package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.FakeAggroModel;
import com.jax.funaethermod.entity.FakeAggroEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FakeAggroRenderer
        extends MobRenderer<FakeAggroEntity, FakeAggroModel<FakeAggroEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/fakeaggro.png"
            );

    public FakeAggroRenderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new FakeAggroModel<>(
                        context.bakeLayer(
                                FakeAggroModel.LAYER_LOCATION
                        )
                ),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            FakeAggroEntity entity
    ) {
        return TEXTURE;
    }
}

