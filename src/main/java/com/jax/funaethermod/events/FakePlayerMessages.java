package com.jax.funaethermod.events;

import com.jax.funaethermod.FunAetherMod;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(
    modid = FunAetherMod.MODID,
    bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FakePlayerMessages {

    /*
    *=========================================================
    * FAKE PLAYER NAMES
    * =========================================================
    */
    

    private static final String REALNESS_NAME = "Realness_12321";
    private static final String FAKESHADOW_NAME = "xXFakeShadowXx";
    private static final String COOLBOY_NAME = "CoolBoy_2012";
    private static final String TRICKY_NAME = "xXTrickyXx";


    /*
    * =========================================================
    * RANDOM
    * =========================================================
    */
    
    private static final Random RANDOM = new Random();


    /*
    * ========================================================
    * CONVERSATION STATE
    *
    */

    private static int fakePlayerConversationTimer = 0;

    private static int fakePlayerConversationStep = 0;

    private static String activeFakePlayerName = null;

    /*
    * =========================================================
    * SESSION STATE
    * =========================================================
    */

    private static boolean realnessHasJoinedThisSession = false;
    private static boolean fakeShadowHasJoinedThisSession = false;
    private static boolean coolBoyHasJoinedThisSession = false;
    private static boolean trickyHasJoinedThisSession = false;


    /*
    * ========================================================
    * SERVER START
    * ========================================================
    */
    
    @SubscribeEvent
    public static void onServerStarting(
            ServerStartingEvent event
    ) {

        System.out.println(
            "[FunAetherMod] FakePlayerMessages loaded."
        );
    }

    /*
    * ========================================================
    * PLAYER JOIN
    * ========================================================
     */

    @SubscribeEvent
    public static void onPlayerJoin(
            PlayerEvent.PlayerLoggedInEvent event
    ) {

        /*
        * only announce real players
        */

        if (!event.getEntity().level().isClientSide) {

            String name =
                event.getEntity()
                        .getName()
                        .getString();

                System.out.println(
                    "[FunAetherMod] Player joined: " + name
                );
        }
    }


    /*
    * ========================================================
    * PLAYER LEAVE
    * =======================================================
    */

    @SubscribeEvent
    public static void onPlayerLeave(
            PlayerEvent.PlayerLoggedOutEvent event
    ) {

        /*
        * only announce real players
        */

        if (!event.getEntity().level().isClientSide) {

            String name =
                event.getEntity()
                        .getName()
                        .getString();

                System.out.println(
                    "[FunAetherMod] Player left: " + name
                );
        }
    }

    /*
    * =======================================================
    * SERVER TICK
    * =======================================================
    */

    @SubscribeEvent
    public static void onServerTick(
            TickEvent.ServerTickEvent event
    ) {

        /*
        * we only want one tick phase.
        */

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (event.getServer() == null) {
            return;
        }

        /*
        * use the overworld as the clock
        */

        ServerLevel level =
            event.getServer()
                    .getLevel(Level.OVERWORLD);

            if (level == null) {
                return;
            }

            /*
            * =======================================================
            * WORLD DAY
            * ======================================================
            */

            long day =
                level.getDayTime() / 24000L;



                /*
                * Fake-player events begin on day 2.
                */

                if (day < 2) {
                    return;
                }


                /*
                * =======================================================
                * CONVERSATION TIMER
                * =======================================================
                */

                if (fakePlayerConversationTimer > 0) {

                    fakePlayerConversationTimer--;

                    return;
                }

                /*
                * =======================================================
                * SELECT FAKE PLAYER
                * ======================================================
                */


                if (activeFakePlayerName == null) {

                    /*
                    * 1/3 chance per timer expiration
                    * to begin an event
                    */

                    if (RANDOM.nextInt(3) != 0) {
                        return;
                    }

                    String candidate = null;

                    /*
                    * =======================================================
                    * REALNESS
                    * =====================================================
                    */

                    if (
                                !realnessHasJoinedThisSession
                                        && RANDOM.nextBoolean()
                    ) {


                        candidate = REALNESS_NAME;
                    }


                    /*
                    * =======================================================
                    * FAKESHADOW
                    * ======================================================
                    */

                    else if (
                                !fakeShadowHasJoinedThisSession
                                        && RANDOM.nextBoolean()
                    ) {

                        candidate = FAKESHADOW_NAME;
                    }


                    /*
                    * =======================================================
                    * COOLBOY
                    * =====================================================
                    */

                    else if (
                                !coolBoyHasJoinedThisSession
                                        && RANDOM.nextBoolean()
                    ) {

                        candidate = COOLBOY_NAME;
                    }
                    

                    /*
                    * =======================================================
                    * TRICKY
                    * ======================================================
                    */

                    else if (
                                !trickyHasJoinedThisSession
                                        
                    ) {

                        candidate = TRICKY_NAME;
                    }

                    /*
                    * No candidate available.
                    */

                    if (candidate == null) {
                        return;
                    }

                    activeFakePlayerName = candidate;

                    fakePlayerConversationStep = 0;
                }

                /*
                * ======================================================
                * CONVERSATION
                * =====================================================
                */

                switch (fakePlayerConversationStep) {


                    /*
                    * =====================================================
                    * JOIN
                    * =====================================================
                    */
                   

                    case 0 -> {

                        broadcast(
                                level,
                                activeFakePlayerName + " joined the game."
                        );

                        fakePlayerConversationStep = 1;

                        /*
                        * wait 10 seconds
                        */

                        fakePlayerConversationTimer = 
                                20 * 10;
                    }

                    /*
                    * =====================================================
                    * MESSAGE
                    * ====================================================
                    */

                    case 1 -> {

                        String message =
                            switch (activeFakePlayerName) {

                                case REALNESS_NAME -> "6d 79 20 73 6b 69 6e 20 69 73 20 67 6f 6e 65 2e 2e 2e";
                                case FAKESHADOW_NAME -> "00101110 00101110 00101110";
                                case COOLBOY_NAME -> "69 6d 20 73 6f 72 72 79 20 63 6a 2e 2e 2e";
                                case TRICKY_NAME -> "6d 79 20 61 72 6d 73 20 68 61 76 65 20 62 65 65 6e 20 73 74 72 65 74 63 68 65 64 20 6f 75 74 20 61 6c 6f 6e 67 20 77 69 74 68 20 6d 79 20 6c 65 67 73 20 68 69 73 20 77 69 72 65 73 20 61 72 65 20 70 6f 6b 69 6e 67 20 6f 75 74 20 6f 66 20 6d 79 20 73 69 64 65 73 2e 20 69 20 61 6d 20 69 6e 20 70 61 69 6e 2e 20 6e 6f 74 20 64 75 6c 6c 20 6f 72 20 73 68 61 72 70 20 6f 72 20 65 76 65 6e 20 6d 65 6e 74 61 6c 2e 20 6a 75 73 74 2e 2e 2e 20 70 61 69 6e 2e";
                                default -> "dont go to the ~#%#$";    
                            };


                        broadcast(
                                level,
                                activeFakePlayerName + ": " + message
                        );

                        fakePlayerConversationStep = 2;

                        /*
                        * wait 10 seconds
                        */

                        fakePlayerConversationTimer = 
                                20 * 10;
                    }

                    /*
                    * ======================================================
                    * LEAVE
                    * =====================================================
                    */

                    case 2 -> {

                        broadcast(
                                level,
                                activeFakePlayerName + " left the game."
                        );

                        /*
                        * Mark(101) this charactar as used
                        * mark 101 is THE best arg oat 
                        */

                        if (
                            activeFakePlayerName
                                    .equals(REALNESS_NAME)
                        ) {
                            realnessHasJoinedThisSession = true;
                        }

                        else if (
                            activeFakePlayerName
                                    .equals(FAKESHADOW_NAME)
                        ) {
                            fakeShadowHasJoinedThisSession = true;
                        }

                        else if (
                            activeFakePlayerName
                                    .equals(COOLBOY_NAME)
                        ) {
                            coolBoyHasJoinedThisSession = true;
                        }

                        else if (
                            activeFakePlayerName
                                    .equals(TRICKY_NAME)
                        ) {
                            trickyHasJoinedThisSession = true;
                        }

                        /*
                        * Reset.
                        */

                        activeFakePlayerName = null;
                        fakePlayerConversationStep = 0;

                        /*
                        * Wait 3 minutes 
                        */

                        fakePlayerConversationTimer = 
                                20 * 60 * 3;
                }
            }
        }

        /*
        * ========================================================
        * BROADCAST MESSAGE
        * ========================================================
        */

        private static void broadcast(
                ServerLevel level,
                String message
        ) {

            level.getServer()
                    .getPlayerList()
                    .broadcastSystemMessage(
                            Component.literal(message),
                            false
                    );

                    System.out.println(
                        "[FunAetherMod] " + message
                    );
        }
}