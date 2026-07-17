package com.jax.funaethermod.registry;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.entity.RealEntity;
import com.jax.funaethermod.entity.RealObserveEntity;
import com.jax.funaethermod.entity.Entity2020Entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    ForgeRegistries.ENTITY_TYPES,
                    FunAetherMod.MODID
            );


    public static final RegistryObject<EntityType<RealEntity>> REAL =
            ENTITY_TYPES.register(
                    "real",
                    () -> EntityType.Builder
                            .of(RealEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build("real")
            );


    public static final RegistryObject<EntityType<RealObserveEntity>> REAL_OBSERVE =
            ENTITY_TYPES.register(
                    "real_observe",
                    () -> EntityType.Builder
                            .of(RealObserveEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build("real_observe")
            );


    public static final RegistryObject<EntityType<Entity2020Entity>> ENTITY2020 =
            ENTITY_TYPES.register(
                    "entity2020",
                    () -> EntityType.Builder
                            .of(Entity2020Entity::new, MobCategory.MONSTER)
                            .sized(2.0F, 6.0F)
                            .build("entity2020")
            );


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}