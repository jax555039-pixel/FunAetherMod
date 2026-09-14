package com.jax.funaethermod;

import com.jax.funaethermod.registry.ModBlocks;
import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.registry.ModItems;
import com.jax.funaethermod.registry.ModSounds;
import com.jax.funaethermod.world.DimensionPortalHandler;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

@Mod(FunAetherMod.MODID)
public class FunAetherMod {

    public static final String MODID = "funaethermod";

    public static final Logger LOGGER = LogUtils.getLogger();

    /*
     * =========================================================
     * CONSTRUCTOR
     * =========================================================
     */

    public FunAetherMod() {

        IEventBus modEventBus =
                FMLJavaModLoadingContext
                        .get()
                        .getModEventBus();


        /*
         * =====================================================
         * REGISTER MOD CONTENT
         * =====================================================
         */

        ModBlocks.register(modEventBus);

        ModItems.register(modEventBus);

        ModEntities.register(modEventBus);

        ModSounds.register(modEventBus);


        /*
         * =====================================================
         * REGISTER FORGE EVENTS
         * =====================================================
         *
         * Other event classes, such as FakePlayerMessages,
         * can register themselves separately using
         * @Mod.EventBusSubscriber.
         *
         * Keeping those systems out of this main class makes
         * FunAetherMod easier to manage.
         */

        MinecraftForge.EVENT_BUS.register(this);


        /*
         * =====================================================
         * PORTAL HANDLER
         * =====================================================
         *
         * Handles the actual portal mechanics.
         * Natural portal generation is handled separately.
         */

        new DimensionPortalHandler();


        /*
         * =====================================================
         * MOD LOADED
         * =====================================================
         */

        LOGGER.info("Fun Aether Mod loaded!");
    }
}