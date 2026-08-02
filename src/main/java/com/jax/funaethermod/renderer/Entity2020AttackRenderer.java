package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.Entity2020Model;
import com.jax.funaethermod.entity.Entity2020AttackEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class Entity2020AttackRenderer extends MobRenderer<Entity2020AttackEntity, Entity2020Model<Entity2020AttackEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/entity2020.png"
            );

    public Entity2020AttackRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new Entity2020Model<>(
                        context.bakeLayer(Entity2020Model.LAYER_LOCATION)
                ),
                0.0F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(Entity2020AttackEntity entity) {
        return TEXTURE;
    }
}