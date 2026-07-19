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
                    "textures/entity/fake_entity.png"
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

/*
=========================================================
                 LEARNING CORNER
=========================================================

This class tells Minecraft how to draw the FakeEntity.

TEXTURE
-------
Points to the texture file inside:

assets/funaethermod/textures/entity/fake_entity.png

Constructor
-----------
Creates the renderer.

context.bakeLayer(...)
loads the model that Blockbench exported.

0.5F
----
This is the entity's shadow size.

getTextureLocation()
--------------------
Whenever Minecraft renders the Fake, it asks:

"What texture should I use?"

We return fake_entity.png.
*/