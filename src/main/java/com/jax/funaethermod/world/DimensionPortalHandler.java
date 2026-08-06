package com.jax.funaethermod.world;


import com.jax.funaethermod.FunAetherMod;
import com.jax.funaethermod.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jax.funaethermod.portal.DimensionPortalManager;

public class DimensionPortalHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DimensionPortalHandler.class);

    private static final Set<UUID> aetherGenerated =
            new HashSet<>();


    private static final Set<UUID> purgatoryGenerated =
            new HashSet<>();

   

    public DimensionPortalHandler() {


        MinecraftForge.EVENT_BUS.register(this);


    }



    // ==========================
    // DIMENSION RETURN PORTAL SYSTEM
    // ==========================



    @SubscribeEvent
    public void playerTick(
            TickEvent.PlayerTickEvent event
    ) {


        if(event.phase != TickEvent.Phase.END)
            return;


        if(!(event.player instanceof ServerPlayer player))
            return;


        if(player.level().isClientSide)
            return;


        ResourceLocation dimension =
                player.level()
                        .dimension()
                        .location();


        if(dimension.equals(
                new ResourceLocation(
                        FunAetherMod.MODID,
                        "aether"
                )
        )) {

            if(!aetherGenerated.contains(player.getUUID())) {

                generateAetherReturnPortals(
                        player.serverLevel(),
                        player.getUUID(),
                        player.blockPosition()
                );

            }

        }


        if(dimension.equals(
                new ResourceLocation(
                        FunAetherMod.MODID,
                        "purgatory"
                )
        )) {

            if(!purgatoryGenerated.contains(player.getUUID())) {

                generatePurgatoryReturnPortal(
                        player.serverLevel(),
                        player.getUUID(),
                        player.blockPosition()
                );

            }

        }

    }


    // ==========================
    // AETHER RETURN PORTAL GENERATION
    // ==========================


    private void generateAetherReturnPortals(
            ServerLevel level,
            UUID playerID,
            BlockPos spawn
    ) {


        if(aetherGenerated.contains(playerID))
            return;



        // 100 chunks south
        BlockPos south =
                findLand(
                        level,
                        spawn,
                        0,
                        -1600
                );



        DimensionPortalManager.generateAetherReturnPortal(
                level,
                south
        );

         LOGGER.info(
"Aether south return portal generated at X:{} Y:{} Z:{}",
south.getX(),
south.getY(),
south.getZ()
);


        // 300 blocks east
        BlockPos east =
                findLand(
                        level,
                        spawn,
                        300,
                        0
                );



        DimensionPortalManager.generateAetherReturnPortal(
                level,
                east
        );

        LOGGER.info(
"Aether east return portal generated at X:{} Y:{} Z:{}",
east.getX(),
east.getY(),
east.getZ()
);



        // 500 chunks north
        BlockPos north =
                findLand(
                        level,
                        spawn,
                        0,
                        8000
                );



        DimensionPortalManager.generateAetherReturnPortal(
                level,
                north
        );

        LOGGER.info(
"Aether north return portal generated at X:{} Y:{} Z:{}",
north.getX(),
north.getY(),
north.getZ()
);


        aetherGenerated.add(playerID);


    }






    // ==========================
    // FIND SAFE LAND
    // ==========================


    private BlockPos findLand(
            ServerLevel level,
            BlockPos start,
            int offsetX,
            int offsetZ
    ) {


        BlockPos target =
                start.offset(
                        offsetX,
                        0,
                        offsetZ
                );



        for(int i = 0; i < 100; i++) {


            BlockPos check =
                    target.offset(
                            i,
                            0,
                            i
                    );



            BlockPos surface =
                    level.getHeightmapPos(
                            net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                            check
                    );



            if(level.getBlockState(
                    surface.below()
            ).isSolid()) {


                return surface;

            }

        }



        return target;


    }

        // ==========================
    // PURGATORY RETURN PORTAL GENERATION
    // ==========================


    private void generatePurgatoryReturnPortal(
            ServerLevel level,
            UUID playerID,
            BlockPos spawn
    ) {


        if(purgatoryGenerated.contains(playerID))
            return;



        int direction =
                level.random.nextInt(4);



        BlockPos portalPos;



        switch(direction) {


            case 0 -> portalPos =
                    findLand(
                            level,
                            spawn,
                            1600,
                            0
                    );


            case 1 -> portalPos =
                    findLand(
                            level,
                            spawn,
                            -1600,
                            0
                    );


            case 2 -> portalPos =
                    findLand(
                            level,
                            spawn,
                            0,
                            1600
                    );


            default -> portalPos =
                    findLand(
                            level,
                            spawn,
                            0,
                            -1600
                    );

        }



        DimensionPortalManager.generatePurgatoryReturnPortal(
                level,
                portalPos
        );

        LOGGER.info(
"Purgatory return portal generated at X:{} Y:{} Z:{}",
portalPos.getX(),
portalPos.getY(),
portalPos.getZ()
);

        purgatoryGenerated.add(playerID);


    }






    

        // ==========================
    // PORTAL TELEPORT HANDLER
    // ==========================


    @SubscribeEvent
    public void portalTick(
            TickEvent.PlayerTickEvent event
    ) {


        if(event.phase != TickEvent.Phase.END)
            return;


        if(event.player.level().isClientSide)
            return;



        ServerPlayer player =
                (ServerPlayer) event.player;



        BlockPos playerPos =
                player.blockPosition();



        // ==========================
        // AETHER RETURN
        // ==========================


        if(player.level()
                .dimension()
                .location()
                .equals(
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                "aether"
                        )
                )) {


            if(isOnPortal(
                    player.level(),
                    playerPos
            )) {


                ServerLevel overworld =
                        player.server
                                .getLevel(
                                        Level.OVERWORLD
                                );


                if(overworld != null) {


                    player.teleportTo(
                            overworld,
                            player.getX(),
                            overworld.getHeight(
                                    Heightmap.Types.WORLD_SURFACE,
                                    (int)player.getX(),
                                    (int)player.getZ()
                            ),
                            player.getZ(),
                            player.getYRot(),
                            player.getXRot()
                    );


                }

            }

        }





        // ==========================
        // PURGATORY RETURN
        // ==========================


        if(player.level()
                .dimension()
                .location()
                .equals(
                        new ResourceLocation(
                                FunAetherMod.MODID,
                                "purgatory"
                        )
                )) {



            if(isOnPortal(
                    player.level(),
                    playerPos
            )) {


                ServerLevel overworld =
                        player.server
                                .getLevel(
                                        Level.OVERWORLD
                                );



                if(overworld != null) {


                    player.teleportTo(
                            overworld,
                            0,
                            overworld.getHeight(
                                    Heightmap.Types.WORLD_SURFACE,
                                    0,
                                    0
                            ),
                            0,
                            player.getYRot(),
                            player.getXRot()
                    );


                }

            }

        }

    }







    // ==========================
    // CHECK PORTAL BLOCK
    // ==========================


    private boolean isOnPortal(
            net.minecraft.world.level.Level level,
            BlockPos pos
    ) {


        BlockState state =
                level.getBlockState(pos);



        return state.is(
                ModBlocks.PURGATORY_PORTAL.get()
        )
        ||
        state.is(
                ModBlocks.AETHER_PORTAL.get()
        );


    }
}


