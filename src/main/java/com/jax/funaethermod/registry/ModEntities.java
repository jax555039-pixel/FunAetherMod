package com.jax.funaethermod.registry;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.entity.RealEntity;
import com.jax.funaethermod.entity.RealObserveEntity;
import com.jax.funaethermod.entity.Entity2020AttackEntity;
import com.jax.funaethermod.entity.Entity2020Entity;
import com.jax.funaethermod.entity.FakeEntity;
import com.jax.funaethermod.entity.PoorBoyEntity;

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

            
            public static final RegistryObject<EntityType<FakeEntity>> FAKE =
        ENTITY_TYPES.register(
                "fake",
                () -> EntityType.Builder
                        .of(FakeEntity::new, MobCategory.CREATURE)
                        .sized(0.6F, 1.8F)
                        .clientTrackingRange(8)
                        .build("fake")
        );

            public static final RegistryObject<EntityType<PoorBoyEntity>> POORBOY =
        ENTITY_TYPES.register(
                    "poorboy",
                    () -> EntityType.Builder
                            .of(PoorBoyEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.8F)
                            .clientTrackingRange(8)
                            .build("poorboy")
            );

            public static final RegistryObject<EntityType<Entity2020AttackEntity>> ENTITY2020_ATTACK =
    ENTITY_TYPES.register(
        "entity2020_attack",
        () -> EntityType.Builder
            .of(Entity2020AttackEntity::new, MobCategory.MONSTER)
            .sized(2.0F, 6.0F)
            .build("entity2020_attack")
    );
            

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}