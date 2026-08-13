package com.jax.funaethermod;

import com.jax.funaethermod.registry.ModBlocks;
import com.jax.funaethermod.registry.ModEntities;
import com.jax.funaethermod.registry.ModItems;
import com.jax.funaethermod.registry.ModSounds;
import com.jax.funaethermod.world.DimensionPortalHandler;

import com.mojang.logging.LogUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

import java.util.Random;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@Mod(FunAetherMod.MODID)
public class FunAetherMod {

    public static final String MODID = "funaethermod";

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Random RANDOM = new Random();

    /*
     * =========================================================
     * FAKE PLAYER NAMES
     * =========================================================
     */

    private static final String REALNESS_NAME = "realness_12321";
    private static final String FAKESHADOW_NAME = "xXfakeshadowXx";
    private static final String COOLBOY_NAME = "coolboy_2012";


    /*
     * =========================================================
     * FAKE PLAYER STATE
     * =========================================================
     */

    private int fakePlayerConversationTimer = 0;

    private int fakePlayerConversationStep = 0;

    private String activeFakePlayerName = null;

    private boolean realnessHasJoinedThisSession = false;
    private boolean fakeShadowHasJoinedThisSession = false;
    private boolean coolboyHasJoinedThisSession = false;


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
         * Register mod content.
         */

        ModBlocks.register(modEventBus);

        ModItems.register(modEventBus);

        ModEntities.register(modEventBus);

        ModSounds.register(modEventBus);


        /*
         * Register Forge events.
         */

        MinecraftForge.EVENT_BUS.register(this);


        /*
         * Portal handler.
         *
         * This handles the actual portal mechanics.
         * Natural portal generation is handled separately.
         */

        new DimensionPortalHandler();


