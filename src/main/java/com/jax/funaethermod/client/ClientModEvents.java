package com.jax.funaethermod.client;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.client.model.Entity2020Model;
import com.jax.funaethermod.client.model.RealEntityModel;
import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.renderer.Entity2020Renderer;
import com.jax.funaethermod.renderer.RealEntityRenderer;
import com.jax.funaethermod.renderer.RealObserveEntityRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = FunAetherMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(
                ModEntities.REAL.get(),
                RealEntityRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.REAL_OBSERVE.get(),
                RealObserveEntityRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.ENTITY2020.get(),
                Entity2020Renderer::new
        );
    }


    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(
                RealEntityModel.LAYER_LOCATION,
                RealEntityModel::createBodyLayer
        );

        event.registerLayerDefinition(
                Entity2020Model.LAYER_LOCATION,
                Entity2020Model::createBodyLayer
        );
    }
}