package com.jax.funaethermod.events;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FunAetherMod.MODID)
public class HavenSleepHandler {

    /*
     * Haven dimension.
     *
     * IMPORTANT:
     * This is Haven, NOT Subsequence.
     */
    private static final ResourceKey<Level> HAVEN_DIMENSION =
            ResourceKey.create(
                    Registries.DIMENSION,
                    new ResourceLocation(
                            FunAetherMod.MODID,
                            "haven"
                    )
            );

    @SubscribeEvent
    public static void onWakeUp(
            PlayerWakeUpEvent event
    ) {

        /*
         * Server players only.
         */
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        /*
         * 1 in 4 chance to teleport.
         *
         * 0 = teleport
         * 1, 2, 3 = nothing happens
         */
        if (player.getRandom().nextInt(4) != 0) {
            return;
        }

        /*
         * Get the Haven dimension.
         */
        ServerLevel haven =
                player.server.getLevel(
                        HAVEN_DIMENSION
                );

        if (haven == null) {

            System.out.println(
                    "[FunAetherMod] Haven dimension not found."
            );

            return;
        }

        /*
         * Haven teleport location.
         */
        BlockPos spawn =
                new BlockPos(
                        0,
                        112,
                        0
                );

        /*
         * Teleport the player to Haven.
         */
        player.teleportTo(
                haven,
                spawn.getX() + 0.5D,
                spawn.getY(),
                spawn.getZ() + 0.5D,
                player.getYRot(),
                player.getXRot()
        );

        System.out.println(
                "[FunAetherMod] Player woke up in Haven."
        );
    }
}