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

            public static final RegistryObject<SoundEvent> TRICK_AMBIENT =
                registerSound("trick_ambient");

    public static final RegistryObject<SoundEvent> FAKE_CHASE =
            registerSound("fake_chase");

    public static final RegistryObject<SoundEvent> AETHER_AMBIENT =
            registerSound("aether_ambient");

    public static final RegistryObject<SoundEvent> HOLLOW_AMBIENT =
            registerSound("hollow_ambient");

    public static final RegistryObject<SoundEvent> RANDOM_ENTITY2020_COUNTDOWN =
            registerSound("random_entity2020_countdown");

    public static final RegistryObject<SoundEvent> GLUTTONY_AMBIENT =
            registerSound("gluttony_ambient");

    public static final RegistryObject<SoundEvent> SPORE_AMBIENT =
            registerSound("spore_ambient");
 
    public static final RegistryObject<SoundEvent> ENTITY_PLAYER_BREATH =
                registerSound("entity_player_breath");

    public static final RegistryObject<SoundEvent> REAL_FLESH_SETTEL =
                registerSound("real_flesh_settel");

    public static final RegistryObject<SoundEvent> FAKE_BREATH_TRY =
                registerSound("fake_breath_try");

    public static final RegistryObject<SoundEvent> CORRUPT_CHASE =
                registerSound("corrupt_chase");

    public static final RegistryObject<SoundEvent> CORRUPT_SCREEN_FLASH =
                registerSound("corrupt_screen_flash");

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