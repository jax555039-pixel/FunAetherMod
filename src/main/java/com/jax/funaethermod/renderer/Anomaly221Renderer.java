package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.Anomaly221Model;
import com.jax.funaethermod.entity.Anomaly221Entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class Anomaly221Renderer
        extends MobRenderer<
                Anomaly221Entity,
                Anomaly221Model<Anomaly221Entity>
        > {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/anomaly_221.png"
            );

    public Anomaly221Renderer(
            EntityRendererProvider.Context context
    ) {
        super(
                context,
                new Anomaly221Model<>(
                        context.bakeLayer(
                                Anomaly221Model.LAYER_LOCATION
                        )
                ),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            Anomaly221Entity entity
    ) {
        return TEXTURE;
    }
}