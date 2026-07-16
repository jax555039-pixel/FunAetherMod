package com.jax.funaethermod;

import com.jax.funaethermod.registry.ModBlocks;
import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.registry.ModSounds;
import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

import com.jax.funaethermod.registry.ModItems;
@Mod(FunAetherMod.MODID)
public class FunAetherMod {

    public static final String MODID = "funaethermod";

    private static final Logger LOGGER = LogUtils.getLogger();

    public FunAetherMod() {

        IEventBus modEventBus =
                FMLJavaModLoadingContext.get()
                        .getModEventBus();

        // Register blocks
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        // Register entities
        ModEntities.register(modEventBus);

        // Register sounds
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Fun Aether Mod loaded!");
    }


    private void commonSetup(final FMLCommonSetupEvent event) {

        LOGGER.info("Common setup complete!");
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        LOGGER.info("Server starting!");
    }
}