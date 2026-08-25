package com.jax.funaethermod.client;

import com.jax.funaethermod.FunAetherMod;

import com.jax.funaethermod.client.model.Entity2020Model;
import com.jax.funaethermod.client.model.RealEntityModel;
import com.jax.funaethermod.client.model.FakeEntityModel;
import com.jax.funaethermod.client.model.PoorBoyModel;
import com.jax.funaethermod.client.model.EntitySpawnerModel;
import com.jax.funaethermod.client.model.TrickModel;
import com.jax.funaethermod.client.model.FakeAggroModel;

import com.jax.funaethermod.registry.ModEntities;

import com.jax.funaethermod.renderer.Entity2020Renderer;
import com.jax.funaethermod.renderer.Entity2020AttackRenderer;
import com.jax.funaethermod.renderer.RealEntityRenderer;
import com.jax.funaethermod.renderer.RealObserveEntityRenderer;
import com.jax.funaethermod.renderer.FakeEntityRenderer;
import com.jax.funaethermod.renderer.PoorBoyRenderer;
import com.jax.funaethermod.renderer.EntitySpawnerRenderer;
import com.jax.funaethermod.renderer.TrickRenderer;
import com.jax.funaethermod.renderer.FakeAggroRenderer;

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

        // Attack version
        event.registerEntityRenderer(
                ModEntities.ENTITY2020_ATTACK.get(),
                Entity2020AttackRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.FAKE.get(),
                FakeEntityRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.POORBOY.get(),
                PoorBoyRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.ENTITY_SPAWNER.get(),
                EntitySpawnerRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.TRICK.get(),
                TrickRenderer::new
        );

        event.registerEntityRenderer(
                ModEntities.FAKE_AGGRO.get(),
                FakeAggroRenderer::new
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

        event.registerLayerDefinition(
                FakeEntityModel.LAYER_LOCATION,
                FakeEntityModel::createBodyLayer
        );

        event.registerLayerDefinition(
                PoorBoyModel.LAYER_LOCATION,
                PoorBoyModel::createBodyLayer
        );

        event.registerLayerDefinition(
                EntitySpawnerModel.LAYER_LOCATION,
                EntitySpawnerModel::createBodyLayer
        );

        event.registerLayerDefinition(
                TrickModel.LAYER_LOCATION,
                TrickModel::createBodyLayer
        );

        event.registerLayerDefinition(
                FakeAggroModel.LAYER_LOCATION,
                TrickModel::createBodyLayer
        );

    }
}