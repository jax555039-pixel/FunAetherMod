package com.jax.funaethermod.registry;

import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.entity.Entity2020Entity;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

    }
}