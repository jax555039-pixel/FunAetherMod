package com.jax.funaethermod.world;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = FunAetherMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class SsIslandEvents {

    private static final ResourceLocation SUBSEQUENCE_DIMENSION =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "subsequence"
            );

    /*
     * Don't run the generator every single tick.
     *
     * 20 ticks = 1 second.
     */
    private static final int GENERATION_INTERVAL = 20;

    @SubscribeEvent
    public static void onPlayerTick(
            TickEvent.PlayerTickEvent event
    ) {

        /*
         * Only run once per tick cycle.
         */
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        /*
         * Server side only.
         */
        if (event.player.level().isClientSide()) {
            return;
        }

        /*
         * We only care about ServerPlayer.
         */
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        /*
         * Don't generate every tick.
         */
        if (player.tickCount % GENERATION_INTERVAL != 0) {
            return;
        }

        /*
         * Only generate Subsequence islands
         * in the Subsequence dimension.
         */
        ResourceLocation dimension =
                player.level()
                        .dimension()
                        .location();

        if (!dimension.equals(
                SUBSEQUENCE_DIMENSION
        )) {
            return;
        }

        /*
         * Generate the 3D grid around the player.
         */
        SsIslandGenerator.generateAroundPlayer(
                (net.minecraft.server.level.ServerLevel)
                        player.level(),
                player
        );
    }
}