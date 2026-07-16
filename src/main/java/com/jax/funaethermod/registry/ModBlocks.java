package com.jax.funaethermod.registry;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(
                    ForgeRegistries.BLOCKS,
                    FunAetherMod.MODID
            );


    public static final RegistryObject<Block> AETHER_PORTAL =
            BLOCKS.register(
                    "aether_portal",
                    () -> new Block(
                            BlockBehaviour.Properties.copy(Blocks.GLOWSTONE)
                    )
            );


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}