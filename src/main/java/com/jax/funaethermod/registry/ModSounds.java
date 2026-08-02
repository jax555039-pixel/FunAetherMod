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
            registerSound("real_ambient");



    public static final RegistryObject<SoundEvent> REAL_GLITCH =
            registerSound("real_glitch");



    public static final RegistryObject<SoundEvent> REAL_TRANSFORM =
            registerSound("real_transform");

    public static final RegistryObject<SoundEvent> ENTITY2020_AMBIENT =
            registerSound("entity2020_ambient");

    public static final RegistryObject<SoundEvent> ENTITY2020_ATTACK =
            registerSound("entity2020attack");

    public static final RegistryObject<SoundEvent> FAKE_AMBIENT =
            registerSound("fake_ambient");

    public static final RegistryObject<SoundEvent> POORBOY_CRY =
            registerSound("poorboy_cry");


    private static RegistryObject<SoundEvent> registerSound(String name) {

        return SOUNDS.register(
                name,
                () -> SoundEvent.createVariableRangeEvent(
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                name
                        )
                )
        );
    }





    public static void register(IEventBus eventBus) {

        SOUNDS.register(eventBus);
    }
}