        LOGGER.info("Fun Aether Mod loaded!");
    }


    /*
     * =========================================================
     * SERVER START
     * =========================================================
     */

    @SubscribeEvent
    public void onServerStarting(
            ServerStartingEvent event
    ) {

        LOGGER.info("Server starting!");
    }


    /*
     * =========================================================
     * PLAYER JOIN
     * =========================================================
     */

    @SubscribeEvent
    public void onPlayerJoin(
            PlayerEvent.PlayerLoggedInEvent event
    ) {

        if (!(event.getEntity()
                instanceof ServerPlayer player)) {

            return;
        }

        String name =
                player.getName()
                        .getString();

        player.serverLevel()
                .getServer()
                .getPlayerList()
                .broadcastSystemMessage(
                        Component.literal(
                                name + " joined the game"
                        ),
                        false
                );
    }


    /*
     * =========================================================
     * PLAYER LEAVE
     * =========================================================
     */

    @SubscribeEvent
    public void onPlayerLeave(
            PlayerEvent.PlayerLoggedOutEvent event
    ) {

        if (!(event.getEntity()
                instanceof ServerPlayer player)) {

            return;
        }

        String name =
                player.getName()
                        .getString();

        player.serverLevel()
                .getServer()
                .getPlayerList()
                .broadcastSystemMessage(
                        Component.literal(
                                name + " left the game"
                        ),
                        false
                );
    }


    /*
     * =========================================================
     * SERVER TICK
     * =========================================================
     *
     * Handles the fake-player conversation events.
     */

    @SubscribeEvent
    public void onServerTick(
            TickEvent.ServerTickEvent event
    ) {

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (event.getServer() == null) {
            return;
        }

        ServerLevel level =
                event.getServer()
                        .getLevel(Level.OVERWORLD);

        if (level == null) {
            return;
        }


        /*
         * Determine the current Minecraft day.
         */

        long day =
                getWorldDay(level);


        /*
         * Fake-player events don't begin
         * until day 2.
         */

        if (day < 2) {
            return;
        }


        /*
         * Conversation timer.
         */

        if (fakePlayerConversationTimer > 0) {

            fakePlayerConversationTimer--;

            return;
        }


        /*
         * =====================================================
         * SELECT FAKE PLAYER
         * =====================================================
         */

        if (activeFakePlayerName == null) {

            /*
             * 1/3 chance to begin checking
             * for a fake player.
             */

            if (RANDOM.nextInt(3) != 0) {
                return;
            }


            String candidate = null;


            /*
             * Realness.
             */

            if (
                    !realnessHasJoinedThisSession
                            && RANDOM.nextBoolean()
            ) {

                candidate = REALNESS_NAME;
            }


            /*
             * FakeShadow.
             */

            else if (
                    !fakeShadowHasJoinedThisSession
                            && day >= 2
                            && RANDOM.nextBoolean()
            ) {

                candidate = FAKESHADOW_NAME;
            }


            /*
             * Coolboy.
             */

            else if (
                    !coolboyHasJoinedThisSession
                            && day >= 2
            ) {

                candidate = COOLBOY_NAME;
            }


            /*
             * No available candidate.
             */

            if (candidate == null) {
                return;
            }


            activeFakePlayerName = candidate;

            fakePlayerConversationStep = 0;
        }


        /*
         * =====================================================
         * CONVERSATION
         * =====================================================
         */

        switch (fakePlayerConversationStep) {


            /*
             * -------------------------------------------------
             * JOIN MESSAGE
             * -------------------------------------------------
             */

            case 0 -> {

                level.getServer()
                        .getPlayerList()
                        .broadcastSystemMessage(
                                Component.literal(
                                        activeFakePlayerName
                                                + " joined the game"
                                ),
                                false
                        );

                fakePlayerConversationStep = 1;

                fakePlayerConversationTimer =
                        20 * 10;
            }


            /*
             * -------------------------------------------------
             * MESSAGE
             * -------------------------------------------------
             */

            case 1 -> {

                String message =
                        switch (activeFakePlayerName) {

                            case REALNESS_NAME ->
                                    "my skin is gone";

                            case FAKESHADOW_NAME ->
                                    "where's my friends";

                            case COOLBOY_NAME ->
                                    "they're crawling in my face";

                            default ->
                                    "i want to go home";
                        };


                level.getServer()
                        .getPlayerList()
                        .broadcastSystemMessage(
                                Component.literal(
                                        activeFakePlayerName
                                                + ": "
                                                + message
                                ),
                                false
                        );

                fakePlayerConversationStep = 2;

                fakePlayerConversationTimer =
                        20 * 10;
            }


            /*
             * -------------------------------------------------
             * LEAVE MESSAGE
             * -------------------------------------------------
             */

            case 2 -> {

                level.getServer()
                        .getPlayerList()
                        .broadcastSystemMessage(
                                Component.literal(
                                        activeFakePlayerName
                                                + " left the game"
                                ),
                                false
                        );


                /*
                 * Mark this fake player as having
                 * appeared during this session.
                 */

                if (
                        activeFakePlayerName
                                .equals(REALNESS_NAME)
                ) {

                    realnessHasJoinedThisSession = true;

                } else if (
                        activeFakePlayerName
                                .equals(FAKESHADOW_NAME)
                ) {

                    fakeShadowHasJoinedThisSession = true;

                } else if (
                        activeFakePlayerName
                                .equals(COOLBOY_NAME)
                ) {

                    coolboyHasJoinedThisSession = true;
                }


                /*
                 * Reset conversation.
                 */

                activeFakePlayerName = null;

                fakePlayerConversationStep = 0;

                /*
                 * Wait 3 minutes before another
                 * fake-player event can happen.
                 */

                fakePlayerConversationTimer =
                        20 * 60 * 3;
            }
        }
    }


    /*
     * =========================================================
     * WORLD DAY
     * =========================================================
     */

    private long getWorldDay(
            ServerLevel level
    ) {

        return level.getDayTime() / 24000L;
    }

    // ========================================================
    // SUBSEQUENCE SLOW FALLING
    // ========================================================

   @SubscribeEvent
public void onPlayerChangedDimension(
        PlayerEvent.PlayerChangedDimensionEvent event
) {

    if (!(event.getEntity() instanceof ServerPlayer player)) {
        return;
    }

    /*
     * Only activate when the player enters
     * the Subsequence dimension.
     */
    if (!player.level().dimension().location().equals(
            new ResourceLocation(
                    FunAetherMod.MODID,
                    "subsequence"
            )
    )) {
        return;
    }

    /*
     * 20 seconds = 400 Minecraft ticks.
     */
    int duration = 20 * 20;

    /*
     * Slow Falling.
     */
    player.addEffect(
            new MobEffectInstance(
                    MobEffects.SLOW_FALLING,
                    duration,
                    0,
                    false,
                    true
            )
    );

    

    LOGGER.info(
            "[FunAetherMod] {} entered Subsequence - applying 20 second air effects.",
            player.getName().getString()
    );
}
}