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


    public static final RegistryObject<Item> AETHER_PORTAL =
            ITEMS.register(
                    "aether_portal",
                    () -> new BlockItem(
                            ModBlocks.AETHER_PORTAL.get(),
                            new Item.Properties()
                    )
            );


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}