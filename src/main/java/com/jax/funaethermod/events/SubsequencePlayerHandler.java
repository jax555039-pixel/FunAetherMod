package com.jax.funaethermod.events;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FunAetherMod.MODID)
public class SubsequencePlayerHandler {

    private static final ResourceLocation SUBSEQUENCE =
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "subsequence"
            );

    /*
     * 20 seconds = 400 ticks.
     */
    private static final int SLOW_FALLING_DURATION = 400;

    @SubscribeEvent
    public static void onPlayerTick(
            TickEvent.PlayerTickEvent event
    ) {

        /*
         * Only run once per tick on the server.
         */
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (event.player.level().isClientSide) {
            return;
        }

        /*
         * We need a real server player.
         */
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        /*
         * Check the dimension the player is CURRENTLY in.
         */
        if (!player.level()
                .dimension()
                .location()
                .equals(SUBSEQUENCE)) {

            return;
        }

        /*
         * Give Slow Falling.
         *
         * Because this is refreshed while the player remains
         * in Subsequence, the player effectively has it
         * continuously while inside the dimension.
         */
        player.addEffect(
                new MobEffectInstance(
                        MobEffects.SLOW_FALLING,
                        SLOW_FALLING_DURATION,
                        0,
                        false,
                        true,
                        true
                )
        );
    }
}