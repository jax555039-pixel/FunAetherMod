package com.jax.funaethermod.registry;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(
                    ForgeRegistries.ITEMS,
                    FunAetherMod.MODID
            );


    public static final RegistryObject<Item> AETHER_PORTAL_FRAME =
            ITEMS.register(
                    "aether_portal_frame",
                    () -> new BlockItem(
                            ModBlocks.AETHER_PORTAL_FRAME.get(),
                            new Item.Properties()
                    )
            );


    public static final RegistryObject<Item> AETHER_PORTAL =
            ITEMS.register(
                    "aether_portal",
                    () -> new BlockItem(
                            ModBlocks.AETHER_PORTAL.get(),
                            new Item.Properties()
                    )
            );


    public static final RegistryObject<Item> PURGATORY_GRASS_BLOCK =
            ITEMS.register(
                    "purgatory_grass_block",
                    () -> new BlockItem(
                            ModBlocks.PURGATORY_GRASS.get(),
                            new Item.Properties()
                    )
            );


    public static final RegistryObject<Item> PURGATORY_PORTAL =
            ITEMS.register(
                    "purgatory_portal",
                    () -> new BlockItem(
                            ModBlocks.PURGATORY_PORTAL.get(),
                            new Item.Properties()
                    )
            );


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}