package com.jax.funaethermod.registry;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(
                    ForgeRegistries.SOUND_EVENTS,
                    FunAetherMod.MODID
            );

    public static final RegistryObject<SoundEvent> REAL_AMBIENT =
            SOUNDS.register(
                    "real_ambient",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(
                                    FunAetherMod.MODID,
                                    "real_ambient"
                            )
                    )
            );

    public static final RegistryObject<SoundEvent> REAL_GLITCH =
            SOUNDS.register(
                    "real_glitch",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(
                                    FunAetherMod.MODID,
                                    "real_glitch"
                            )
                    )
            );

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}