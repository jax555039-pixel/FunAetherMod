package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.Entity2020Model;
import com.jax.funaethermod.entity.Entity2020Entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class Entity2020Renderer extends MobRenderer<Entity2020Entity, Entity2020Model<Entity2020Entity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/entity2020.png"
            );


    public Entity2020Renderer(EntityRendererProvider.Context context) {
        super(
                context,
                new Entity2020Model<>(
                        context.bakeLayer(Entity2020Model.LAYER_LOCATION)
                ),
                0.0F
        );
    }


    @Override
    public ResourceLocation getTextureLocation(Entity2020Entity entity) {
        return TEXTURE;
    }
}