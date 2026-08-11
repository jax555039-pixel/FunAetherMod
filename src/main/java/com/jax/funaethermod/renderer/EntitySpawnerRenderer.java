package com.jax.funaethermod.renderer;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.EntitySpawnerModel;
import com.jax.funaethermod.entity.EntitySpawnerEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EntitySpawnerRenderer extends MobRenderer<EntitySpawnerEntity, EntitySpawnerModel<EntitySpawnerEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "textures/entity/entity_spawner.png"
            );

    public EntitySpawnerRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new EntitySpawnerModel<>(context.bakeLayer(EntitySpawnerModel.LAYER_LOCATION)),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySpawnerEntity entity) {
        return TEXTURE;
    }
}