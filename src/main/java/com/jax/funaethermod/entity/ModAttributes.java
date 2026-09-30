package com.jax.funaethermod.entity;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.entity.Entity2020Entity;
import com.jax.funaethermod.entity.EntitySpawnerEntity;
import com.jax.funaethermod.entity.TrickEntity;
import com.jax.funaethermod.entity.FakeAggroEntity;
import com.jax.funaethermod.entity.Anomaly221Entity;
import com.jax.funaethermod.entity.PoorBoyEntity;
import com.jax.funaethermod.entity.FakeEntity;
import com.jax.funaethermod.entity.RealObserveEntity;
import com.jax.funaethermod.entity.RealEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.jax.funaethermod.registry.ModEntities;

@Mod.EventBusSubscriber(modid = FunAetherMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

        event.put(
                ModEntities.ENTITY2020.get(),
                Entity2020Entity.createAttributes().build()
        );

        event.put(
                ModEntities.ENTITY2020_ATTACK.get(),
                com.jax.funaethermod.entity.Entity2020AttackEntity.createAttributes().build()
        );

        event.put(
        ModEntities.ENTITY_SPAWNER.get(),
        EntitySpawnerEntity.createAttributes().build()
        );

        event.put(
        ModEntities.TRICK.get(),
        TrickEntity.createAttributes().build()
        );

        event.put(
        ModEntities.FAKE_AGGRO.get(),
        FakeAggroEntity.createAttributes().build()
        );

        event.put(
        ModEntities.ANOMALY_221.get(),
        Anomaly221Entity.createAttributes().build()
        );

        event.put(
                ModEntities.REAL.get(),
                RealEntity.createAttributes().build()
        );

        event.put(
                ModEntities.REAL_OBSERVE.get(),
                RealObserveEntity.createAttributes().build()
        );

        event.put(
                ModEntities.FAKE.get(),
                FakeEntity.createAttributes().build()
        );

        event.put(
                ModEntities.POORBOY.get(),
                PoorBoyEntity.createAttributes().build()
        );

    }
}