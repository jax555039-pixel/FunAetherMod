package com.jax.funaethermod.entity;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.registry.ModEntities;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = FunAetherMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ModEntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {

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

/*
=========================================================
                 LEARNING CORNER
=========================================================

EntityAttributeCreationEvent
----------------------------
This event happens once when Minecraft starts.

Every living entity MUST have an AttributeSupplier.

Without one, Minecraft doesn't know things like:

- Max Health
- Movement Speed
- Follow Range
- Knockback Resistance

If you forget to register an entity here,
you'll get an error like:

NullPointerException:
AttributeMap.getValue()

because the entity has NO attributes.

event.put(...)
--------------
Links an EntityType to its attributes.

Example:

FAKE EntityType
        ↓
FakeEntity.createAttributes()

Minecraft stores those stats and gives them
to every FakeEntity when it spawns.
*/